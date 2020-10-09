package com.zvv.client.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    private static final String APPLICATION_NAME = "ZVV CLOUD";
    private static final String AUTEHENTICATION_FAILED = "Логин или пароль введены неверно.";
    private static final String MAIN_VIEW = "/mainView.xml";

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
        if(!login.isEmpty() || !password.isEmpty()){
                ((Parent) actionEvent.getSource()).getScene().getWindow().hide();
                showMainView();
        } else {
            lblError.setText(AUTEHENTICATION_FAILED);
        }
    }

    private void showMainView() throws IOException {
        Parent mainView = FXMLLoader.load(getClass().getResource(MAIN_VIEW));
        Stage mainStage = new Stage();
        mainStage.setScene(new Scene(mainView, 600, 418));
        mainStage.setTitle(APPLICATION_NAME);
        mainStage.show();
    }
}
