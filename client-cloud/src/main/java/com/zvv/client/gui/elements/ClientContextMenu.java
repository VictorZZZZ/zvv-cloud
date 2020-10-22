package com.zvv.client.gui.elements;

import com.zvv.client.core.NettyClient;
import com.zvv.client.gui.MainController;
import core.files.FileTree;
import core.messsages.request.DeleteRequest;
import core.messsages.request.FileRequest;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.stage.FileChooser;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.IOException;

@Log4j2
public class ClientContextMenu extends ContextMenu {
    private MenuItem saveItem;
    private MenuItem createDirItem;
    private MenuItem deleteItem;
    private NettyClient nettyClient;
    private MainController mainController;

    public ClientContextMenu(TreeCell<FileTreeItem> cell,MainController mainController, NettyClient nettyClient) {
        createContextMenu(cell);
        this.nettyClient = nettyClient;
        this.mainController = mainController;
    }

    private void createContextMenu(TreeCell<FileTreeItem> cell) {
            createDirItem = new MenuItem("Создать папку");
            createDirItem.setOnAction(createFolderAction(cell));
            this.getItems().add(createDirItem);

            saveItem = new MenuItem("Скачать");
            saveItem.setOnAction(saveFileAction(cell));
            this.getItems().add(saveItem);

            deleteItem = new MenuItem("Удалить");
            deleteItem.setOnAction(deleteFileAction(cell));
            this.getItems().add(deleteItem);
    }

    private EventHandler<ActionEvent> saveFileAction(TreeCell<FileTreeItem> cell){
        return (EventHandler<ActionEvent>) event -> {
            TreeItem<FileTreeItem> fileTreeItem = (FileTreeItem) cell.getTreeItem();
            mainController.saveFileDialog(fileTreeItem);
        };
    }

    private EventHandler<ActionEvent> createFolderAction(TreeCell<FileTreeItem> cell){
        return (EventHandler<ActionEvent>) event -> {
            TreeItem<FileTreeItem> fileTreeItem = (FileTreeItem) cell.getTreeItem();
            try {
                mainController.showModalDlg(fileTreeItem);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        };
    }

    private EventHandler<ActionEvent> deleteFileAction(TreeCell<FileTreeItem> cell){
        return (EventHandler<ActionEvent>) event -> {
            TreeItem<FileTreeItem> fileTreeItem = (FileTreeItem) cell.getTreeItem();
            FileTree fileTree = mainController.getFileTree(fileTreeItem);
            DeleteRequest deleteRequest = new DeleteRequest(fileTree,mainController.getUser());
            mainController.getNettyClient().send(deleteRequest);
        };
    }
}
