package core.messsages.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import core.files.FileTree;
import core.messsages.AbstractMessage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class FileRequest extends AbstractMessage {
    private byte messageType = (byte) 5;
    @JsonProperty
    private FileTree fileTree;

    public FileRequest(FileTree fileTree) {
        this.fileTree = fileTree;
    }

    /**
     * После десериализации file tree позиционируется на корневой каталог. А в данном случае нам нужен конечный файл,
     * который мы будем передавать. Также при десериализации в каждом элементе заполняется только поле children,
     * а parent нет. Поэтому здесь при прохождении всех children присваиваем им parent.
     * @return
     */
    @JsonIgnore
    public FileTree getReversedFileTree() {
        FileTree lastFileTree;
        FileTree result = fileTree;
        while(!result.getChildren().isEmpty()){
            lastFileTree = result.getChildren().get(0);
            lastFileTree.setParent(result);
            result = lastFileTree;
        }
        return result;
    }
}
