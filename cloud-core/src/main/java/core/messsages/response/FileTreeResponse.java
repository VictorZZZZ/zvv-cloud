package core.messsages.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import core.files.FileTree;
import core.messsages.AbstractMessage;
import core.messsages.Serializer;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

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

    public static void main(String[] args) throws JsonProcessingException {
        //тест сериалайз
        FileTree ftRoot = new FileTree("root",true,null);
        FileTree dir1 = new FileTree("1",true,ftRoot);
        FileTree file1 = new FileTree("root",false,dir1);
        FileTreeResponse fileTreeResponse = new FileTreeResponse(ftRoot);
        System.out.println(Arrays.toString(Serializer.serialize(fileTreeResponse)));
    }
}
