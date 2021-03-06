package com.zvv.client.gui;

import com.zvv.client.core.NettyClient;
import core.auth.User;
import core.messsages.request.AuthRequest;
import core.messsages.request.FileTreeRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.IOException;

public class LoginController {
    private static final String APPLICATION_NAME = "ZVV CLOUD";
    private static final String AUTEHENTICATION_FAILED = "Логин или пароль введены неверно.";
    private static User user;
    @Setter
    private MainController mainController;
    @Setter
    private Parent mainView;
    @Setter
    private NettyClient nettyClient;

    @FXML
    TextField inpLogin;

    @FXML
    PasswordField inpPassword;

    @FXML
    Button btnLogin;

    @FXML
    Label lblError;

    public void btnLoginAction(ActionEvent actionEvent) throws IOException {
        lblError.setText("");
        String login = inpLogin.getText();
        String password = inpPassword.getText();
        User user = new User(login,password);
        AuthRequest authRequest = new AuthRequest(user);
        nettyClient.send(authRequest);
    }

    public void showMainView(String username) throws IOException {
        Stage stage =(Stage) btnLogin.getScene().getWindow();
        stage.setScene(new Scene(mainView, 600, 418));
        stage.setTitle(APPLICATION_NAME + " for User - " + username);
        user = new User(username,"");
        mainController.setUser(user);
        nettyClient.send(new FileTreeRequest(user));
        stage.show();
    }

    public void showAuthError() {
        lblError.setText(AUTEHENTICATION_FAILED);
    }

    public void signUpAction(MouseEvent mouseEvent) {
        lblError.setText("Регистрация пока недоступна");
    }
}
