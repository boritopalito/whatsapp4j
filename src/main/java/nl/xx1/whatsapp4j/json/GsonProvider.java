package nl.xx1.whatsapp4j.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nl.xx1.whatsapp4j.Client;
import nl.xx1.whatsapp4j.model.Chat;
import nl.xx1.whatsapp4j.model.GroupChat;
import nl.xx1.whatsapp4j.model.PrivateChat;

public class GsonProvider {
    public static Gson getGson(Client client) {
        RuntimeTypeAdapterFactory<Chat> chatAdapterFactory = RuntimeTypeAdapterFactory.of(Chat.class, "type")
                .registerSubtype(GroupChat.class, "group")
                .registerSubtype(PrivateChat.class, "private");

        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .registerTypeAdapterFactory(chatAdapterFactory)
                .registerTypeAdapterFactory(new ClientFieldAdapterFactory(client))
                .create();
    }
}
