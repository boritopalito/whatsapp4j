package nl.xx1.whatsapp4j.webcache;

public class WebCacheFactory {
    public static WebCache create(String type) {
        return switch (type) {
            case "local" -> new LocalWebCache();
            case "remote" -> new RemoteWebCache();
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }
}
