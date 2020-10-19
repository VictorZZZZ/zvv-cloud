package core.messsages.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.messsages.AbstractMessage;
import core.messsages.Serializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;

@Getter
@Setter
@NoArgsConstructor
public class AuthResponse extends AbstractMessage {
    private byte messageType=(byte) 2;
    private String userName;
    private boolean authenticated;

    public AuthResponse(String username, boolean authenticateUser) {
        this.authenticated = authenticateUser;
        this.userName = username;
    }

    public static void main(String[] args) throws JsonProcessingException {
        AuthResponse authResponse = new AuthResponse("Login",true);
        System.out.println(Arrays.toString(Serializer.serialize(authResponse)));

       //AuthResponse authResponse1 = Serializer.deserialize("{\"messageType\":2,\"userName\":\"ss\",\"authenticated\":false}",AuthResponse.class);
//        list.add(authResponse);
    }
}
