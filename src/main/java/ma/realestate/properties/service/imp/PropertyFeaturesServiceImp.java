package ma.realestate.properties.service.imp;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import ma.realestate.properties.common.PagesResponse;
import ma.realestate.properties.dto.request.PropertyFeaturesRequest;
import ma.realestate.properties.dto.response.ChangeStatusResponse;
import ma.realestate.properties.dto.response.PropertyFeaturesResponse;
import ma.realestate.properties.dto.util.SortInput;
import ma.realestate.properties.entity.PropertyFeatureLink;
import ma.realestate.properties.entity.PropertyFeatures;
import ma.realestate.properties.exception.InvalidOperationException;
import ma.realestate.properties.helper.StringHelper;
import ma.realestate.properties.mapper.imp.PropertyFeaturesMapper;
import ma.realestate.properties.repository.PropertyFeatureLinkRepository;
import ma.realestate.properties.repository.PropertyFeaturesRepository;
import ma.realestate.properties.service.PropertyFeaturesService;
import ma.realestate.properties.specification.GlobalSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PropertyFeaturesServiceImp implements PropertyFeaturesService {
    private final PropertyFeaturesRepository repository;
    private final PropertyFeatureLinkRepository linkRepository;
    private final PropertyFeaturesMapper mapper;
    private final StringHelper stringHelper;

    @Override
    public Optional<PropertyFeaturesResponse> add(PropertyFeaturesRequest request) {
        PropertyFeatures features = mapper.toEntity(request);
        this.repository.findByName(features.getName()).ifPresent(feature -> {
            throw new InvalidOperationException("Property features already exist");
        });
        repository.save(features);
        return Optional.ofNullable(mapper.toResponse(features));
    }

    @Override
    public Optional<PropertyFeaturesResponse> remove(UUID id) {
        PropertyFeatures features = this.getPropertyFeature(id);
        if (features.getDeleted() != null) {
            throw new InvalidOperationException("Features '" + features.getName() + "' is already deleted.");
        }
        Set<PropertyFeatureLink> data = features.getLinks();
        if (data != null && !data.isEmpty()) {
            if (features.isAutoRestore()) {
                for (PropertyFeatureLink link : data) {
                    link.setDeletedAt(LocalDateTime.now());
                }
                linkRepository.saveAll(data);
            } else {
                for (PropertyFeatureLink link : data) {
                    link.setProperty(null);
                    link.setFeature(null);
                }
                linkRepository.deleteAll(data);
            }
        }
        features.setDeleted(LocalDateTime.now());
        return Optional.ofNullable(mapper.toResponse(repository.save(features)));
    }


    @Override
    public Optional<PropertyFeaturesResponse> getById(UUID id) {
        PropertyFeatures features = this.getPropertyFeature(id);
        if (features.getDeleted() != null) {
            throw new InvalidOperationException("Features '" + features.getName() + "' is already deleted.");
        }
        return Optional.ofNullable(mapper.toResponse(features));
    }

    @Override
    public Optional<ChangeStatusResponse<PropertyFeaturesResponse>> update(UUID id, PropertyFeaturesRequest request) {
        boolean changed = false;
        PropertyFeatures features = this.getPropertyFeature(id);
        if (features.getDeleted() != null) {
            throw new InvalidOperationException("Features '" + features.getName() + "' is already deleted.");
        }
        if(request.name() != null && !request.name().equals(features.getName())) {
            features.setName(request.name().trim());
            changed = true;
        }
        if (request.description() != null && !request.description().equals(features.getDescription())) {
            features.setDescription(request.description().trim());
            changed = true;
        }
        if(request.autoRestore()!=null && request.autoRestore()!=features.isAutoRestore()) {
            features.setAutoRestore(request.autoRestore());
            changed = true;
        }
        if (changed) {
            repository.save(features);
        }
        return Optional.ofNullable(ChangeStatusResponse.buildResponse(mapper.toResponse(features),changed));
    }

    @Override
    public List<PropertyFeaturesResponse> getAll(String search, SortInput sort) {
        return this.getFeatures(search,toSort(sort),false).stream().map(mapper::toResponse).toList();
    }

    @Override
    public PagesResponse<PropertyFeaturesResponse> paginate(String search, SortInput sort, int page, int size) {
        return this.getPageFeatures(search, toSort(sort), page, size, false);
    }

    @Override
    public PagesResponse<PropertyFeaturesResponse> trash(String search, SortInput sort, int page, int size) {
        return this.getPageFeatures(search, toSort(sort), page, size, true);
    }

    @Override
    public Optional<PropertyFeaturesResponse> recovory(UUID id) {
        PropertyFeatures features = this.getPropertyFeature(id);
        if (features.getDeleted() != null) {
            throw new InvalidOperationException("Features '" + features.getName() + "' is not deleted.");
        }
        Set<PropertyFeatureLink> data = features.getLinks();
        if (data != null && !data.isEmpty()) {
            if (features.isAutoRestore()) {
                for (PropertyFeatureLink link : data) {
                    link.setDeletedAt(null);
                }
                linkRepository.saveAll(data);
            }
        }
        features.setDeleted(null);
        return Optional.ofNullable(mapper.toResponse(repository.save(features)));
    }

    @Override
    public Optional<PropertyFeaturesResponse> destroy(UUID id) {
        PropertyFeatures features = this.getPropertyFeature(id);
        if (features.getDeleted() == null) {
            throw new InvalidOperationException("Features '" + features.getName() + "' is not deleted.");
        }
        Set<PropertyFeatureLink> data = features.getLinks();
        if (data != null && !data.isEmpty()) {
            for (PropertyFeatureLink link : data) {
                link.setProperty(null);
                link.setFeature(null);
            }
            linkRepository.deleteAll(data);
        }
        repository.delete(features);
        return Optional.ofNullable(mapper.toResponse(features));
    }

    private PropertyFeatures getPropertyFeature(UUID id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(" Features "+id+" not found"));
    }

    private PagesResponse<PropertyFeaturesResponse> getPageFeatures(String search, Sort sort, int page, int size, boolean deleted) {
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<PropertyFeatures> ftr=repository.findAll(
                GlobalSpecification.<PropertyFeatures>nameContains(stringHelper.normalize(search))
                        .and(GlobalSpecification.<PropertyFeatures>isDeleted(deleted)),
                pageable
        );
        return mapper.toPages(ftr,mapper::toResponse);
    }
    private List<PropertyFeatures> getFeatures(String search, Sort sort,boolean deleted) {
        return repository.findAll(
                GlobalSpecification.<PropertyFeatures>nameContains(stringHelper.normalize(search))
                        .and(GlobalSpecification.<PropertyFeatures>isDeleted(deleted)),
                sort
        );
    }
}
