package ma.realestate.properties.dto.request;

import jakarta.validation.constraints.NotBlank;
import ma.realestate.properties.validation.OnCreate;

public record PropertyFeaturesRequest (
        @NotBlank(groups = OnCreate.class)
        String name,
        @NotBlank(groups = OnCreate.class)
        String description,
        Boolean autoRestore
){
}
