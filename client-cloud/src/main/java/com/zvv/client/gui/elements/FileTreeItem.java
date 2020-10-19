package com.zvv.client.gui.elements;

import core.files.FileTree;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@NoArgsConstructor
public class FileTreeItem extends TreeItem<FileTreeItem> {
    public static final String DIR_TYPE = "dir";
    public static final String FILE_TYPE = "file";
    @Getter
    private boolean dir=false;

    @Getter
    private String name;

    @Getter
    private Node icon;

    public FileTreeItem(String name, boolean dir) {
        this.dir = dir;
        this.name = name;
        if (dir) {
            icon = new ImageView(new Image(FileTreeItem.class.getResourceAsStream("/dir.png")));
        } else {
            icon = new ImageView(new Image(FileTreeItem.class.getResourceAsStream("/file.png")));
        }
    }

    public FileTreeItem(FileTreeItem value, String name) {
        super(value, value.icon);
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * преобразует элемент в FileTree с полным деревом до корня.
     * @return
     */
    public FileTree getFileTree() {
        FileTree fileTree = new FileTree();
        TreeItem<FileTreeItem> fileTreeItem = this;
        while (fileTreeItem.getParent() != null) {
            FileTree parentFileTree = new FileTree();
            fileTree.setName(fileTreeItem.getValue().getName());
            TreeItem<FileTreeItem> parentItem = fileTreeItem.getParent();
            parentFileTree.setName(parentItem.getValue().getName());
            parentFileTree.addChild(fileTree);
            fileTree = parentFileTree;
            fileTreeItem = parentItem;
        }
        return fileTree;
    }
}
