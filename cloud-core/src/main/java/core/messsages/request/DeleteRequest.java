package core.messsages.request;

import core.auth.User;
import core.files.FileTree;
import core.messsages.AbstractMessage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DeleteRequest extends AbstractMessage {
    private byte messageType = (byte) 8;

    private User user;
    private FileTree fileTree;

    public DeleteRequest(FileTree fileTree,User user) {
        this.fileTree = fileTree;
        this.user = user;
    }
}
