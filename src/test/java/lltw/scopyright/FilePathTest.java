package lltw.scopyright;

import java.io.File;

public class FilePathTest {
    public static void main(String[] args) {
        String path = "D:/it/gitClone/scopyright/target/classes/config-example.toml";
        File file = new File(path);
        if (file.exists()) {
            System.out.println("文件存在！");
        } else {
            System.out.println("文件不存在！");
        }
    }
}
