package ma.realestate.properties.controller;

import lombok.RequiredArgsConstructor;
import ma.realestate.properties.common.PagesResponse;
import ma.realestate.properties.dto.request.PropertyDetailsRequest;
import ma.realestate.properties.dto.request.PropertyRequest;
import ma.realestate.properties.dto.response.ChangeStatusResponse;
import ma.realestate.properties.dto.response.PropertyDetailsResponse;
import ma.realestate.properties.dto.response.PropertyResponse;
import ma.realestate.properties.dto.util.SortInput;
import ma.realestate.properties.entity.enumu.StatusProperty;
import ma.realestate.properties.service.PropertyService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class PropertyController {
    private final PropertyService propertyService;

    @MutationMapping
    public PropertyResponse createProperty(@Argument PropertyRequest request) {
        return propertyService.add(request).orElseThrow(()->new RuntimeException("Error creating property"));
    }
    @MutationMapping
    public ChangeStatusResponse<PropertyResponse> updateProperty(@Argument UUID id, @Argument PropertyRequest request) {
        return propertyService.update(id,request).orElseThrow(()->new RuntimeException("Error updating property"));
    }
    @MutationMapping
    public PropertyResponse deleteProperty(@Argument UUID id) {
        return propertyService.remove(id).orElseThrow(()->new RuntimeException("Error deleting property"));
    }
    @MutationMapping
    public PropertyResponse destroyProperty(@Argument UUID id) {
        return propertyService.destroy(id).orElseThrow(()->new RuntimeException("Error destroying property"));
    }
    @MutationMapping
    public PropertyResponse recoveryProperty(@Argument UUID id) {
        return propertyService.recovory(id).orElseThrow(()->new RuntimeException("Error recovering property"));
    }
    @MutationMapping
    public ChangeStatusResponse<PropertyResponse> updateStatusProperty(@Argument UUID id, @Argument StatusProperty status) {
        return propertyService.changeStatus(id, status).orElseThrow(()->new RuntimeException("Error updating status property"));
    }
    @MutationMapping
    public ChangeStatusResponse<PropertyResponse> verifiedProperty(@Argument UUID id, @Argument Boolean verified) {
        return propertyService.verifiedProperty(id,verified).orElseThrow(()->new RuntimeException("Error verifiing property"));
    }
    @MutationMapping
    public ChangeStatusResponse<PropertyDetailsResponse> updateExtraInfoProperty(@Argument UUID id, @Argument PropertyDetailsRequest request) {
        return propertyService.changeExtrainfo(id,request).orElseThrow(()->new RuntimeException("Error update"));
    }
    @MutationMapping
    public ChangeStatusResponse<PropertyResponse> addPropertyFeaturesToProperty(@Argument UUID id, @Argument Set<UUID> data) {
        return propertyService.assignFeatureToProperty(id,data).orElseThrow(()->new RuntimeException("Error update"));
    }



    @QueryMapping
    public PagesResponse<PropertyResponse> getPageProperty(
            @Argument String search,
            @Argument SortInput sort,
            @Argument int page,
            @Argument int limit,
            @Argument boolean deleted
    ){
        if(deleted) {
            return propertyService.trash(search,sort,page,limit);
        }
        return  propertyService.paginate(search,sort,page,limit);
    }
    @QueryMapping
    public List<PropertyResponse> getAllProperty(
            @Argument String search,
            @Argument SortInput sort
    ) {
        return this.propertyService.getAll(search, sort);
    }
    @QueryMapping
    public PropertyResponse getPropertyBySlug(@Argument String slug) {
        return propertyService.getBySlug(slug).orElseThrow(()->new RuntimeException("Error retrieving property"));
    }
    @QueryMapping
    public PropertyResponse getPropertyById(@Argument UUID id) {
        return propertyService.getById(id).orElseThrow(()->new RuntimeException("Error retrieving property"));
    }
}
