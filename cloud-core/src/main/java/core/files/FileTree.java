package core.files;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@NoArgsConstructor
public class FileTree {
    public static final String DIR_TYPE="dir";
    public static final String FILE_TYPE="file";
    @Getter
    private String type;

    @Getter
    private String name;
    @Setter
    private FileTree parent;
    @Setter
    @Getter
    private ArrayList<FileTree> children;

    public FileTree(String name, String type,FileTree parent) {
        this.type = type;
        this.name = name;
        this.parent = parent;
        children = new ArrayList<>();
    }

    public void addChild(FileTree fileTree){
        children.add(fileTree);
    }

}
