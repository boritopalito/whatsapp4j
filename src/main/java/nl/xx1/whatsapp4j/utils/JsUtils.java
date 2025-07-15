package nl.xx1.whatsapp4j.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class JsUtils {
    /**
     * Loads the JS from a file in the resource directory
     *
     * @param resourcePath The path of the JS file
     * @return String with the contents of the file
     * @throws RuntimeException When the file cant be loaded.
     */
    public static String loadJsFromResources(String resourcePath) throws RuntimeException {
        ClassLoader classLoader = JsUtils.class.getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Resource not found: " + resourcePath);
            }
            try (BufferedReader reader =
                    new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
