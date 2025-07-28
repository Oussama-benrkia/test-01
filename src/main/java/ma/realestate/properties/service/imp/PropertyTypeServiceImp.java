package ma.realestate.properties.service.imp;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ma.realestate.properties.common.PagesResponse;
import ma.realestate.properties.dto.request.PropertyDetailsRequest;
import ma.realestate.properties.dto.request.PropertyTypeRequest;
import ma.realestate.properties.dto.response.ChangeStatusResponse;
import ma.realestate.properties.dto.response.PropertyTypeResponse;
import ma.realestate.properties.dto.util.SortInput;
import ma.realestate.properties.entity.Property;
import ma.realestate.properties.entity.PropertyDetails;
import ma.realestate.properties.entity.PropertyType;
import ma.realestate.properties.exception.InvalidOperationException;
import ma.realestate.properties.helper.StringHelper;
import ma.realestate.properties.mapper.imp.PropertyTypeMapper;
import ma.realestate.properties.repository.PropertyTypeRepository;
import ma.realestate.properties.service.PropertyTypeService;
import ma.realestate.properties.specification.GlobalSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@RequiredArgsConstructor
@Log4j2
@Service
public class PropertyTypeServiceImp implements PropertyTypeService {

    private final PropertyTypeRepository repository;
    private final PropertyTypeMapper mapper;
    private final StringHelper stringHelper;

    @Override
    public Optional<PropertyTypeResponse> add(PropertyTypeRequest request) {
        this.repository.findByName(request.name()).ifPresent(existing -> {
            throw new EntityNotFoundException("Property type '" + existing.getName() + "' already exists.");
        });
        PropertyType propertyType = PropertyType.builder()
                .name(request.name())
                .build();
        return Optional.of(mapper.toResponse(repository.save(propertyType)));
    }

    @Override
    public Optional<PropertyTypeResponse> destroy(UUID id) {
        PropertyType propertyType = getPropertyTypeById(id);
        if (propertyType.getDeleted() == null) {
            throw new InvalidOperationException("Property type '" + propertyType.getName() + "' is not deleted.");
        }
        repository.delete(propertyType);
        return Optional.of(mapper.toResponse(propertyType));
    }

    @Override
    public Optional<PropertyTypeResponse> getById(UUID id) {
        var pro = this.getPropertyTypeById(id);
        if(pro.getDeleted()!=null) {
            throw new InvalidOperationException("Property type '" + pro.getName() + "' has been deleted.");
        }
        return Optional.ofNullable(mapper.toResponse(pro));
    }

    @Override
    @Transactional
    public Optional<ChangeStatusResponse<PropertyTypeResponse>> update(UUID id, PropertyTypeRequest request) {
        PropertyType propertyType = getPropertyTypeById(id);
        boolean changed = false;
        if (request.name() != null && !request.name().isBlank() && !request.name().equals(propertyType.getName())) {
            changed = true;
            propertyType.setName(request.name());
        }
        return changeResponse(mapper.toResponse(repository.save(propertyType)), changed);
    }

    @Override
    public List<PropertyTypeResponse> getAll(String search, SortInput sort) {
        return getFilteredTypes(search, toSort(sort), false)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public PagesResponse<PropertyTypeResponse> paginate(String search, SortInput sort,int page, int size) {
        return getPagedResponse(search, toSort(sort), page, size, false);
    }


    @Override
    public PagesResponse<PropertyTypeResponse> trash(String search, SortInput sort, int page, int size) {
        return getPagedResponse(search, toSort(sort), page, size, true);
    }

    @Override
    public Optional<PropertyTypeResponse> recovory(UUID id) {
        PropertyType propertyType = getPropertyTypeById(id);
        if(propertyType.getDeleted() == null) {
            throw new InvalidOperationException("Property type '" + propertyType.getName() + "' is not deleted.");
        }
        propertyType.setDeleted(null);
        return Optional.ofNullable(mapper.toResponse(repository.save(propertyType)));
    }

    @Override
    @Transactional
    public Optional<PropertyTypeResponse> remove(UUID id) {
        PropertyType propertyType = getPropertyTypeById(id);
        if(propertyType.getDeleted() != null) {
            throw new InvalidOperationException("Property type '" + propertyType.getName() + "' is already deleted.");
        }
        propertyType.setDeleted(LocalDateTime.now());
        return Optional.ofNullable(mapper.toResponse(repository.save(propertyType)));
    }


    private PropertyType getPropertyTypeById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Property Type with ID " + id + " does not exist."));
    }

    private List<PropertyType> getFilteredTypes(String search, Sort sort, boolean deleted) {
        return repository.findAll(
                GlobalSpecification.<PropertyType>nameContains(stringHelper.normalize(search))
                        .and(GlobalSpecification.<PropertyType>isDeleted(deleted)),
                sort
        );
    }
    private PagesResponse<PropertyTypeResponse> getPagedResponse(String search, Sort sort, int page, int size, boolean deleted) {
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<PropertyType> types = repository.findAll(
                GlobalSpecification.<PropertyType>nameContains(stringHelper.normalize(search))
                        .and(GlobalSpecification.<PropertyType>isDeleted(deleted)),
                pageable
        );
        return mapper.toPages(types, mapper::toResponse);
    }
}