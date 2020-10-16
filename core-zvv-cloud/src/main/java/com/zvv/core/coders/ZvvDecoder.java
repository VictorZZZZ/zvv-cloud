package com.zvv.core.coders;

import com.zvv.core.auth.User;
import com.zvv.core.messsages.MessageTypes;
import com.zvv.core.messsages.response.AuthResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.log4j.Log4j2;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Log4j2
public class ZvvDecoder {

    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        byte messageType = byteBuf.readByte();
        log.info("Получено сообщение в первом байте лежит {}", messageType);
        String requestName = (String) MessageTypes.TYPES.get(messageType);
        if (requestName != null) {
            byte var7 = -1;
            switch(requestName) {
                case("authRequest"):
                    User user = new User();
                    int loginLength = byteBuf.readInt();
                    user.setLogin(byteBuf.readCharSequence(loginLength, StandardCharsets.UTF_8).toString());
                    int pwdLength = byteBuf.readInt();
                    user.setPwd(byteBuf.readCharSequence(pwdLength, StandardCharsets.UTF_8).toString());
                    list.add(user);
                    break;
                case("authResponse"):
                    int usernameLength = byteBuf.readInt();
                    String username = byteBuf.readCharSequence(usernameLength, StandardCharsets.UTF_8).toString();
                    boolean isAuthenticated = byteBuf.readBoolean();
                    AuthResponse authResponse = new AuthResponse(username, isAuthenticated);
                    list.add(authResponse);
                    break;
                default:
                    log.error("Неизвестный тип запроса!");
            }
        } else {
            log.error("Нет такого запроса в Протоколе.");
        }

    }
}
