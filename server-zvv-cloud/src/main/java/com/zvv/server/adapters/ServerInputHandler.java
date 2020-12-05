package com.zvv.server.adapters;

import com.zvv.database.DatabaseCore;
import core.auth.User;
import core.files.FileTree;
import core.messsages.request.*;
import core.messsages.response.AuthResponse;
import core.messsages.response.FileResponse;
import core.messsages.response.FileTreeResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
            sendFileTree(getFileTree(fileTreeRequest.getUser()),ctx);
        } else if (msg instanceof FileRequest) {
            log.info("Получен fileRequest");
            FileRequest fileRequest = (FileRequest) msg;
            FileTree fileTree = fileRequest.getFileTree().getReversed();
            log.info("Отправка файла {}", fileTree.toString());
            Path path = Paths.get(SERVER_STORAGE, fileTree.toString());
            FileResponse fileResponse = new FileResponse(path);
            while (!fileResponse.isFileEnded()) {
                fileResponse.readNextPortion();
                ctx.writeAndFlush(fileResponse);
            }
        } else if (msg instanceof NewFolderRequest) {
            NewFolderRequest newFolderRequest = (NewFolderRequest) msg;
            FileTree fileTree = newFolderRequest.getFileTree();
            File newDir = new File(SERVER_STORAGE,fileTree.getReversed().toString());
            newDir.mkdir();
            log.info("Создана папка {}",newDir.getAbsolutePath().toString());
            sendFileTree( getFileTree(newFolderRequest.getUser()), ctx);
        } else if (msg instanceof DeleteRequest) {
            DeleteRequest deleteRequest = (DeleteRequest) msg;
            FileTree fileTree = deleteRequest.getFileTree();
            File deleteFile = new File(SERVER_STORAGE,fileTree.getReversed().toString());
            deleteFile.delete();
            log.info("Удален файл {}",deleteFile.getAbsolutePath().toString());
            sendFileTree( getFileTree(deleteRequest.getUser()), ctx);
        } else {
            log.info("Неизвестный объект");
        }
    }

    /**
     * Генерирует FileTree для папки user
     *
     * @param user
     * @return FileTree
     * @throws IOException
     */
    private FileTree getFileTree(User user) throws IOException {
        Path root = Paths.get(SERVER_STORAGE, user.getLogin());
        FileTree fileTree = new FileTree(user.getLogin(), true, null);
        buildTree(root, fileTree);
        FileTreeResponse fileTreeResponse = new FileTreeResponse(fileTree);
        return fileTree;
    }

    private void sendFileTree(FileTree fileTree, ChannelHandlerContext ctx) {
        FileTreeResponse fileTreeResponse = new FileTreeResponse(fileTree);
        ctx.writeAndFlush(fileTreeResponse);
    }

    private static void buildTree(Path path, FileTree fileTree) throws IOException {
        Files.walk(path, 1).skip(1).forEach(p -> {
            if (Files.isDirectory(p)) {
                FileTree newDir = new FileTree(p.getFileName().toString(), true, fileTree);
                fileTree.addChild(newDir);
                try {
                    buildTree(p, newDir);//рекурсивно
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
