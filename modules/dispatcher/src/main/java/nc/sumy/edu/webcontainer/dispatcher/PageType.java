package nc.sumy.edu.webcontainer.dispatcher;

import static org.apache.commons.lang3.StringUtils.split;

public enum PageType {
    HTML("text/html"),
    HTM("text/htm"),
    CSS("text/css"),
    XML("text/xml"),
    JSP("text/jsp"),
    PDF("application/pdf"),
    ZIP("application/zip"),
    JAVASCRIPT("application/javascript "),
    GIF("image/gif"),
    JPEG("image/jpeg"),
    JPG("image/jpg"),
    SWG("image/swg"),
    PNG("image/png");

    private final String fileExtension;

    PageType(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getFileExtension() {
        return split(fileExtension, "/")[1];
    }
}
