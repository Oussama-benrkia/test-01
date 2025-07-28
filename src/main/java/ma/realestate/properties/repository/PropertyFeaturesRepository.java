package ma.realestate.properties.repository;

import ma.realestate.properties.entity.PropertyFeatures;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PropertyFeaturesRepository extends JpaRepository<PropertyFeatures, UUID>, JpaSpecificationExecutor<PropertyFeatures> {
    Optional<PropertyFeatures> findByName(String name);
}
