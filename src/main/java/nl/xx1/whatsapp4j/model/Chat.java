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
public abstract class Chat {
    @Expose
    private final String id;

    @Expose
    private final String name;

    @Expose
    private final boolean isGroup;

    @Expose
    private final boolean isMuted;

    @Expose
    private final boolean isReadOnly;

    @Expose
    private final int unreadCount;

    @Expose
    private final boolean pinned;

    @Expose
    private final Long timestamp;

    protected final Client client;
}
