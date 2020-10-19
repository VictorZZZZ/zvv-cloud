package core.messsages.response;

import core.messsages.AbstractMessage;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FileResponse extends AbstractMessage {
    private byte messageType = (byte)6;
}
