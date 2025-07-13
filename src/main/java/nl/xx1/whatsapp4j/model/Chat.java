package nl.xx1.whatsapp4j.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(force = true)
@AllArgsConstructor
@ToString
@Getter
public abstract class Chat {
    private final String id;
    private final String name;
    private final boolean isGroup;
    private final boolean isMuted;
    private final boolean isReadOnly;
    private final int unreadCount;
    private final boolean pinned;
    private final Long timestamp;
}
