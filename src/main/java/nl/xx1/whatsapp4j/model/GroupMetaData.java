package nl.xx1.whatsapp4j.model;

import com.google.gson.annotations.SerializedName;
import java.util.Date;

public record GroupMetaData(@SerializedName("desc") String description, @SerializedName("creation") Long createdAt) {
    public GroupMetaData {
        if (description == null) {
            description = "";
        }
    }

    public Date createdAtDate() {
        return new Date(createdAt * 1000);
    }

    @Override
    public String toString() {
        return "GroupMetaData{" + "description='" + description + '\'' + ", createdAt=" + createdAtDate() + '}';
    }
}
