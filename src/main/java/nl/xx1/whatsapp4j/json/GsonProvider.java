package nl.xx1.whatsapp4j.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nl.xx1.whatsapp4j.model.Chat;
import nl.xx1.whatsapp4j.model.GroupChat;
import nl.xx1.whatsapp4j.model.PrivateChat;

public class GsonProvider {
    private static final Gson GSON;

    static {
        RuntimeTypeAdapterFactory<Chat> chatAdapterFactory = RuntimeTypeAdapterFactory.of(Chat.class, "type")
                .registerSubtype(GroupChat.class, "group")
                .registerSubtype(PrivateChat.class, "private");

        GSON = new GsonBuilder().registerTypeAdapterFactory(chatAdapterFactory).create();
    }

    public static Gson getGson() {
        return GSON;
    }
}
