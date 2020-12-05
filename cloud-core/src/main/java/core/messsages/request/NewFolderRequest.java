package core.messsages.request;

import core.auth.User;
import core.files.FileTree;
import core.messsages.AbstractMessage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class NewFolderRequest extends AbstractMessage {
    private byte messageType=(byte) 7;

    private User user;
    private FileTree fileTree;

    public NewFolderRequest(FileTree fileTree,User user) {
        this.fileTree = fileTree;
        this.user = user;
    }
}
