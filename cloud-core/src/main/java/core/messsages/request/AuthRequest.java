package core.messsages.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.auth.User;
import core.messsages.AbstractMessage;
import core.messsages.Serializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;

@Getter
@Setter
@NoArgsConstructor
public class AuthRequest extends AbstractMessage {
    private byte messageType=(byte) 1;
    private User user;

    public AuthRequest(User user) {
        this.user = user;
    }

    public static void main(String[] args) throws JsonProcessingException {
        AuthRequest authRequest = new AuthRequest(new User("login","password"));
        System.out.println(Arrays.toString(Serializer.serialize(authRequest)));
        authRequest = Serializer.deserialize("{\"messageType\":1,\"user\":{\"login\":\"login\",\"pwd\":\"password\"}}",AuthRequest.class);
        System.out.println(authRequest);
    }
}
