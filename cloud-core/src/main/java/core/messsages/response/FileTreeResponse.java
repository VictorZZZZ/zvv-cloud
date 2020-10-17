package core.messsages.response;

import core.files.FileTree;
import core.messsages.AbstractMessage;
import lombok.Getter;
import lombok.Setter;

public class FileTreeResponse extends AbstractMessage {
    @Getter
    @Setter
    private FileTree fileTree;
    public FileTreeResponse() {
        this.setMessageType((byte) 4);
    }

    public FileTreeResponse(FileTree fileTree) {
        this.setMessageType((byte) 4);
        this.fileTree = fileTree;
    }
}
