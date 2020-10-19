package com.zvv.client.gui;

import com.zvv.client.core.NettyClient;
import com.zvv.client.gui.elements.ClientContextMenu;
import com.zvv.client.gui.elements.FileTreeItem;
import core.auth.User;
import core.files.FileTree;
import core.messsages.request.FileTreeRequest;
import core.messsages.response.FileTreeResponse;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.text.SimpleDateFormat;
import java.util.Date;

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

    @FXML
    public void initialize() {
        MainController mainController = this;
        fileTreeView.setCellFactory(new Callback<TreeView<FileTreeItem>, TreeCell<FileTreeItem>>() {
            @Override
            public TreeCell<FileTreeItem> call(TreeView<FileTreeItem> param) {
                TreeCell<FileTreeItem> cell = new TreeCell<FileTreeItem>() {
                    @Override
                    protected void updateItem(FileTreeItem fileTreeItem, boolean empty) {
                        super.updateItem(fileTreeItem, empty);
                        if (empty) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(fileTreeItem.toString());
                            setGraphic(fileTreeItem.getIcon());
                        }
                    }
                };
                cell.setContextMenu(new ClientContextMenu(cell, mainController, nettyClient));
                return cell;
            }
        });
    }

    public void refreshTreeAction(ActionEvent actionEvent) {
        nettyClient.send(new FileTreeRequest(user));
    }

    public void refreshTree(FileTreeResponse fileTreeResponse) {
        FileTree firstElement = fileTreeResponse.getFileTree();
        FileTreeItem newRootDir = new FileTreeItem(firstElement.getName(), firstElement.isDir());
        FileTreeItem treeItemRoot = new FileTreeItem(newRootDir, newRootDir.getName());

        recursiveBuild(treeItemRoot, firstElement);

        fileTreeView.setRoot(treeItemRoot);
        fileTreeView.refresh();
    }

    private void recursiveBuild(FileTreeItem dir, FileTree fileTree) {
        for (FileTree fTelement : fileTree.getChildren()) {
            if (fTelement.isDir()) {
                FileTreeItem newDir = new FileTreeItem(new FileTreeItem(fTelement.getName(), fTelement.isDir()), fTelement.getName());
                dir.getChildren().add(newDir);
                recursiveBuild(newDir, fTelement);
            } else {
                FileTreeItem newFile = new FileTreeItem(new FileTreeItem(fTelement.getName(), fTelement.isDir()), fTelement.getName());
                dir.getChildren().add(newFile);
            }
        }
    }

    public void updateStatus(String newStatus) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
        String time = dateFormat.format(new Date(System.currentTimeMillis()));
        lblStatus.setText(time + " : " + newStatus);
    }

}

