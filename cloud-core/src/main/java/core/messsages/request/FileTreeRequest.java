package core.messsages.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import core.auth.User;
import core.messsages.AbstractMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class FileTreeRequest extends AbstractMessage {
    @Setter
    @Getter
    private User user;
    public FileTreeRequest() {
        this.setMessageType((byte)3);
    }
    public FileTreeRequest(User user){
        this.setMessageType((byte)3);
        this.user = user;
    }


}
