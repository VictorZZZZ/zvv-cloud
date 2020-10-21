package core.messages.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import core.files.FileTree;
import core.messsages.AbstractMessage;
import core.messsages.Serializer;
import core.messsages.request.FileRequest;
import org.junit.Assert;
import org.junit.Test;


public class FileRequestTest extends AbstractMessage {

    @Test
    public void testGetReversedFileTree() throws JsonProcessingException {
        FileRequest fileRequest = Serializer.
                deserialize("{\"messageType\":5,\"fileTree\":{\"dir\":false,\"name\":\"test\",\"children" +
                        "\":[{\"dir\":false,\"name\":\"dir1\",\"children\":[{\"dir\":false,\"name\":\"file2.txt\"" +
                        ",\"children\":[]}]}]}}",FileRequest.class);
        FileTree fileTree = fileRequest.getReversedFileTree();
        Assert.assertTrue(fileTree.toString().equals("test/dir1/file2.txt"));
    }
}
