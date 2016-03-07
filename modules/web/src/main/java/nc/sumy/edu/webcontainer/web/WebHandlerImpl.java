package nc.sumy.edu.webcontainer.web;

import java.io.*;

import static java.lang.String.format;
import static nc.sumy.edu.webcontainer.web.WebException.*;

public class WebHandlerImpl implements WebHandler {

    public String process(File page) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(page))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append('\n');
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            throw new WebException(format(CANNOT_FIND_READ_FILE, page.getName()), e);
        }
    }
}
