package com.zvv.server.adapters;

import com.zvv.database.DatabaseCore;
import core.auth.User;
import core.messsages.response.AuthResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ServerInputHandler extends ChannelInboundHandlerAdapter {

    public void handlerAdded(ChannelHandlerContext ctx) {
        log.info("Client connected from {}", ctx.channel().remoteAddress());
    }

    public void handlerRemoved(ChannelHandlerContext ctx) {
        log.info("Client disconnect - {}", ctx.channel().remoteAddress());
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof User) {
            User user = (User)msg;
            log.info(user.toString());
            AuthResponse authResponse = new AuthResponse(user.getLogin(), DatabaseCore.authenticateUser(user));
            ctx.writeAndFlush(authResponse);
        } else {
            log.info("неизвестный объект");
        }

    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
