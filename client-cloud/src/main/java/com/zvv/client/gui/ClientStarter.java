package com.zvv.client.gui;

import com.zvv.client.core.NettyClient;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientStarter extends Application {
    private static final String LOGIN_VIEW = "/loginView.fxml";
    private static final String MAIN_VIEW = "/mainView.fxml";
    private Parent loginView;
    private Parent mainView;
    private LoginController loginController;
    private MainController mainController;
    private NettyClient nettyClient;

    @Override
    public void init() throws Exception {
        FXMLLoader loginViewLoader = new FXMLLoader(getClass().getResource(LOGIN_VIEW));
        loginView = loginViewLoader.load();
        loginController = loginViewLoader.<LoginController>getController();
        FXMLLoader mainViewLoader = new FXMLLoader(getClass().getResource(MAIN_VIEW));
        mainView = mainViewLoader.load();
        mainController = mainViewLoader.<MainController>getController();
        nettyClient = new NettyClient(loginController, mainController);
        loginController.setNettyClient(nettyClient);
        loginController.setMainView(mainView);
        loginController.setMainController(mainController);
        mainController.setNettyClient(nettyClient);

        Thread t = new Thread(nettyClient);
        t.setDaemon(true);
        t.setName("Netty Client");
        t.start();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("ZVV Cloud Client");
        primaryStage.setScene(new Scene(loginView, 300, 300));
        primaryStage.setOnCloseRequest(event -> {
            nettyClient.stop();
        });
        primaryStage.show();
    }

}
