package core.messages.response;

import core.messsages.Serializer;
import core.messsages.response.FileResponse;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileResponseTest {

    /**
     * Передача файла через протокол FileResponse. И сравнение итоговых контрольных сумм файлов.
     *
     * @throws IOException
     */
    @Test
    public void fileTransferTest() throws IOException {
        File file = new File("../server_folder/storage/test/file1.txt");
        String md5 = DigestUtils.md5Hex(Files.newInputStream(file.toPath()));
        File newFile = new File("newFile.txt");
        newFile.delete();
        newFile.createNewFile();
        FileResponse fileResponse = new FileResponse(file.toPath());

        while (!fileResponse.isFileEnded()) {
            fileResponse.readNextPortion();
            Object[] obj = Serializer.serialize(fileResponse);

            FileResponse fileResponseReceived = Serializer.deserialize((String) obj[1], FileResponse.class);
            fileResponseReceived.setPath(newFile.toPath());
            fileResponseReceived.write();
        }
        String afterCopyMd5 = DigestUtils.md5Hex(Files.newInputStream(file.toPath()));
        Assert.assertEquals(md5, afterCopyMd5);
        newFile.delete();
    }
}
