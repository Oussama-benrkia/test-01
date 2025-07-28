package ma.realestate.properties.service;

import ma.realestate.properties.dto.request.PropertyFeaturesRequest;
import ma.realestate.properties.dto.response.PropertyFeaturesResponse;

public interface PropertyFeaturesService extends Service<PropertyFeaturesResponse,PropertyFeaturesRequest>,ServiceSoftDelete<PropertyFeaturesResponse> {
}
