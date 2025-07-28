package ma.realestate.properties.dto.request;

public record PropertyDetailsRequest(
        Integer bathRooms,
        Integer bedRooms,
        Boolean singleBedRooms,
        Boolean doubleBedRooms,
        Integer kitchens,
        Integer rooms,
        Integer maxPersonnes
) {
}
