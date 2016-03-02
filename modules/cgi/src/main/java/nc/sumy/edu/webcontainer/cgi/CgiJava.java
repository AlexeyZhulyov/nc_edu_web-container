package nc.sumy.edu.webcontainer.cgi;

import nc.sumy.edu.webcontainer.http.HttpMethod;
import nc.sumy.edu.webcontainer.http.Request;

import org.atteo.classindex.ClassIndex;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Properties;

public class CgiJava implements CgiHandler {

    Request request = null;
    private File tmp = null;
    PrintStream printStream = null;
    Properties properties = null;
    String queryString = null;
    Class newClass = null;
    String className = null;

    public CgiJava(String className) {
        this.className = className;

        createTempFile("TmpDir", "cgi_");
        createPrintStream(tmp);
        System.setOut(printStream);

        properties = System.getProperties();
    }

    public void setEnvironmentVariable(String name, String value) {
        properties.setProperty(name, value);
    }

    @Override
    public String process(Request request) {
        this.request = request;

        newClass = searchClass(className);

        setQueryString(request);

        setEnvironmentVariable("REDIRECT_STATUS", "true");
        setEnvironmentVariable("REQUEST_METHOD", request.getMethod().name());
        setEnvironmentVariable("SCRIPT_NAME", className);
        setEnvironmentVariable("GATEWAY_INTERFACE", "CGI/1.1");
        setEnvironmentVariable("CONTENT_LENGTH", Integer.toString(queryString.length()));
        setEnvironmentVariable("CONTENT_TYPE", "text/html");
        if (HttpMethod.GET == request.getMethod()) {
            setEnvironmentVariable("QUERY_STRING", queryString);
        }

        invokeMainMethod(newClass);

        BufferedReader readerFromFile = setReaderFromFile(tmp);

        String responseString = bufReaderToStr(readerFromFile);

        tmp.deleteOnExit();
        return responseString;
    }

    private static String urlDecode(String in) {
        StringBuffer out = new StringBuffer(in.length());
        int numChar = 0;
        while (numChar < in.length()) {
            char character = in.charAt(numChar);
            numChar++;
            if (character == '+') character = ' ';
            else if (character == '%') {
                character = (char) Integer.parseInt(in.substring(numChar, numChar + 2), 16);
                numChar += 2;
            }
            out.append(character);
        }
        return new String(out);
    }


    private Class searchClass(String className) {
        for (Class<?> klass : ClassIndex.getAnnotated(Cgi.class)) {
            if (klass.getSimpleName().equals(className))
                return klass;
            //klass.getAnnotation(Cgi.class).id();
        }
        throw new CgiException("Class \"" + className + "\" not found");
    }

    private void createTempFile(String fileName, String dirName) {
        try {
            File tmpDir = new File(dirName);
            tmpDir.mkdir();
            tmp = File.createTempFile(fileName, ".tmp", tmpDir);
        } catch (IOException e) {
            throw new CgiException("Cannot create temporary file", e);
        }
    }

    private void createPrintStream(File file) {
        try {
            printStream = new PrintStream(file);
        } catch (FileNotFoundException e) {
            throw new CgiException("Cannot create new stream to file", e);
        }
    }

    private void setQueryString(Request request) {
        queryString = "";
        for (Map.Entry entry : request.getParameters().entrySet()) {
            if (!queryString.isEmpty()) queryString += "&";
            queryString += urlDecode(entry.getKey().toString()) + "=";
            queryString += urlDecode(entry.getValue().toString());
        }
    }

    private void invokeMainMethod(Class klass) {
        try {
            Class[] argTypes = new Class[]{String[].class};
            Method main = klass.getDeclaredMethod("main", argTypes);
            String[] mainArgs = new String[]{""};
            main.invoke(null, (Object) mainArgs);
        } catch (NoSuchMethodException e) {
            throw new CgiException("Method \"main\" not found", e);
        } catch (IllegalAccessException e) {
            throw new CgiException("No access", e);
        } catch (InvocationTargetException e) {
            throw new CgiException("Invocation target exception", e);
        }
    }

    private BufferedReader setReaderFromFile(File file) {
        BufferedReader readerFromFile = null;
        try {
            readerFromFile = new BufferedReader(new FileReader(tmp));
        } catch (FileNotFoundException e) {
            throw new CgiException("Cannot create new reader for file \"" + file.getName() + "\"", e);
        }
        return readerFromFile;
    }

    private String bufReaderToStr(BufferedReader bufferedReader) {
        StringBuilder stringBuilder = new StringBuilder();
        String temp = null;
        try {
            while ((temp = bufferedReader.readLine()) != null) {
                temp += '\n';
                stringBuilder.append(temp);
            }
        } catch (IOException e) {
            throw new CgiException("Cannot read temporary file", e);
        }
        return stringBuilder.toString();
    }
}