package ma.realestate.properties.controller;

import lombok.RequiredArgsConstructor;
import ma.realestate.properties.common.PagesResponse;
import ma.realestate.properties.dto.request.PropertyFeaturesRequest;
import ma.realestate.properties.dto.response.ChangeStatusResponse;
import ma.realestate.properties.dto.response.PropertyFeaturesResponse;
import ma.realestate.properties.dto.response.PropertyResponse;
import ma.realestate.properties.dto.util.SortInput;
import ma.realestate.properties.service.PropertyFeaturesService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class PropertyFeaturesController {
    private final PropertyFeaturesService service;
    @QueryMapping
    public List<PropertyFeaturesResponse> getAllPropertyFeatures(
            @Argument String search,
            @Argument SortInput sort
    ){
        return this.service.getAll(search, sort);
    }
    @QueryMapping
    public PagesResponse<PropertyFeaturesResponse> getPagePropertyFeatures(
            @Argument String search,
            @Argument SortInput sort,
            @Argument int page,
            @Argument int limit,
            @Argument boolean deleted
    ){
        if(deleted){
            return this.service.trash(search, sort, page, limit);
        }
        return this.service.paginate(search,sort,page,limit);
    }

    @MutationMapping
    public PropertyFeaturesResponse createPropertyFeatures(
            @Argument PropertyFeaturesRequest request
    ){
        return this.service.add(request).orElseThrow(()->new RuntimeException("Error creating property features"));
    }
    @MutationMapping
    public ChangeStatusResponse<PropertyFeaturesResponse> updatePropertyFeatures(
            @Argument UUID id,
            @Argument PropertyFeaturesRequest request
    ){
        return this.service.update(id,request).orElseThrow(()->new RuntimeException("Error updating property features"));
    }
    @MutationMapping
    public PropertyFeaturesResponse deletePropertyFeatures(
            @Argument UUID id
    ){
        return this.service.remove(id).orElseThrow(()->new RuntimeException("Error deleting property features"));
    }
    @MutationMapping
    public PropertyFeaturesResponse destroyPropertyFeatures(
            @Argument UUID id
    ){
        return this.service.destroy(id).orElseThrow(()->new RuntimeException("Error destroying property features"));
    }
    @MutationMapping
    public PropertyFeaturesResponse recoverPropertyFeatures(
            @Argument UUID id
    ){
        return this.service.recovory(id).orElseThrow(()->new RuntimeException("Error recovering property features"));
    }
}
