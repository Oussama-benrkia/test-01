package ma.realestate.properties.service;

import ma.realestate.properties.dto.request.PropertyDetailsRequest;
import ma.realestate.properties.dto.request.PropertyRequest;
import ma.realestate.properties.dto.response.ChangeStatusResponse;
import ma.realestate.properties.dto.response.PropertyDetailsResponse;
import ma.realestate.properties.dto.response.PropertyResponse;
import ma.realestate.properties.entity.enumu.StatusProperty;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface PropertyService extends Service<PropertyResponse, PropertyRequest> , ServiceSoftDelete<PropertyResponse> {
   Optional<PropertyResponse> getBySlug(String slug);
   Optional<ChangeStatusResponse<PropertyResponse>> changeStatus(UUID id , StatusProperty status);
   Optional<ChangeStatusResponse<PropertyResponse>> verifiedProperty(UUID id,boolean verified);
   Optional<ChangeStatusResponse<PropertyDetailsResponse>> changeExtrainfo(UUID id, PropertyDetailsRequest request);
   Optional<ChangeStatusResponse<PropertyResponse>> assignFeatureToProperty(UUID id, Set<UUID> data);
}
