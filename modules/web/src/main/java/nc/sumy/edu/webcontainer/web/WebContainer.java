package nc.sumy.edu.webcontainer.web;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.File;

public interface WebContainer {

    String processStatic (File file);

    ServletResponse processServlet(ServletRequest request, Class klass);

    ServletResponse processJSP (ServletRequest request, File file);
}
