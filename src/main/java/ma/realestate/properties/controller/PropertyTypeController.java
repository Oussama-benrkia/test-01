package ma.realestate.properties.controller;

import lombok.RequiredArgsConstructor;
import ma.realestate.properties.common.PagesResponse;
import ma.realestate.properties.dto.request.PropertyTypeRequest;
import ma.realestate.properties.dto.response.ChangeStatusResponse;
import ma.realestate.properties.dto.response.PropertyTypeResponse;
import ma.realestate.properties.dto.util.SortInput;
import ma.realestate.properties.service.PropertyTypeService;
import ma.realestate.properties.validation.OnCreate;
import ma.realestate.properties.validation.OnUpdate;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class PropertyTypeController {
    private final PropertyTypeService service;
    @QueryMapping
    public List<PropertyTypeResponse> getAllPropertyTypes(
            @Argument String search,
            @Argument SortInput sort
    ) {
        return this.service.getAll(search, sort);
    }

    @QueryMapping
    public PagesResponse<PropertyTypeResponse> getPagePropertyTypes(
            @Argument String search,
            @Argument SortInput sort,
            @Argument int page,
            @Argument int limit,
            @Argument boolean deleted
    ){
        if(deleted) {
            return this.service.trash(search,sort,page,limit);
        }
        return this.service.paginate(search,sort,page,limit);

    }

    @QueryMapping
    public PropertyTypeResponse getPropertyTypeById(
            @Argument String id
    ) {
        return this.service.getById(UUID.fromString(id)).orElseThrow(()-> new RuntimeException(String.format("Type %s not found", id)));
    }

    @MutationMapping
    public PropertyTypeResponse createPropertyType(
            @Argument @Validated(OnCreate.class) PropertyTypeRequest request
    ) {
        return this.service.add(request).orElseThrow(()-> new RuntimeException(String.format("Type Problme")));
    }

    @MutationMapping
    public ChangeStatusResponse<PropertyTypeResponse> updatePropertyType(
            @Argument UUID id,
            @Argument@Validated(OnUpdate.class) PropertyTypeRequest request
    ) {
        return this.service.update(id,request).orElseThrow(()-> new RuntimeException(String.format("Type Problme")));
    }

    @MutationMapping
    public PropertyTypeResponse deletePropertyType(
            @Argument UUID id
    ) {
        return this.service.remove(id).orElseThrow(()-> new RuntimeException(String.format("Type Problme")));
    }

    @MutationMapping
    public PropertyTypeResponse destroyPropertyType(
            @Argument UUID id
    ) {
        return this.service.destroy(id).orElseThrow(()-> new RuntimeException(String.format("Type Problme")));
    }

    @MutationMapping
    public PropertyTypeResponse recoveryPropertyType(
            @Argument UUID id
    ) {
        return this.service.recovory(id).orElseThrow(()-> new RuntimeException(String.format("Type Problme")));
    }


}
