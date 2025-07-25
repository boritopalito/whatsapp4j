package nl.xx1.whatsapp4j.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString(callSuper = true)
@AllArgsConstructor
public class GroupChat extends Chat {
    @Expose
    @SerializedName("groupMetadata")
    private GroupMetaData groupMetaData;
}
