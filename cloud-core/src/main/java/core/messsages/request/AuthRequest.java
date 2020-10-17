package core.messsages.request;

import core.auth.User;
import core.messsages.AbstractMessage;
import lombok.Getter;

@Getter
public class AuthRequest extends AbstractMessage {
    private User user;

    public AuthRequest(User user) {
        this.user = user;
        this.setMessageType((byte)1);
    }
}
