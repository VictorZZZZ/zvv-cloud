package com.zvv.client.gui.elements;

import com.zvv.client.core.NettyClient;
import com.zvv.client.gui.MainController;
import core.files.FileTree;
import core.messsages.request.FileRequest;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.stage.FileChooser;

import java.io.File;

public class ClientContextMenu extends ContextMenu {
    private MenuItem saveItem;
    private NettyClient nettyClient;
    private MainController mainController;

    public ClientContextMenu(TreeCell<FileTreeItem> cell,MainController mainController, NettyClient nettyClient) {
        createContextMenu(cell);
        this.nettyClient = nettyClient;
        this.mainController = mainController;
    }

    private void createContextMenu(TreeCell<FileTreeItem> cell) {
        saveItem = new MenuItem("Сохранить на диск");
        saveItem.setOnAction(saveFileAction(cell));
        this.getItems().add(saveItem);
    }

    private EventHandler<ActionEvent> saveFileAction(TreeCell<FileTreeItem> cell){
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TreeItem<FileTreeItem> fileTreeItem = (FileTreeItem) cell.getTreeItem();
                if(fileTreeItem.getValue().isDir()){
                    mainController.updateStatus("Данная версия не поддерживает пакетное скачивание. Попробуйте скачать файлы по отдельности!");
                    return;
                }
                FileChooser fileChooser = new FileChooser();//Класс работы с диалогом выборки и сохранения
                fileChooser.setTitle("Укажите имя нового файла для сохранения");//Заголовок диалога
//                FileChooser.ExtensionFilter extFilter =
//                        new FileChooser.ExtensionFilter("HTML files (*.html)", "*.html");//Расширение
//                fileChooser.getExtensionFilters().add(extFilter);
                File file = fileChooser.showSaveDialog(((Node) cell).getScene().getWindow());//Указываем текущую сцену CodeNote.mainStage
                if (file != null) {
                    //Save
                    FileTree fileTree = fileTreeItem.getValue().getFileTree();
                    FileRequest fileRequest = new FileRequest(fileTree);
                    nettyClient.send(fileRequest);
                }
            }
        };
    }
}
