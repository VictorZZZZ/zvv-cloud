package core.messsages.response;

import core.messsages.AbstractMessage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse extends AbstractMessage {
    private String username;
    public boolean isAuthenticated;

    public AuthResponse(String username, boolean authenticateUser) {
        this.setMessageType((byte)2);
        this.isAuthenticated = authenticateUser;
        this.username = username;
    }
}
