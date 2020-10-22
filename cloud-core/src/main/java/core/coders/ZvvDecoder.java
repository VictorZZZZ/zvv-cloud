package core.coders;

import core.messsages.AbstractMessage;
import core.messsages.MessageTypes;
import core.messsages.Serializer;
import core.messsages.request.*;
import core.messsages.response.AuthResponse;
import core.messsages.response.FileResponse;
import core.messsages.response.FileTreeResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import lombok.extern.log4j.Log4j2;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Log4j2
public class ZvvDecoder extends ReplayingDecoder<AbstractMessage> {

    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        byte messageType = byteBuf.readByte();
        int strLength = byteBuf.readInt();
        String serStr = byteBuf.readCharSequence(strLength, StandardCharsets.UTF_8).toString();
        log.info("Получено сообщение в первом байте лежит {}", messageType);
        String requestName = MessageTypes.TYPES.get(messageType);
        if (requestName != null) {
            switch(requestName) {
                case("authRequest"):
                    AuthRequest authRequest = Serializer.deserialize(serStr,AuthRequest.class);
                    list.add(authRequest);
                    break;
                case("authResponse"):
                    AuthResponse authResponse = Serializer.deserialize(serStr,AuthResponse.class);
                    list.add(authResponse);
                    break;
                case("fileTreeRequest"):
                    FileTreeRequest fileTreeRequest = Serializer.deserialize(serStr,FileTreeRequest.class);
                    list.add(fileTreeRequest);
                    break;
                case("fileTreeResponse"):
                    FileTreeResponse fileTreeResponse = Serializer.deserialize(serStr,FileTreeResponse.class);
                    list.add(fileTreeResponse);
                    break;
                case("fileRequest"):
                    log.info(serStr);
                    FileRequest fileRequest = Serializer.deserialize(serStr,FileRequest.class);
                    list.add(fileRequest);
                    break;
                case("fileResponse"):
                    //log.info(serStr);
                    FileResponse fileResponse = Serializer.deserialize(serStr, FileResponse.class);
                    list.add(fileResponse);
                    break;
                case("newFolderRequest"):
                    NewFolderRequest newFolderRequest = Serializer.deserialize(serStr,NewFolderRequest.class);
                    list.add(newFolderRequest);
                    break;
                case("deleteRequest"):
                    DeleteRequest deleteRequest = Serializer.deserialize(serStr,DeleteRequest.class);
                    list.add(deleteRequest);
                    break;
                default:
                    log.error("Неизвестный тип запроса!");
            }
        } else {
            log.error("Нет такого запроса в Протоколе.");
        }

    }
}
