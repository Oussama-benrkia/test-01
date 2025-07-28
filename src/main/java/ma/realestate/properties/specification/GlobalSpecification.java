package ma.realestate.properties.specification;

import org.springframework.data.jpa.domain.Specification;

public class GlobalSpecification {
    public static <E> Specification<E> isDeleted(boolean deleted) {
        return (root, query, builder) -> deleted
                ? builder.isNotNull(root.get("deleted"))
                : builder.isNull(root.get("deleted"));
    }

    public static <E> Specification<E> nameContains(String keyword) {
        return (root, query, builder) ->
                keyword == null || keyword.isEmpty()
                        ? builder.conjunction()
                        : builder.like(builder.lower(root.get("name")), "%" + keyword.toLowerCase() + "%");
    }
}
