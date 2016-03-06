package nc.sumy.edu.webcontainer.web;

import java.io.*;

public class WebHandlerImpl implements WebHandler {

    public String process(File page) {
        String pageToString = null;
        try(BufferedReader br = new BufferedReader(new FileReader(page))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            pageToString = sb.toString();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Cannot find file",e);
        } catch (IOException e) {
            throw new RuntimeException("Cannot read file",e);
        }
        return pageToString;
    }
}
