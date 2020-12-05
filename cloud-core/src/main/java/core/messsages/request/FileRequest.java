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

}
