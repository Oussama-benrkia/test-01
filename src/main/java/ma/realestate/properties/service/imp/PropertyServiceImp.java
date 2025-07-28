package ma.realestate.properties.service.imp;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ma.realestate.properties.common.PagesResponse;
import ma.realestate.properties.dto.request.PropertyDetailsRequest;
import ma.realestate.properties.dto.request.PropertyRequest;
import ma.realestate.properties.dto.response.ChangeStatusResponse;
import ma.realestate.properties.dto.response.PropertyDetailsResponse;
import ma.realestate.properties.dto.response.PropertyResponse;
import ma.realestate.properties.dto.util.SortInput;
import ma.realestate.properties.entity.*;
import ma.realestate.properties.entity.enumu.StatusProperty;
import ma.realestate.properties.exception.InvalidOperationException;
import ma.realestate.properties.helper.StringHelper;
import ma.realestate.properties.mapper.imp.PropertyDetailsMapper;
import ma.realestate.properties.mapper.imp.PropertyMapper;
import ma.realestate.properties.repository.PropertyFeatureLinkRepository;
import ma.realestate.properties.repository.PropertyFeaturesRepository;
import ma.realestate.properties.repository.PropertyRepository;
import ma.realestate.properties.repository.PropertyTypeRepository;
import ma.realestate.properties.service.PropertyService;
import ma.realestate.properties.specification.GlobalSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PropertyServiceImp implements PropertyService {
    private final PropertyRepository propertyRepository;
    private final PropertyTypeRepository propertyTypeRepository;
    private final PropertyFeatureLinkRepository linkRepository;
    private final PropertyFeaturesRepository featuresRepository;

    private final PropertyDetailsMapper propertyDetailsMapper;
    private final PropertyMapper propertyMapper;

    private final StringHelper stringHelper;


    @Override
    @Transactional
    public Optional<PropertyResponse> add(PropertyRequest request) {
        Property property = this.propertyMapper.toEntity(request);
        property.setSlug(checkSlug(request.name()));
        property.setType(this.checkPropertyType(request.type()));
        if(request.extra()!=null) {
            property = this.updateExtraInfo(property,request.extra()).getResponse().getProperty();
        }
        property = propertyRepository.saveAndFlush(property);  // flush pour que created soit à jour

        if(request.features()!=null) {
            property = addUpdateFeature(property,request.features()).getResponse();
            property = propertyRepository.saveAndFlush(property); // flush aussi après modifs features
        }

        return Optional.ofNullable(propertyMapper.toResponse(property));
    }


    @Override
    @Transactional
    public Optional<PropertyResponse> remove(UUID id) {
        Property property = this.getPropertyById(id);
        if (property.getDeleted() != null) {
            throw new InvalidOperationException("Property '" + property.getName() + "' is already deleted.");
        }
        if (property.getLinks() != null) {
            for (PropertyFeatureLink link : property.getLinks()) {
                if (link.getFeature().isAutoRestore()) {
                    link.setDeletedAt(LocalDateTime.now());
                    linkRepository.save(link);
                } else {
                    linkRepository.delete(link);
                }
            }
        }
        property.setStatus(StatusProperty.DRAFT);
        property.setVerified(false);
        property.setDeleted(LocalDateTime.now());
        Property saved = propertyRepository.save(property);
        return Optional.ofNullable(propertyMapper.toResponse(saved));
    }

    @Override
    public Optional<PropertyResponse> getById(UUID id) {
        Property property = this.getPropertyById(id);
        if (property.getDeleted() != null) {
            throw new InvalidOperationException("Property  '" + property.getName() + "' is already deleted.");
        }
        return Optional.ofNullable(propertyMapper.toResponse(property));
    }

    @Override
    public Optional<ChangeStatusResponse<PropertyResponse>> update(UUID id, PropertyRequest request) {
        boolean changed = false;
        Property property = this.getPropertyById(id);
        if(property.getDeleted() != null) {
            throw new InvalidOperationException("Property '" + property.getName() + "' is already deleted.");
        }
        if (!request.name().isBlank() && !request.name().trim().equals(property.getName())) {
            property.setName(request.name().trim());
            property.setSlug(checkSlug(request.name().trim()));
            changed = true;
        }
        if (!request.description().isBlank() && !request.description().trim().equals(property.getDescription())) {
            property.setDescription(request.description().trim());
            changed = true;
        }
        if (!request.city().isBlank() && !request.city().trim().equals(property.getCity())) {
            property.setCity(request.city().trim());
            changed = true;
        }
        if (!request.address().isBlank() && !request.address().trim().equals(property.getAddress())) {
            property.setAddress(request.address().trim());
            changed = true;
        }
        if (request.latitude() != null && !request.latitude().equals(property.getLatitude())) {
            property.setLatitude(request.latitude());
            changed = true;
        }
        if (request.longitude() != null && !request.longitude().equals(property.getLongitude())) {
            property.setLongitude(request.longitude());
            changed = true;
        }
        if(request.type()!=null && !request.type().equals(property.getType().getId())) {
            property.setType(this.checkPropertyType(request.type()));
            changed = true;
        }
        ChangeStatusResponse<PropertyDetails> data =this.updateExtraInfo(property,request.extra());
        if(data.getChanged()) {
            property=data.getResponse().getProperty();
            changed=true;
        }
        ChangeStatusResponse<Property> feature = this.addUpdateFeature(property,request.features());
        if(feature.getChanged()) {
            property=data.getResponse().getProperty();
            changed=true;
        }
        if(changed){
            property.setStatus(StatusProperty.DRAFT);
            property.setVerified(false);
            propertyRepository.save(property);
        }
        return changeResponse(propertyMapper.toResponse(property), changed);
    }

    @Override
    public List<PropertyResponse> getAll(String search, SortInput sort) {
            return getFiltered(search, toSort(sort), false)
                    .stream()
                    .map(propertyMapper::toResponse)
                    .toList();
    }

    @Override
    public PagesResponse<PropertyResponse> paginate(String search, SortInput sort, int page, int size) {
            return getPagedResponse(search, toSort(sort), page, size, false);
    }

    @Override
    public PagesResponse<PropertyResponse> trash(String search, SortInput sort, int page, int size) {
            return getPagedResponse(search, toSort(sort), page, size, true);
    }

    @Override
    @Transactional
    public Optional<PropertyResponse> recovory(UUID id) {
        Property property = this.getPropertyById(id);
        if (property.getDeleted() == null) {
            throw new InvalidOperationException("Property '" + property.getName() + "' is not deleted.");
        }
        if(property.getLinks()!=null) {
            property.getLinks().forEach(link -> link.setDeletedAt(null));
        }
        property.setStatus(StatusProperty.DRAFT);
        property.setVerified(false);
        property.setDeleted(null);
        return Optional.ofNullable(propertyMapper.toResponse(propertyRepository.save(property)));
    }

    @Override
    @Transactional
    public Optional<PropertyResponse> destroy(UUID id) {
        Property property = this.getPropertyById(id);
        if (property.getDeleted() == null) {
            throw new InvalidOperationException("Property '" + property.getName() + "' is not deleted.");
        }
        if(property.getLinks()!=null) {
            linkRepository.deleteAll(property.getLinks());
        }
        property.setType(null);
        propertyRepository.delete(property);
        return Optional.ofNullable(propertyMapper.toResponse(property));
    }

    @Override
    public Optional<PropertyResponse> getBySlug(String slug) {
        Property property = propertyRepository.findBySlug(slug)
                .orElseThrow(() -> new EntityNotFoundException("Property '" + slug + "' not found"));
        return Optional.ofNullable(propertyMapper.toResponse(property));
    }

    @Override
    public Optional<ChangeStatusResponse<PropertyResponse>> changeStatus(UUID id, StatusProperty newStatus) {
        Property property = this.getPropertyById(id);
        if (property.getDeleted() != null) {
            throw new InvalidOperationException("La propriété '" + property.getName() + "' est déjà supprimée.");
        }
        boolean statusChanged = false;
        switch (newStatus) {
            case PUBLISHED -> {
                if (property.isVerified()) {
                    statusChanged = true;
                    property.setPublishedAt(LocalDateTime.now());
                    property.setPublished(true);
                }
            }
            case DRAFT -> {
                statusChanged = true;
                property.setPublishedAt(null);
            }
        }
        if (statusChanged) {
            property.setStatus(newStatus);
            propertyRepository.save(property);
        }
        PropertyResponse response = propertyMapper.toResponse(property);
        return Optional.of(ChangeStatusResponse.buildResponse(response, statusChanged));
    }

    @Override
    public Optional<ChangeStatusResponse<PropertyResponse>> verifiedProperty(UUID id, boolean verified) {
        Property property = this.getPropertyById(id);
        if (property.getDeleted() != null) {
            throw new InvalidOperationException("Property '" + property.getName() + "' is already deleted.");
        }
        boolean changed = verified != property.isVerified();
        if(changed){
            if(!verified){
                property.setStatus(StatusProperty.DRAFT);
                property.setPublished(false);
                property.setPublishedAt(null);
            }
            property.setVerified(verified);
            propertyRepository.save(property);
        }
        return changeResponse(propertyMapper.toResponse(property), changed);
    }

    @Override
    public Optional<ChangeStatusResponse<PropertyDetailsResponse>> changeExtrainfo(UUID id,PropertyDetailsRequest request) {
        Property property = this.getPropertyById(id);
        ChangeStatusResponse<PropertyDetails> data = this.updateExtraInfo(property,request);
        if(data.getChanged()) {
            property.setDetails(data.getResponse());
            this.propertyRepository.save(property);
        }
        return Optional.ofNullable(ChangeStatusResponse.buildResponse(propertyDetailsMapper.toResponse(data.getResponse()), data.getChanged()));
    }

    @Override
    public Optional<ChangeStatusResponse<PropertyResponse>> assignFeatureToProperty(UUID id, Set<UUID> featureIds) {
        Property property = this.getPropertyById(id);
        ChangeStatusResponse<Property> propertyChange=this.addUpdateFeature(property,featureIds);
        if(propertyChange.getChanged()) {
            propertyRepository.save(property);
        }
        return Optional.ofNullable(ChangeStatusResponse.buildResponse(propertyMapper.toResponse(propertyChange.getResponse()), propertyChange.getChanged()));
    }

    private ChangeStatusResponse<Property> addUpdateFeature(Property property, Set<UUID> featureIds) {
        if (property.getDeleted() != null) {
            throw new InvalidOperationException("Property '" + property.getName() + "' is already deleted.");
        }
        if (featureIds.size() > 10) {
            throw new InvalidOperationException("Too many features in property '" + property.getName() + "'.");
        }
        Set<PropertyFeatureLink> links = property.getLinks();
        if (links == null) {
            links = new HashSet<>();
            property.setLinks(links);
        }
        Map<UUID, PropertyFeatureLink> existingLinks = new HashMap<>();
        for (PropertyFeatureLink link : links) {
            existingLinks.put(link.getFeature().getId(), link);
        }
        boolean changed = false;
        for (UUID featureId : featureIds) {
            PropertyFeatures feature = featuresRepository.findById(featureId).orElse(null);
            if (feature == null) continue;
            if (feature.getDeleted() != null) continue;
            PropertyFeatureLink existing = existingLinks.get(featureId);
            if (existing != null){
                linkRepository.delete(existing);
                changed = true;
            }else{
                PropertyFeatureLink newLink = PropertyFeatureLink.builder()
                        .id(new PropertyFeatureLinkId(property.getId(), featureId))
                        .property(property)
                        .feature(feature)
                        .deletedAt(null)
                        .build();
                linkRepository.save(newLink);
                changed = true;
            }
        }
        return ChangeStatusResponse.buildResponse(property, changed);
    }




    private Property getPropertyById(UUID id) {
        return propertyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Property id not found"));
    }

    private String checkSlug(String name) {
        String slug =stringHelper.toSlug(name);
        List<Property> list = this.propertyRepository.findAllBySlugStartingWith(slug);
        if (!list.isEmpty()) {
            slug = stringHelper.generateUniqueSlug(slug, list.size());
        }
        return slug;
    }

    private List<Property> getFiltered(String search, Sort sort, boolean deleted) {
        return propertyRepository.findAll(
                GlobalSpecification.<Property>nameContains(stringHelper.normalize(search))
                        .and(GlobalSpecification.<Property>isDeleted(deleted)),
                sort
        );
    }

    private PagesResponse<PropertyResponse> getPagedResponse(String search, Sort sort, int page, int size, boolean deleted) {
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Property> types = propertyRepository.findAll(
                GlobalSpecification.<Property>nameContains(stringHelper.normalize(search))
                        .and(GlobalSpecification.<Property>isDeleted(deleted)),
                pageable
        );
        return propertyMapper.toPages(types, propertyMapper::toResponse);
    }

    private void checkName(String name){
        return ;
    }
    private void checkReserved(Property property){
        return;
    }
    private ChangeStatusResponse<PropertyDetails> updateExtraInfo(Property property, PropertyDetailsRequest request) {
        if (property.getDetails() == null) {
            PropertyDetails details = this.propertyDetailsMapper.toEntity(request);
            details.setProperty(property);
            property.setDetails(details);
            return ChangeStatusResponse.buildResponse(details, true);
        }
        boolean statusChanged = false;
        PropertyDetails details = property.getDetails();
        details.setProperty(property);
        if (request.bathRooms() != null && !request.bathRooms().equals(details.getBathRooms())) {
            statusChanged = true;
            details.setBathRooms(request.bathRooms());
        }
        if (request.bedRooms() != null && !request.bedRooms().equals(details.getBedRooms())) {
            statusChanged = true;
            details.setBedRooms(request.bedRooms());
        }
        if (request.singleBedRooms() != null && !request.singleBedRooms().equals(details.getSingleBedRooms())) {
            statusChanged = true;
            details.setSingleBedRooms(request.singleBedRooms());
        }
        if (request.doubleBedRooms() != null && !request.doubleBedRooms().equals(details.getDoubleBedRooms())) {
            statusChanged = true;
            details.setDoubleBedRooms(request.doubleBedRooms());
        }
        if (request.kitchens() != null && !request.kitchens().equals(details.getKitchens())) {
            statusChanged = true;
            details.setKitchens(request.kitchens());
        }
        if (request.rooms() != null && !request.rooms().equals(details.getRooms())) {
            statusChanged = true;
            details.setRooms(request.rooms());
        }
        if (request.maxPersonnes() != null && !request.maxPersonnes().equals(details.getMaxPersonnes())) {
            statusChanged = true;
            details.setMaxPersonnes(request.maxPersonnes());
        }
        return ChangeStatusResponse.buildResponse(details, statusChanged);
    }

    private PropertyType checkPropertyType(UUID id) {
        PropertyType type = propertyTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Property type not found"));
        if(type.getDeleted()!=null){
            throw new InvalidOperationException("Property type has been deleted");
        }
        return type;
    }

}
