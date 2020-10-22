package com.zvv.client.core;

import com.zvv.client.gui.LoginController;
import com.zvv.client.gui.MainController;
import core.messsages.response.AuthResponse;
import core.messsages.response.FileResponse;
import core.messsages.response.FileTreeResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import javafx.application.Platform;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.IOException;

@Log4j2
public class ClientInputHandler extends ChannelInboundHandlerAdapter {
    private LoginController loginController;
    private MainController mainController;

    public ClientInputHandler(LoginController loginController, MainController mainController) {
        this.loginController = loginController;
        this.mainController = mainController;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("Успешное подключение к серверу.");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof AuthResponse){
            AuthResponse authResponse = (AuthResponse) msg;
            log.info("Auth response {}",authResponse.isAuthenticated());
            if(authResponse.isAuthenticated()) {
                //показываем mainView
                showMainView(authResponse);
            } else {
                //показываем Error
                showErrorAuth();

            }
        } else if(msg instanceof FileTreeResponse){
            log.warn("Получен fileTreeResponse");
            FileTreeResponse fileTreeResponse = (FileTreeResponse) msg;
            updateFileTree(fileTreeResponse);
        } else if(msg instanceof FileResponse){
            log.warn("Получен fileResponse");
            FileResponse fileResponse = (FileResponse) msg;
            File fileToSave = mainController.getFileToSave();
            fileResponse.setPath(fileToSave.toPath());
            fileResponse.write();
            if(fileResponse.getLeftToRead()<1){
                if(fileResponse.isMd5Ok()) updateStatus("Файл получен. Контрольная сумма совпала.");
                mainController.setFileToSave(null);
            }
            //Показать процесс загрузки
        } else {
            log.warn("Неизвестный объект");
        }
    }

    private void updateStatus(String newStatus){
        Platform.runLater(() -> {
                    try {
                        Thread.sleep(100);
                        mainController.updateStatus(newStatus);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    private void showErrorAuth() {
        Platform.runLater(() -> {
                    try {
                        Thread.sleep(100);
                        loginController.showAuthError();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    private void showMainView(AuthResponse authResponse) {
        Platform.runLater(() -> {
                    try {
                        Thread.sleep(100);
                        loginController.showMainView(authResponse.getUserName());
                    } catch (InterruptedException | IOException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    private void updateFileTree(FileTreeResponse fileTreeResponse){
        Platform.runLater(() -> {
                    try {
                        Thread.sleep(100);
                        mainController.refreshTree(fileTreeResponse);
                        mainController.updateStatus("Синхронизация дерева файлов выполнена.");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        );
    }
}
