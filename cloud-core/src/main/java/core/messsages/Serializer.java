package core.messsages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.messsages.request.AuthRequest;

public class Serializer {

    public static Object[] serialize(AbstractMessage abstractMessage) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String message = mapper.writeValueAsString(abstractMessage);
        Integer size = message.getBytes().length;//Длинна строки сериализованного объекта
        Object[] msgArray = new Object[]{size, message};
        return msgArray;
    }

    public static <T> T deserialize(String str, Class<T> classType) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(str, classType);
    }
}
