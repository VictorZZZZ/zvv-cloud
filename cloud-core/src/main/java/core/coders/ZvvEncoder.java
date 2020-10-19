package core.coders;

import core.auth.User;
import core.files.FileTree;
import core.messsages.AbstractMessage;
import core.messsages.MessageTypes;
import core.messsages.Serializer;
import core.messsages.request.AuthRequest;
import core.messsages.request.FileRequest;
import core.messsages.request.FileTreeRequest;
import core.messsages.response.AuthResponse;
import core.messsages.response.FileResponse;
import core.messsages.response.FileTreeResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.log4j.Log4j2;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Log4j2
public class ZvvEncoder extends MessageToByteEncoder<AbstractMessage> {


    protected void encode(ChannelHandlerContext channelHandlerContext, AbstractMessage abstractMessage, ByteBuf byteBuf) throws Exception {
        byte messageType = abstractMessage.getMessageType();
        String requestType = MessageTypes.TYPES.get(messageType);
        log.info("Получен запрос на обработку {}",requestType);
        switch(requestType) {
            case ("authRequest"):
                if (abstractMessage instanceof AuthRequest) {
                    AuthRequest authRequest = (AuthRequest) abstractMessage;
                    byteBuf.writeByte(authRequest.getMessageType());
                    Object[] objArr = Serializer.serialize(authRequest);
                    sendObjArray(byteBuf, objArr);
                }
                break;
            case ("authResponse"):
                if (abstractMessage instanceof AuthResponse) {
                    AuthResponse authResponse = (AuthResponse) abstractMessage;
                    byteBuf.writeByte(authResponse.getMessageType());
                    Object[] objArr = Serializer.serialize(authResponse);
                    sendObjArray(byteBuf, objArr);
                }
                break;
            case ("fileTreeRequest"):
                if(abstractMessage instanceof FileTreeRequest){
                    FileTreeRequest fileTreeRequest = (FileTreeRequest) abstractMessage;
                    byteBuf.writeByte(fileTreeRequest.getMessageType());
                    Object[] objArr = Serializer.serialize(fileTreeRequest);
                    sendObjArray(byteBuf, objArr);
                }
                break;
            case ("fileTreeResponse"):
                if(abstractMessage instanceof FileTreeResponse){
                    FileTreeResponse fileTreeResponse = (FileTreeResponse) abstractMessage;
                    byteBuf.writeByte(fileTreeResponse.getMessageType());
                    Object[] objArr = Serializer.serialize(fileTreeResponse);
                    sendObjArray(byteBuf, objArr);
                }
                break;
            case ("fileRequest"):
                if(abstractMessage instanceof FileRequest){
                    FileRequest fileRequest = (FileRequest) abstractMessage;
                    byteBuf.writeByte(fileRequest.getMessageType());
                    Object[] objArr = Serializer.serialize(fileRequest);
                    sendObjArray(byteBuf, objArr);
                }
                break;
            case ("fileResponse"):
                if(abstractMessage instanceof FileResponse){
                    log.info("Тут будет отправка файла");
                }
                break;
            default:
                byteBuf.writeByte(0);
        }
    }

    private void sendObjArray(ByteBuf byteBuf, Object[] objArr) {
        Arrays.stream(objArr).forEach(obj -> {
            if (obj instanceof Integer) {
                byteBuf.writeInt((Integer) obj);
            } else {
                byteBuf.writeCharSequence((String) obj, StandardCharsets.UTF_8);
            }
        });
    }
}
