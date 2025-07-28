package ma.realestate.properties.runner;

import lombok.RequiredArgsConstructor;
import ma.realestate.properties.entity.Property;
import ma.realestate.properties.entity.PropertyType;
import ma.realestate.properties.entity.enumu.StatusProperty;
import ma.realestate.properties.repository.PropertyRepository;
import ma.realestate.properties.repository.PropertyTypeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AppDBRuner implements CommandLineRunner {

    private final PropertyTypeRepository repository;
    private final PropertyRepository propertyRepository;

    @Override
    public void run(String... args) {
        createDefaultPropertyTypes();
        createFakeProperties();
    }

    private void createDefaultPropertyTypes() {
        List<String> defaultTypes = List.of(
                "appartement",
                "maisons",
                "villas",
                "riad",
                "locaux commerciaux",
                "bureaux",
                "terrains"
        );
        for (String name : defaultTypes) {
            repository.findByName(name).orElseGet(() -> {
                PropertyType newType = PropertyType.builder().name(name).build();
                return repository.save(newType);
            });
        }
    }
    private void createFakeProperties() {
        if (propertyRepository.count() > 0) return;

        List<PropertyType> types = repository.findAll();
        if (types.isEmpty()) return;

        for (int i = 1; i <= 10; i++) {
            String name = "Bien Immobilier " + i;
            String slug = generateSlug(name);

            Property property = Property.builder()
                    .name(name)
                    .slug(slug)
                    .description("Description fictive pour " + name)
                    .latitude(33.5 + i * 0.01)
                    .longitude(-7.6 + i * 0.01)
                    .publishedAt(i % 2 == 0 ? LocalDateTime.now() : null)
                    .address("Adresse " + i + ", Ville Test")
                    .city("Ville" + i)
                    .status(i % 2 == 0 ? StatusProperty.PUBLISHED : StatusProperty.DRAFT)
                    .isVerified(i % 3 == 0)
                    .isPublished(i % 2 == 0)
                    .type(types.get(i % types.size()))
                    .build();

            propertyRepository.save(property);
        }
    }

    private String generateSlug(String input) {
        return input
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "")
                + "-" + UUID.randomUUID().toString().substring(0, 8);
    }
}
