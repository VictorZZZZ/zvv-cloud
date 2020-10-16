package com.zvv.core.messsages;

import lombok.NoArgsConstructor;

import java.util.HashMap;

@NoArgsConstructor
public class MessageTypes {
    public static final HashMap<Byte, String> TYPES = new HashMap<Byte, String>() {
        {
            this.put((byte)1, "authRequest");
            this.put((byte)2, "authResponse");
        }
    };
}
