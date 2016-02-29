package nc.sumy.edu.webcontainer.cgi;


import nc.sumy.edu.webcontainer.http2java.HttpMethod;
import nc.sumy.edu.webcontainer.http2java.Request;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.Properties;

public class CGIJava implements CGI {

    Request request = null;
    private File tmp = null;
    PrintStream printStream = null;
    Properties properties = null;
    File file = null;
    String queryString = null;
    URLClassLoader loader = null;
    URL url = null;
    File classPath = null;
    String repository = null;
    Class newClass = null;
    String className = null;

    public CGIJava(Request request, File file) {
        this.request = request;
        this.file = file;
        try {
            File tmpDir = new File("TmpDir");
            tmpDir.mkdir();
            tmp = File.createTempFile("cgi_", ".tmp", tmpDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            printStream = new PrintStream(tmp);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.setOut(printStream);
        properties = System.getProperties();

        queryString = "";
        for (Map.Entry entry : request.getParameters().entrySet()) {
            if (!queryString.isEmpty()) queryString += "&";
            queryString += urlDecode(entry.getKey().toString()) + "=";
            queryString += urlDecode(entry.getValue().toString());
        }

    }

    public void setEnvironmentVariable(String name, String value) {
        properties.setProperty(name, value);
    }

    public static String urlDecode(String in) {
        StringBuffer out = new StringBuffer(in.length());
        int i = 0;
        int j = 0;

        while (i < in.length()) {
            char ch = in.charAt(i);
            i++;
            if (ch == '+') ch = ' ';
            else if (ch == '%') {
                ch = (char) Integer.parseInt(in.substring(i, i + 2), 16);
                i += 2;
            }
            out.append(ch);
            j++;
        }
        return new String(out);
    }

    @Override
    public String process(Request request) {

        setEnvironmentVariable("REDIRECT_STATUS", "true");
        setEnvironmentVariable("REQUEST_METHOD", request.getMethod().name());
        setEnvironmentVariable("SCRIPT_FILENAME", file.getPath());
        setEnvironmentVariable("SCRIPT_NAME", "Hello");
        setEnvironmentVariable("GATEWAY_INTERFACE", "CGI/1.1");
        setEnvironmentVariable("CONTENT_LENGTH", Integer.toString(queryString.length()));
        setEnvironmentVariable("CONTENT_TYPE", "text/html");
        if (HttpMethod.GET == request.getMethod()) {
            setEnvironmentVariable("QUERY_STRING", queryString);
        }

        classPath = new File("modules\\cgi\\target\\classes\\");
        try {
            repository = "file:" + classPath.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            url = new URL(repository);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        loader = new URLClassLoader(new URL[]{url});
        className = "nc.sumy.edu.webcontainer.cgi." + System.getProperty("SCRIPT_NAME");
        try {
            newClass = loader.loadClass(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            Class<?> c = Class.forName(className);
            Class[] argTypes = new Class[]{String[].class};
            Method main = c.getDeclaredMethod("main", argTypes);
            String[] mainArgs = new String[]{""};
            main.invoke(null, (Object) mainArgs);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        StringBuilder sb = new StringBuilder();

        try {
            BufferedReader in = new BufferedReader(new FileReader(tmp));
            try {
                String s;
                while ((s = in.readLine()) != null) {
                    sb.append(s);
                    sb.append("\n");
                }
            } finally {
                in.close();
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }
}
