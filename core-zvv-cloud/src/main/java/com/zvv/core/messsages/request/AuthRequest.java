package com.zvv.core.messsages.request;

import com.zvv.core.auth.User;
import com.zvv.core.messsages.AbstractMessage;
import lombok.Getter;

@Getter
public class AuthRequest extends AbstractMessage {
    private User user;

    public AuthRequest(User user) {
        this.user = user;
        this.setMessageType((byte)1);
    }
}
