package nc.sumy.edu.webcontainer.dispatcher;

public enum PageType {
    HTML("html", "text/html"),
    HTM("htm", "text/htm"),
    CSS("css", "text/css"),
    XML("xml", "text/xml"),
    JSP("jsp", "text/jsp"),
    PDF("pdf", "application/pdf"),
    ZIP("zip", "application/zip"),
    JAVASCRIPT("js", "application/javascript "),
    GIF("gif", "image/gif"),
    JPEG("jpeg", "image/jpeg"),
    JPG("jpg", "image/jpg"),
    SWG("swg", "image/swg"),
    PNG("png", "image/png");

    private final String fileExtension;
    private final String mime;

    PageType(String fileExtension, String mime) {
        this.fileExtension = fileExtension;
        this.mime = mime;
    }

    public String getMIME() {
        return mime;
    }

    public String getFileExtension() {
        return fileExtension;
    }
}
