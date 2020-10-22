package com.zvv.client.gui;

import com.zvv.client.core.NettyClient;
import com.zvv.client.gui.elements.ClientContextMenu;
import com.zvv.client.gui.elements.FileTreeItem;
import core.auth.User;
import core.files.FileTree;
import core.messsages.request.FileRequest;
import core.messsages.request.FileTreeRequest;
import core.messsages.response.FileTreeResponse;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

@Log4j2
public class MainController {
    @Setter
    @Getter
    private NettyClient nettyClient;
    @Setter
    @Getter
    private User user;
    @FXML
    Label lblStatus;
    @FXML
    TreeView<FileTreeItem> fileTreeView;
    @FXML
    Button btnRefresh;

    @Getter
    @Setter
    File fileToSave;

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

        treeItemRoot.setExpanded(true);
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
            dir.getChildren().sort(Comparator.comparing(item -> !item.getValue().isDir()));
        }
    }

    public void updateStatus(String newStatus) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        String time = dateFormat.format(new Date(System.currentTimeMillis()));
        lblStatus.setText(time + " : " + newStatus);
    }

    public void uploadAction(ActionEvent actionEvent) {
        TreeItem<FileTreeItem> fileTreeItem = fileTreeView.getSelectionModel().getSelectedItem();
        loadFileDialog(fileTreeItem);
    }

    private void loadFileDialog(TreeItem<FileTreeItem> fileTreeItem) {
        if(!fileTreeItem.getValue().isDir()){
            fileTreeItem = fileTreeItem.getParent();
        }
        System.out.println(fileTreeItem.getValue().getName());
    }

    public void downloadAction(ActionEvent actionEvent) {
        TreeItem<FileTreeItem> fileTreeItem = fileTreeView.getSelectionModel().getSelectedItem();
        saveFileDialog(fileTreeItem);
    }

    public void saveFileDialog(TreeItem<FileTreeItem> fileTreeItem) {
        if(fileTreeItem.getValue().isDir()){
            updateStatus("Данная версия не поддерживает пакетное скачивание. Попробуйте скачать файлы по отдельности!");
            return;
        }
        FileChooser fileChooser = new FileChooser();//Класс работы с диалогом выборки и сохранения
        fileChooser.setTitle("Укажите имя нового файла для сохранения");//Заголовок диалога
        fileChooser.setInitialFileName(fileTreeItem.getValue().getName());

        File file = fileChooser.showSaveDialog(lblStatus.getScene().getWindow());//Указываем текущую сцену CodeNote.mainStage
        if (file != null) {
            file.delete();
            try {
                file.createNewFile();
                setFileToSave(file);
                //Save
                FileTree fileTree = fileTreeItem.getValue().getFileTree(fileTreeItem);
                FileRequest fileRequest = new FileRequest(fileTree);
                nettyClient.send(fileRequest);
            } catch (IOException e) {
                log.error(e.getStackTrace());
            }
        }
    }

    public void showModalDlg(TreeItem<FileTreeItem> fileTreeItem) throws IOException {
        Stage stage;
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Swing in JavaFX");
        FXMLLoader modalViewLoader = new FXMLLoader(getClass().getResource("/modalDlg.fxml"));
        Parent modalView = modalViewLoader.load();
        ModalDlgController modalDlgController = modalViewLoader.<ModalDlgController>getController();
        modalDlgController.setFileTreeItem(fileTreeItem);
        modalDlgController.setMainController(this);
        stage.setScene(new Scene(modalView));
        stage.show();
    }

    public FileTree getFileTree(TreeItem<FileTreeItem> fileTreeItem){
        FileTree fileTree = new FileTree(fileTreeItem.getValue().getName(), true, null);
        FileTree parentFileTree = new FileTree(fileTreeItem.getParent().getValue().getName(), true, null);
        parentFileTree.addChild(fileTree);
        fileTree = parentFileTree;
        fileTreeItem = fileTreeItem.getParent();
        while (fileTreeItem.getParent() != null) {
            fileTreeItem = fileTreeItem.getParent();
            parentFileTree = new FileTree(fileTreeItem.getValue().getName(), true, null);
            parentFileTree.addChild(fileTree);
            fileTree = parentFileTree;
        }
        log.info(fileTree.getReversed().toString());
        return fileTree;
    }
}

