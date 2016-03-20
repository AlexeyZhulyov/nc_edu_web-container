package nc.sumy.edu.webcontainer.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

public interface JspHandler {
    public HttpServletResponse processJSP(HttpServletRequest request, File file);
}
