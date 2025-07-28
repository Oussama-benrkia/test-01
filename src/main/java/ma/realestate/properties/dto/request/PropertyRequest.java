package ma.realestate.properties.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ma.realestate.properties.validation.OnCreate;

import java.util.Set;
import java.util.UUID;

public record PropertyRequest(
        @NotBlank(groups = OnCreate.class)
        String name,
        @NotBlank(groups = OnCreate.class)
        String description,
        @NotNull(groups = OnCreate.class)
        @Min(value = -90)
        @Max(value = 90)
        Double latitude,
        @NotNull(groups = OnCreate.class)
        @Min(value = -180)
        @Max(value = 180)
        Double longitude,
        @NotBlank(groups = OnCreate.class)
        String address,
        @NotBlank(groups = OnCreate.class)
        String city,
        @NotNull(groups = OnCreate.class)
        UUID type,
        PropertyDetailsRequest extra,
        Set<UUID> features
) {
}
