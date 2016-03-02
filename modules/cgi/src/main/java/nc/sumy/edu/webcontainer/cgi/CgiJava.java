package nc.sumy.edu.webcontainer.cgi;

import nc.sumy.edu.webcontainer.http.HttpMethod;
import nc.sumy.edu.webcontainer.http.Request;
import org.atteo.classindex.ClassIndex;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.Properties;

public class CgiJava implements CgiHandler {

    Request request = null;
    private File tmp = null;
    PrintStream printStream = null;
    Properties properties = null;
    String queryString = null;
    URLClassLoader loader = null;
    URL url = null;
    File classPath = null;
    String repository = null;
    Class newClass = null;
    String className = null;

    public CgiJava(String className) {
        this.className = className;

        try {
            File tmpDir = new File("TmpDir");
            tmpDir.mkdir();
            tmp = File.createTempFile("cgi_", ".tmp", tmpDir);
        } catch (IOException e) {
            throw new CgiException("Cannot create temporary file", e);
        }

        try {
            printStream = new PrintStream(tmp);
        } catch (FileNotFoundException e) {
            throw new CgiException("Cannot create new stream to file", e);
        }

        System.setOut(printStream);

        properties = System.getProperties();
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
        this.request = request;

        newClass = searchClass(className);

        queryString = "";
        for (Map.Entry entry : request.getParameters().entrySet()) {
            if (!queryString.isEmpty()) queryString += "&";
            queryString += urlDecode(entry.getKey().toString()) + "=";
            queryString += urlDecode(entry.getValue().toString());
        }

        setEnvironmentVariable("REDIRECT_STATUS", "true");
        setEnvironmentVariable("REQUEST_METHOD", request.getMethod().name());
        setEnvironmentVariable("SCRIPT_NAME", className);
        setEnvironmentVariable("GATEWAY_INTERFACE", "CGI/1.1");
        setEnvironmentVariable("CONTENT_LENGTH", Integer.toString(queryString.length()));
        setEnvironmentVariable("CONTENT_TYPE", "text/html");
        if (HttpMethod.GET == request.getMethod()) {
            setEnvironmentVariable("QUERY_STRING", queryString);
        }

        try {
            Class[] argTypes = new Class[]{String[].class};
            Method main = newClass.getDeclaredMethod("main", argTypes);
            String[] mainArgs = new String[]{""};
            main.invoke(null, (Object) mainArgs);
        } catch (NoSuchMethodException e) {
            throw new CgiException("Method \"main\" not found", e);
        } catch (IllegalAccessException e) {
            throw new CgiException("No access", e);
        } catch (InvocationTargetException e) {
            throw new CgiException("Invocation target exception", e);
        }

        StringBuilder sb = new StringBuilder();

        BufferedReader in = null;

        try {
            in = new BufferedReader(new FileReader(tmp));
        } catch (FileNotFoundException e) {
            throw new CgiException("Cannot create new reader for temporary file", e);
        }
        String s = null;
        try {
            while ((s = in.readLine()) != null) {
                sb.append(s);
                sb.append("\n");
            }
        } catch (IOException e) {
            throw new CgiException("Cannot read temporary file", e);
        }
        tmp.deleteOnExit();
        return sb.toString();
    }

    public Class searchClass(String className) {
        for (Class<?> klass : ClassIndex.getAnnotated(Cgi.class)) {
            if (klass.getSimpleName().equals(className))
                return klass;
            //klass.getAnnotation(Cgi.class).id();
        }
        throw new CgiException("Class \"" + className + "\" not found");
    }
}
