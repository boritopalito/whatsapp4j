package nl.xx1.whatsapp4j.webcache;

import java.util.Optional;

public interface WebCache {
    Optional<String> resolve(String version);

    void persist(String html, String version);
}
