package nl.xx1.whatsapp4j.json;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;
import nl.xx1.whatsapp4j.Client;

public class ClientFieldAdapterFactory implements TypeAdapterFactory {

    private final Client client;

    public ClientFieldAdapterFactory(Client client) {
        this.client = client;
    }

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, typeToken);
        final Class<?> rawType = typeToken.getRawType();

        Optional<Field> optionalClientField = Arrays.stream(rawType.getDeclaredFields())
                .filter(field ->
                        field.getType() == client.getClass() && field.getName().equals("client"))
                .findFirst();

        if (optionalClientField.isEmpty()) {
            return delegate;
        }

        Field field = optionalClientField.get();
        field.setAccessible(true);

        return new TypeAdapter<T>() {
            @Override
            public void write(JsonWriter jsonWriter, T t) throws IOException {
                delegate.write(jsonWriter, t);
            }

            @Override
            public T read(JsonReader jsonReader) throws IOException {
                T read = delegate.read(jsonReader);

                try {
                    if (read != null) {
                        field.set(read, client);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Unable to inject client.");
                }
                return read;
            }
        };
    }
}
