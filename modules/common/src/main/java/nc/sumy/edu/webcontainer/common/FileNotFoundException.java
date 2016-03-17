package nc.sumy.edu.webcontainer.common;

public class FileNotFoundException extends RuntimeException {
    public FileNotFoundException(String fileName) {
        super("Unable to find the file by name " + fileName);
    }
}