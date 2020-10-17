package core.messsages;

import lombok.NoArgsConstructor;

import java.util.HashMap;

@NoArgsConstructor
public class MessageTypes {
    public static final HashMap<Byte, String> TYPES = new HashMap<Byte, String>() {
        {
            this.put((byte)1, "authRequest");
            this.put((byte)2, "authResponse");
            this.put((byte)3, "fileTreeRequest");
            this.put((byte)4, "fileTreeResponse");
        }
    };
}
