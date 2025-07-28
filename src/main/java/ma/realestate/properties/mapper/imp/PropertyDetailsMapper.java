package ma.realestate.properties.mapper.imp;

import ma.realestate.properties.dto.request.PropertyDetailsRequest;
import ma.realestate.properties.dto.response.PropertyDetailsResponse;
import ma.realestate.properties.entity.PropertyDetails;
import ma.realestate.properties.mapper.Mapper;
import org.springframework.stereotype.Component;

@Component
public class PropertyDetailsMapper implements Mapper<PropertyDetailsRequest, PropertyDetailsResponse, PropertyDetails> {
    @Override
    public PropertyDetailsResponse toResponse(PropertyDetails entity) {
        if (entity == null) return null;

        return PropertyDetailsResponse.builder()
                .bathRooms(entity.getBathRooms() != null ? entity.getBathRooms() : null)
                .bedRooms(entity.getBedRooms() != null ? entity.getBedRooms() : null)
                .rooms(entity.getRooms() != null ? entity.getRooms() : null)
                .doubleBedRooms(entity.getDoubleBedRooms() != null ? entity.getDoubleBedRooms() : null)
                .singleBedRooms(entity.getSingleBedRooms() != null ? entity.getSingleBedRooms() : null)
                .kitchens(entity.getKitchens() != null ? entity.getKitchens() : null)
                .maxPersonnes(entity.getMaxPersonnes() != null ? entity.getMaxPersonnes() : null)
                .build();
    }

    @Override
    public PropertyDetails toEntity(PropertyDetailsRequest request) {
        if(request == null) return null;
        return PropertyDetails.builder()
                .bathRooms(request.bathRooms()!=null?request.bathRooms():null)
                .bedRooms(request.bedRooms()!=null?request.bedRooms():null)
                .rooms(request.rooms()!=null?request.rooms():null)
                .doubleBedRooms(request.doubleBedRooms()!=null?request.doubleBedRooms():null)
                .singleBedRooms(request.singleBedRooms()!=null?request.singleBedRooms():null)
                .kitchens(request.kitchens()!=null?request.kitchens():null)
                .maxPersonnes(request.maxPersonnes()!=null?request.maxPersonnes():null)
                .build();
    }
}
