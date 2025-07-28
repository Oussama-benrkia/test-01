package ma.realestate.properties.mapper.imp;

import lombok.RequiredArgsConstructor;
import ma.realestate.properties.dto.request.PropertyTypeRequest;
import ma.realestate.properties.dto.response.PropertyTypeResponse;
import ma.realestate.properties.entity.PropertyType;
import ma.realestate.properties.mapper.Mapper;
import org.springframework.stereotype.Component;

import javax.swing.text.DateFormatter;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class PropertyTypeMapper implements Mapper<PropertyTypeRequest,PropertyTypeResponse, PropertyType> {
    static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss");
    @Override
    public PropertyTypeResponse toResponse(PropertyType entity) {
        return PropertyTypeResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .createdAt(entity.getCreated().format(formatter))
                .deleteAt(entity.getDeleted()!=null?entity.getDeleted().format(formatter):"")
                .build();
    }

    @Override
    public PropertyType toEntity(PropertyTypeRequest request) {
        return PropertyType.builder()
                .name(request.name().trim())
                .build();
    }
}
