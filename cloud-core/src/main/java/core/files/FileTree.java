package core.files;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@NoArgsConstructor
@Getter
@Setter
public class FileTree {

    private boolean dir;
    private String name;
    private ArrayList<FileTree> children = new ArrayList<>();

    /**
     * JsonIgnore иначе получим бесконечный цикл при создании json.
     */
    @Setter
    @Getter
    @JsonIgnore
    private FileTree parent;

    public FileTree(String name, boolean dir,FileTree parent) {
        this.dir = dir;
        this.name = name;
        this.parent = parent;
        children = new ArrayList<>();
    }

    public void addChild(FileTree fileTree){
        children.add(fileTree);
    }

    @Override
    public String toString() {
        FileTree fileTree = this;
        StringBuilder sb = new StringBuilder();
        sb.append(fileTree.getName());
        while(fileTree.getParent() != null){
            FileTree parent = fileTree.getParent();
            sb.insert(0,parent.getName()+"/");
            fileTree = parent;
        }
        return sb.toString();
    }
}
