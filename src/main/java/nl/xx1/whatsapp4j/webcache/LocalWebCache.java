package nl.xx1.whatsapp4j.webcache;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;

public class LocalWebCache implements WebCache {
    private Path getPath() {
        return Path.of("w4j_cache");
    }

    @Override
    public Optional<String> resolve(String version) {
        String fileName = version + ".html";
        Path filePath = getPath().resolve(fileName);

        try {
            String content = Files.readString(filePath);
            return Optional.of(content);
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    @Override
    public void persist(String html, String version) {
        Path dirPath = getPath();

        try {
            Files.createDirectories(dirPath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create directory: " + dirPath, e);
        }

        Path filePath = dirPath.resolve(version + ".html");

        try {
            Files.writeString(filePath, html, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write path: " + filePath, e);
        }
    }
}
