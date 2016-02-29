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


public class CGIServlet  {


    public void process(Request request, Response response) {

        String uri = ""; //request.getURL();
        String servletName = uri.substring(uri.lastIndexOf("/") + 1);

        URLClassLoader loader = null;

        URL url = null;

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
            e.printStackTrace();
        }
        try {
            url = new URL(repository);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        loader = new URLClassLoader(new URL[]{url});

        Class newClass = null;
        try {
            newClass = loader.loadClass(servletName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        try {
            servlet = (Servlet) newClass.newInstance();
            servlet.service((ServletRequest) request, (ServletResponse) response);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}