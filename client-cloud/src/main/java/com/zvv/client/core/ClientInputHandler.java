package com.zvv.client.core;

import com.zvv.client.gui.LoginController;
import com.zvv.client.gui.MainController;
import core.messsages.response.AuthResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import javafx.application.Platform;
import lombok.extern.log4j.Log4j2;

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
            log.info("Auth response {}",authResponse.isAuthenticated);
            if(authResponse.isAuthenticated) {
                //показываем mainView
                Platform.runLater(() -> {
                            try {
                                Thread.sleep(100);
                                loginController.showMainView(authResponse.getUsername());
                            } catch (InterruptedException | IOException e) {
                                e.printStackTrace();
                            }
                        }
                );
            } else {
                //показываем Error
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
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
