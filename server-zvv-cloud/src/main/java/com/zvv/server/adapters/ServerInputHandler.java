package com.zvv.server.adapters;

import com.zvv.database.DatabaseCore;
import core.auth.User;
import core.files.FileTree;
import core.messsages.Serializer;
import core.messsages.request.AuthRequest;
import core.messsages.request.FileRequest;
import core.messsages.request.FileTreeRequest;
import core.messsages.response.AuthResponse;
import core.messsages.response.FileTreeResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

@Log4j2
public class ServerInputHandler extends ChannelInboundHandlerAdapter {
    private static final String SERVER_STORAGE = "server_folder/storage";

    public void handlerAdded(ChannelHandlerContext ctx) {
        log.info("Client connected from {}", ctx.channel().remoteAddress());
    }

    public void handlerRemoved(ChannelHandlerContext ctx) {
        log.info("Client disconnect - {}", ctx.channel().remoteAddress());
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws IOException {
        if (msg instanceof AuthRequest) {
            AuthRequest authRequest = (AuthRequest) msg;
            User user = authRequest.getUser();
            AuthResponse authResponse = new AuthResponse(user.getLogin(), DatabaseCore.authenticateUser(user));
            ctx.writeAndFlush(authResponse);
        } else if (msg instanceof FileTreeRequest) {
            log.info("Получен fileTreeRequest");
            FileTreeRequest fileTreeRequest = (FileTreeRequest) msg;
            FileTree fileTree = getFileTree(fileTreeRequest.getUser());
            FileTreeResponse fileTreeResponse = new FileTreeResponse(fileTree);
            ctx.writeAndFlush(fileTreeResponse);
        } else if (msg instanceof FileRequest) {
            log.info("Получен fileRequest");
            FileRequest fileRequest = (FileRequest) msg;
            FileTree fileTree = fileRequest.getReversedFileTree();
            log.info("Здесь будет отправка файла {}",fileTree.toString());
        } else {
            log.info("Неизвестный объект");
        }
    }

    private FileTree getFileTree(User user) throws IOException {
        Path root = Paths.get(SERVER_STORAGE,user.getLogin());
        FileTree fileTree = new FileTree(user.getLogin(),true,null);
        buildTree(root,fileTree);
        FileTreeResponse fileTreeResponse = new FileTreeResponse(fileTree);
        return fileTree;
    }

    private static void buildTree(Path path, FileTree fileTree) throws IOException {
        Files.walk(path,1).skip(1).forEach(p -> {
            if(Files.isDirectory(p)) {
                FileTree newDir = new FileTree(p.getFileName().toString(), true, fileTree);
                fileTree.addChild(newDir);
                try {
                    buildTree(p,newDir);//рекурсивно
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                fileTree.addChild(new FileTree(p.getFileName().toString(), false, fileTree));
            }
        });
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
