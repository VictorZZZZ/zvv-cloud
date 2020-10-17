package com.zvv.client.gui.elements;

import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FileTreeItem extends TreeItem<FileTreeItem>{
    public static final String DIR_TYPE="dir";
    public static final String FILE_TYPE="file";
    @Getter
    private String type;

    @Getter
    private String name;

    public FileTreeItem(String name, String type) {
        this.type = type;
        this.name = name;
    }

    public FileTreeItem(FileTreeItem value, String name) {
        super(value);
        Node icon;
        if(value.getType().equals(DIR_TYPE)) {
            icon = new ImageView(new Image(FileTreeItem.class.getResourceAsStream("/dir.png")));
        } else {
            icon = new ImageView(new Image(FileTreeItem.class.getResourceAsStream("/file.png")));
        }
        this.name = name;
        this.setGraphic(icon);
    }

    @Override
    public String toString() {
        return name;
    }
}
