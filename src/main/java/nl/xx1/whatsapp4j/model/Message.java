package nl.xx1.whatsapp4j.model;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import nl.xx1.whatsapp4j.Client;

@NoArgsConstructor(force = true)
@AllArgsConstructor
@ToString
@Getter
public class Message {

    @Expose
    private final String id;

    @Expose
    private final String body;

    @Expose
    private final boolean isNewMessage;

    protected final Client client;
}
