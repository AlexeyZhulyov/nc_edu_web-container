package nc.sumy.edu.webcontainer.web;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@SuppressWarnings("PMD")
public class HttpSessionImpl implements HttpSession {

    private ConcurrentMap<String,Object> attributes = new ConcurrentHashMap<>();

    @Override
    public long getCreationTime() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getLastAccessedTime() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ServletContext getServletContext() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setMaxInactiveInterval(int i) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getMaxInactiveInterval() {
        throw new UnsupportedOperationException();
    }

    @Override
    public HttpSessionContext getSessionContext() {
        throw new UnsupportedOperationException();
    }

    @Override
     public Object getAttribute(String s) {
        System.out.println("------------------- get attribute=" + s + " value=" + attributes.get(s));
        return attributes.get(s);
    }

    @Override
    public Object getValue(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String[] getValueNames() {
        throw new UnsupportedOperationException();
    }

    @Override
     public void setAttribute(String s, Object o) {
        System.out.println("------------------- set attribute=" + s + " value=" + o);
        attributes.put(s, o);
    }

    @Override
     public void putValue(String s, Object o) {
        System.out.println("------------------- put attribute=" + s + " value=" + o);
        attributes.put(s, o);
    }

    @Override
    public void removeAttribute(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeValue(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void invalidate() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isNew() {
        throw new UnsupportedOperationException();
    }
}
