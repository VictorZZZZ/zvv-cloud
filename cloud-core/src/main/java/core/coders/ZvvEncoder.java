package core.coders;

import core.auth.User;
import core.messsages.AbstractMessage;
import core.messsages.MessageTypes;
import core.messsages.request.AuthRequest;
import core.messsages.response.AuthResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.log4j.Log4j2;

import java.nio.charset.StandardCharsets;

@Log4j2
public class ZvvEncoder extends MessageToByteEncoder<AbstractMessage> {


    protected void encode(ChannelHandlerContext channelHandlerContext, AbstractMessage abstractMessage, ByteBuf byteBuf) throws Exception {
        log.info("получено на обработку");
        byte messageType = abstractMessage.getMessageType();
        String requestType = MessageTypes.TYPES.get(messageType);
        log.info("reqType {}",requestType);
        switch(requestType) {
            case ("authRequest"):
                if (abstractMessage instanceof AuthRequest) {
                    AuthRequest authRequest = (AuthRequest)abstractMessage;
                    byteBuf.writeByte(authRequest.getMessageType());
                    User user = authRequest.getUser();
                    byteBuf.writeInt(user.getLogin().getBytes().length);
                    byteBuf.writeCharSequence(user.getLogin(), StandardCharsets.UTF_8);
                    byteBuf.writeInt(user.getPwd().getBytes().length);
                    byteBuf.writeCharSequence(user.getPwd(), StandardCharsets.UTF_8);
                }
                break;
            case ("authResponse"):
                    if (abstractMessage instanceof AuthResponse) {
                    AuthResponse authResponse = (AuthResponse)abstractMessage;
                    byteBuf.writeByte(authResponse.getMessageType());
                    byteBuf.writeInt(authResponse.getUsername().getBytes().length);
                    byteBuf.writeCharSequence(authResponse.getUsername(), StandardCharsets.UTF_8);
                    byteBuf.writeBoolean(authResponse.isAuthenticated);
                }
                break;
            default:
                byteBuf.writeByte(0);
        }
    }
}
