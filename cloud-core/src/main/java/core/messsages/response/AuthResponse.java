package core.messsages.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.messsages.AbstractMessage;
import core.messsages.Serializer;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

@Getter
@Setter
public class AuthResponse extends AbstractMessage {
    private String userName;
    private boolean authenticated;

    public AuthResponse() {
        this.setMessageType((byte)2);
    }

    public AuthResponse(String username, boolean authenticateUser) {
        this.setMessageType((byte)2);
        this.authenticated = authenticateUser;
        this.userName = username;
    }

    public static void main(String[] args) throws JsonProcessingException {
        AuthResponse authResponse = new AuthResponse("Login",true);
        System.out.println(Arrays.toString(Serializer.serialize(authResponse)));

       AuthResponse authResponse1 = Serializer.deserialize("{\"messageType\":2,\"userName\":\"ss\",\"authenticated\":false}",AuthResponse.class);
//        list.add(authResponse);
    }
}
