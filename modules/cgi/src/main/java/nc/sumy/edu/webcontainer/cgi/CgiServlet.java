package nc.sumy.edu.webcontainer.cgi;

import nc.sumy.edu.webcontainer.http.Request;
import nc.sumy.edu.webcontainer.http.Response;

import javax.servlet.Servlet;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class CgiServlet  {
    public void process(Request request, Response response) {

        String uri = ""; //request.getURL();
        String servletName = uri.substring(uri.lastIndexOf("/") + 1);
        URLClassLoader loader = null;
        URL url = null;
        Class newClass = null;
        Servlet servlet = null;

//      servletPath get from Configuration
//        Configuration configuration = new Configuration();
//        String servletPath = configuration.getServletsPath();
        String servletPath = "\\";
        File classPath = new File(servletPath);

        String repository = null;

        try {
            repository = "file:" + classPath.getCanonicalPath() + File.separator;
        } catch (IOException e) {
            throw new CgiException("Cannot get class path", e);
        }
        try {
            url = new URL(repository);
        } catch (MalformedURLException e) {
            throw new CgiException("Cannot create URL", e);
        }
        loader = new URLClassLoader(new URL[]{url});

        try {
            newClass = loader.loadClass(servletName);
        } catch (ClassNotFoundException e) {
            throw new CgiException("Class \"" + servletName + "\" not found", e);
        }

        try {
            servlet = (Servlet) newClass.newInstance();
            servlet.service((ServletRequest) request, (ServletResponse) response);
        } catch (InstantiationException e) {
            throw new CgiException("Cannot create new instance for class \"" + servletName + "\"", e);
        } catch (IllegalAccessException e) {
            throw new CgiException("No access", e);
        } catch (ServletException e) {
            throw new CgiException("Servlet exception", e);
        } catch (IOException e) {
            throw new CgiException("Input/output exception", e);
        }
    }
}
