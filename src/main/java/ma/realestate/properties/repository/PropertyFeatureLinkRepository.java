package ma.realestate.properties.repository;

import ma.realestate.properties.entity.PropertyFeatureLink;
import ma.realestate.properties.entity.PropertyFeatureLinkId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PropertyFeatureLinkRepository extends JpaRepository<PropertyFeatureLink, PropertyFeatureLinkId> {

}
