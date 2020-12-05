package com.zvv.client.gui;

import com.zvv.client.core.NettyClient;
import com.zvv.client.gui.elements.FileTreeItem;
import core.files.FileTree;
import core.messsages.request.NewFolderRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.stage.Stage;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ModalDlgController {
    @Setter
    private TreeItem<FileTreeItem> fileTreeItem;

    @Setter
    private MainController mainController;

    @FXML
    TextField inpNewFolder;

    public void saveAction(ActionEvent actionEvent) {
        String newFolderName = inpNewFolder.getText();
        FileTree fileTree = new FileTree(newFolderName, true, null);
        FileTree parentFileTree = new FileTree(fileTreeItem.getValue().getName(), true, null);
        parentFileTree.addChild(fileTree);
        fileTree = parentFileTree;
        while (fileTreeItem.getParent() != null) {
            fileTreeItem = fileTreeItem.getParent();
            parentFileTree = new FileTree(fileTreeItem.getValue().getName(), true, null);
            parentFileTree.addChild(fileTree);
            fileTree = parentFileTree;
        }

        NewFolderRequest newFolderRequest = new NewFolderRequest(fileTree,mainController.getUser());
        mainController.getNettyClient().send(newFolderRequest);
        ((Stage) inpNewFolder.getScene().getWindow()).close();
    }

    public void cancelAction(ActionEvent actionEvent) {
    }
}
