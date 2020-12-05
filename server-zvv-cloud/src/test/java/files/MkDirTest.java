package files;

import org.junit.Test;

import java.io.File;

public class MkDirTest {

    @Test
    public void mkDirTest(){
        File file = new File("test/Новая папка");
        file.mkdir();
    }
}
