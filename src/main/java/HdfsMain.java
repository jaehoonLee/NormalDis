import org.apache.hadoop.fs.FileSystem;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: leejaehoon
 * Date: 13. 10. 18.
 * Time: 오전 12:10
 * To change this template use File | Settings | File Templates.
 */
public class HdfsMain {
    public static void main(String args[]) throws IOException {

        FileSystem fileSystem = HdfsUtils.getFileSystem(HdfsUtils.HDFS_URL_PREFIX);
        InputStream inputStream = HdfsUtils.getInputStream(fileSystem, "/output/part-r-00000");
        OutputStream outputStream = new FileOutputStream("test.txt");


//        StringBuffer out = new StringBuffer();
//        byte[] b = new byte[4096];
//        for (int n; (n = inputStream.read(b)) != -1;) {
//            out.append(new String(b, 0, n));
//        }
//        System.out.println(out.toString());

        //read and write to file
        while(true)
        {
            int data = inputStream.read();

            if(data == -1)
            {
                System.out.println("File exit");
                break;
            }

            outputStream.write(data);
        }
        outputStream.close();



    }
}
