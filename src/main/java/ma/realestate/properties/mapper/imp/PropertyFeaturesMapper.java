package ma.realestate.properties.mapper.imp;

import ma.realestate.properties.dto.request.PropertyFeaturesRequest;
import ma.realestate.properties.dto.response.PropertyFeaturesResponse;
import ma.realestate.properties.entity.PropertyFeatures;
import ma.realestate.properties.mapper.Mapper;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
@Component
public class PropertyFeaturesMapper implements Mapper<PropertyFeaturesRequest, PropertyFeaturesResponse, PropertyFeatures> {
    static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss");
    @Override
    public PropertyFeaturesResponse toResponse(PropertyFeatures entity) {
        return PropertyFeaturesResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .createdAt(entity.getCreated().format(formatter))
                .autoRestore(entity.isAutoRestore())
                .build();
    }

    @Override
    public PropertyFeatures toEntity(PropertyFeaturesRequest request) {
        return PropertyFeatures.builder()
                .name(request.name().trim())
                .description(request.description().trim())
                .autoRestore(request.autoRestore()!=null?request.autoRestore():false)
                .build();
    }
}
