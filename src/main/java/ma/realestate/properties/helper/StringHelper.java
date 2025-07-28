package ma.realestate.properties.helper;

import org.springframework.stereotype.Component;

import java.text.Normalizer;

@Component
public class StringHelper {
    public  String normalize(String search) {
        return search != null ? search.trim() : "";
    }
    public String generateUniqueSlug(String slug, int count){
        count ++;
        return slug+"-"+count;
    }
    public String toSlug(String name){
        String normalized= Normalizer.normalize(name , Normalizer.Form.NFD);
        return normalized
                .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "")
                .toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-+|-+$", "");

    }
}
