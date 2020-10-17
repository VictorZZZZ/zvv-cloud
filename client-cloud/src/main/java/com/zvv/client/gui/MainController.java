package com.zvv.client.gui;

import com.zvv.client.core.NettyClient;
import com.zvv.client.gui.elements.FileTreeItem;
import core.auth.User;
import core.files.FileTree;
import core.messsages.request.FileTreeRequest;
import core.messsages.response.FileTreeResponse;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class MainController {
    @Setter
    private NettyClient nettyClient;
    @Setter
    private User user;
    @FXML
    Label lblStatus;
    @FXML
    TreeView<FileTreeItem> fileTreeView;
    @FXML
    Button btnRefresh;

    public void refreshTreeAction(ActionEvent actionEvent) {
        nettyClient.send(new FileTreeRequest(user));
    }

    public void refreshTree(FileTreeResponse fileTreeResponse) {
        FileTree firstElement = fileTreeResponse.getFileTree();
        FileTreeItem newRootDir = new FileTreeItem(firstElement.getName(),firstElement.getType());
        FileTreeItem treeItemRoot = new FileTreeItem(newRootDir,newRootDir.getName());

        recursiveBuild(treeItemRoot, firstElement);

        fileTreeView.setRoot(treeItemRoot);
        fileTreeView.refresh();
    }

    private void recursiveBuild(FileTreeItem dir, FileTree fileTree) {
        for(FileTree fTelement : fileTree.getChildren()){
            if(fTelement.getType().equals(FileTree.DIR_TYPE)){
                FileTreeItem newDir = new FileTreeItem(new FileTreeItem(fTelement.getName(),fTelement.getType()),fTelement.getName());
                dir.getChildren().add(newDir);
                recursiveBuild(newDir,fTelement);
            } else {
                FileTreeItem newFile = new FileTreeItem(new FileTreeItem(fTelement.getName(),fTelement.getType()),fTelement.getName());
                dir.getChildren().add(newFile);
            }
        }
    }


}

