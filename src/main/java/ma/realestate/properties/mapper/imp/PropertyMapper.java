package ma.realestate.properties.mapper.imp;

import lombok.RequiredArgsConstructor;
import ma.realestate.properties.dto.request.PropertyRequest;
import ma.realestate.properties.dto.response.PropertyFeaturesResponse;
import ma.realestate.properties.dto.response.PropertyResponse;
import ma.realestate.properties.entity.Property;
import ma.realestate.properties.entity.PropertyFeatureLink;
import ma.realestate.properties.entity.PropertyFeatures;
import ma.realestate.properties.mapper.Mapper;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PropertyMapper implements Mapper<PropertyRequest, PropertyResponse, Property> {
    private final PropertyTypeMapper typeMapper;
    private final PropertyDetailsMapper detailsMapper;
    private final PropertyFeaturesMapper featuresMapper;
    static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss");
    @Override
    public PropertyResponse toResponse(Property entity) {
        Set<PropertyFeatureLink> safeLinks = entity.getLinks() != null ? entity.getLinks() : Collections.emptySet();
        System.out.println(safeLinks.size());
        Set<PropertyFeaturesResponse> features = safeLinks.stream()
                .map(PropertyFeatureLink::getFeature)
                .filter(feature -> feature != null)
                .map(featuresMapper::toResponse)
                .collect(Collectors.toSet());

        return PropertyResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .slug(entity.getSlug())
                .description(entity.getDescription())
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .address(entity.getAddress())
                .city(entity.getCity())
                .status(entity.getStatus())
                .isVerified(entity.isVerified())
                .isPublished(entity.isPublished())
                .type(typeMapper.toResponse(entity.getType()))
                .extra(detailsMapper.toResponse(entity.getDetails()))
                .features(features)
                .createAt(entity.getCreated().format(formatter))
                .deleteAt(entity.getDeleted() != null ? entity.getDeleted().format(formatter) : "")
                .publishedAt(entity.getPublishedAt() != null ? entity.getPublishedAt().format(formatter) : "")
                .build();
    }


    @Override
    public Property toEntity(PropertyRequest request) {
        return Property.builder()
                .name(request.name().trim())
                .description(request.description().trim())
                .latitude(request.latitude())
                .longitude(request.longitude())
                .address(request.address().trim())
                .city(request.city().trim().toLowerCase())
                .build();
    }
}
