package com.zvv.client.gui;

import com.zvv.client.core.NettyClient;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.Setter;

public class MainController {
    @Setter
    private NettyClient nettyClient;

    @FXML
    Label lblStatus;
}
