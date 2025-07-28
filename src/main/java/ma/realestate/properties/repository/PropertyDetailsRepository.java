package ma.realestate.properties.repository;

import ma.realestate.properties.entity.PropertyDetails;
import ma.realestate.properties.entity.PropertyFeatureLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PropertyDetailsRepository extends JpaRepository<PropertyDetails, UUID>  {
}
