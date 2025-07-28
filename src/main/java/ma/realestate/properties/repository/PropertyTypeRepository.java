package ma.realestate.properties.repository;

import ma.realestate.properties.entity.PropertyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PropertyTypeRepository extends JpaRepository<PropertyType, UUID>, JpaSpecificationExecutor<PropertyType> {
    Optional<PropertyType> findByName(String name);
}

