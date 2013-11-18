/**
 * Created with IntelliJ IDEA.
 * User: leejaehoon
 * Date: 13. 10. 13.
 * Time: 오후 2:21
 * To change this template use File | Settings | File Templates.
 */
public class FileSystemException extends Exception {
    public FileSystemException(String exception) {
        super();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public FileSystemException(String message, Exception cause) {
        super(message, cause);
        System.out.println(message);
    }
}
