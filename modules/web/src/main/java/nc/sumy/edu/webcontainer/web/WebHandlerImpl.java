package nc.sumy.edu.webcontainer.web;

import java.io.*;

import static java.lang.String.format;
import static nc.sumy.edu.webcontainer.web.WebException.*;

public class WebHandlerImpl implements WebHandler {

    public String process(File page) {
        String pageToString = null;
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(page))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();

            while (line != null) {
                stringBuilder.append(line);
                stringBuilder.append(System.lineSeparator());
                line = bufferedReader.readLine();
            }
            pageToString = stringBuilder.toString();
        } catch (FileNotFoundException e) {
            throw new WebException(format(CANNOT_FIND_FILE, page.getName()), e);
        } catch (IOException e) {
            throw new WebException(format(CANNOT_READ_FILE, page.getName()), e);
        }
        return pageToString;
    }
}
