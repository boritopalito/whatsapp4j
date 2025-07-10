package nl.xx1.whatsapp4j;

@FunctionalInterface
public interface ClientEventListener<T> {
    void onEvent(T args);
}
