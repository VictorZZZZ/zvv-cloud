package core.messsages.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import core.auth.User;
import core.messsages.AbstractMessage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class FileTreeRequest extends AbstractMessage {
    private byte messageType=(byte) 3;
    @JsonProperty
    private User user;

    public FileTreeRequest(User user){
        this.setMessageType((byte)3);
        this.user = user;
    }


}
