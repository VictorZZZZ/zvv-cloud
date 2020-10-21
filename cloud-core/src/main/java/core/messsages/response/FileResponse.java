package core.messsages.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import core.messsages.AbstractMessage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@NoArgsConstructor
@Getter
@Setter
@Log4j2
public class FileResponse extends AbstractMessage {
    public static final int PORTION = 256;
    private byte messageType = (byte) 6;

    private Path path;

    private byte[] data;

    private long fileSize;

    private long leftToRead;

    @JsonIgnore
    private boolean fileEnded = false;

    @JsonIgnore
    private FileChannel channel;

    @JsonIgnore
    ByteBuffer buffer;

    @JsonIgnore
    private RandomAccessFile src;


    public FileResponse(Path path) throws IOException {
        this.path = path;
        src = new RandomAccessFile(path.toFile(), "r");
        channel = src.getChannel();
        buffer = ByteBuffer.allocate(PORTION);
        fileSize = Files.size(path);
        leftToRead = fileSize;
        if(fileSize < PORTION){
            data = new byte[(int) fileSize];
        } else {
            data = new byte[PORTION];
        }
    }

    public void readNextPortion() {
        int bytesRead = 0;
        try {
            bytesRead = channel.read(buffer);
            leftToRead-= bytesRead;
            if(leftToRead <= 0){
                setFileEnded(true);
                src.close();
            }
            if (bytesRead > -1) {
                buffer.flip();
                if(bytesRead < data.length) {
                    data = new byte[bytesRead];
                }
                buffer.get(data);
                buffer.clear();
            } else {
                setFileEnded(true);
                src.close();
            }
        } catch (IOException e) {
            log.error(e.getStackTrace());
        }
    }

    public void write() throws IOException {
        Files.write(path, data, StandardOpenOption.APPEND);
    }

}
