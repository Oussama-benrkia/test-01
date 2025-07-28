package ma.realestate.properties.service;

import ma.realestate.properties.dto.request.PropertyTypeRequest;
import ma.realestate.properties.dto.response.PropertyTypeResponse;

public interface PropertyTypeService extends Service<PropertyTypeResponse, PropertyTypeRequest>,ServiceSoftDelete<PropertyTypeResponse> {
}
