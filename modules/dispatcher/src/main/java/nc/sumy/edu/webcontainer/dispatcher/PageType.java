package nc.sumy.edu.webcontainer.dispatcher;

import org.apache.commons.lang3.StringUtils;

/**
 * Enum that contains the types of MIME pages.
 * @author Vinogradov M.O.
 */
public enum PageType {
    HTML("html", "text/html"),
    HTM("htm", "text/htm"),
    CSS("css", "text/css"),
    XML("xml", "text/xml"),
    JSP("jsp", "text/jsp"),
    CGI("cgi", "text/html"),
    PDF("pdf", "application/pdf"),
    ZIP("zip", "application/zip"),
    JAVASCRIPT("js", "application/javascript"),
    GIF("gif", "image/gif"),
    JPEG("jpeg", "image/jpeg"),
    JPG("jpg", "image/jpg"),
    SWG("swg", "image/swg"),
    PNG("png", "image/png"),
    DEFAULT("-", "text/html");

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

    public String getMimeViaExtension(String fileExtension) {
        for (PageType pageType: PageType.values()) {
            if (StringUtils.equals(pageType.getFileExtension(), fileExtension)) {
                return pageType.getMIME();
            }
        }
        return HTML.getMIME();
    }

}