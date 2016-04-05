package nc.sumy.edu.webcontainer.web.stub;

import org.apache.commons.lang3.ArrayUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TestServletRequestResponse extends HttpServlet {

    private static final String TAG_P_CLOSE = "</p>";

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        out.println("<h1>Test Servlet</h1>");
        out.println("<body>");
        out.println("<h1>ServletRequest:</h1>");
        out.println("<p>RemoteHost: " + request.getRemoteHost() + TAG_P_CLOSE);
        out.println("<p>ContextPath: " + request.getServletContext().getContextPath() + TAG_P_CLOSE);
        out.println("<p>Parameter Accept: " + request.getParameter("Accept") + TAG_P_CLOSE);
        out.println("<p>Session: " + request.getSession().getClass().getName() + TAG_P_CLOSE);
        out.println("<h1>ServletResponse:</h1>");
        response.setContentType("text/html");
        response.setBufferSize(2048);
        out.println("<p>ContentType: " + response.getContentType() + TAG_P_CLOSE);
        out.println("\n</body>");
        request.getSession().setAttribute("Attribute1", "Value1");
        request.getSession().getAttribute("Attribute1");
        request.getSession().putValue("Attribute2","Value2");

        Method[] methods = request.getClass().getDeclaredMethods();
        invoke(methods, request);

        methods = response.getClass().getDeclaredMethods();
        invoke(methods, response);

        methods = this.getServletConfig().getClass().getDeclaredMethods();
        invoke(methods, this.getServletConfig());

        HttpSession session = request.getSession();
        methods = session.getClass().getDeclaredMethods();
        Method methodPutValue = null,
                methodGetAttribute = null,
                methodSetAttribute = null;
        try {
            methodPutValue = session.getClass().getMethod("putValue",new Class[]{String.class,Object.class});
            methodGetAttribute = session.getClass().getMethod("getAttribute",new Class[]{String.class});
            methodSetAttribute = session.getClass().getMethod("setAttribute",new Class[]{String.class,Object.class});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        methods = ArrayUtils.removeElement(methods, methodPutValue);
        methods = ArrayUtils.removeElement(methods, methodGetAttribute);
        methods = ArrayUtils.removeElement(methods, methodSetAttribute);
        invoke(methods, session);

    }

    private void invoke(Method[] methods, Object obj) {
        for (Method method : methods) {
            Object[] param = setParam(method);
            try {
                if (param.length == 0)
                    method.invoke(obj);
                else
                    method.invoke(obj, param);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                if (!e.getCause().getClass().equals(UnsupportedOperationException.class)) {
                    System.out.println(method.getName());
                    e.printStackTrace();
                }
            } catch (IllegalArgumentException e) {
                System.out.println(method.getName());
                e.printStackTrace();
            }
        }
    }

    private Object[] setParam(Method method) {
        Object[] param = new Object[method.getParameterTypes().length];
        if (param.length > 0)
            for (int i = 0; i < param.length; i++)
                switch (method.getParameterTypes()[i].getName()) {
                    case "boolean":
                        param[i] = Boolean.FALSE;
                        break;
                    case "byte":
                    case "short":
                    case "int":
                        param[i] = 0;
                        break;
                    case "long":
                        param[i] = 0L;
                        break;
                    case "float":
                        param[i] = 0.0f;
                        break;
                    case "double":
                        param[i] = 0.0d;
                        break;
                    case "char":
                        param[i] = '\u0000';
                        break;
                    default:
                        param[i] = null;
                        break;
                }
        return param;
    }
}
