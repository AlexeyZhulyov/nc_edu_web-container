package nc.sumy.edu.webcontainer.http2java;

import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class HttpResponseTest {
    private HttpResponse response;
    private Map<String, String> headers;
    private static final String ENDL = "\r\n";
    private static final String SPARATOR = ": ";
    private static final String HTTP_VERSION = "HTTP/1.1 ";
    private static final String HOST = "Host";
    private static final String CONNECTION = "Connection";
    private static final String CLOSE_STR = "close";
    private static final String CONNECTION_CLOSE = "Connection: close";
    private static final String STRING = "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "\t<meta charset=\"UTF-8\">\n" +
            "\t<title>Document</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "\t\n" +
            "</body>\n" +
            "</html>";
    private static final String STRING1 = "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "\t<meta charset=\"UTF-8\">\n" +
            "\t<title>Document</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "\t<div style=\"text-align: center;\">\n" +
            "\t\t<h1>Hellow world!!!</h1>\n" +
            "\t\t<div>\n" +
            "\t\t\t<h3>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Nam atque fugit, ullam voluptate inventore illo, quisquam sit veniam reiciendis distinctio excepturi sapiente numquam incidunt perspiciatis sunt, aperiam rem quas nisi.</h3>\n" +
            "\t\t</div>\n" +
            "\t</div>\n" +
            "\t<div>\n" +
            "\t\t<h4>\n" +
            "\t\t\tLorem ipsum dolor sit amet, consectetur adipisicing elit. Dolorem ea ipsam, excepturi qui maiores quia modi deserunt quisquam omnis id animi aliquid amet quibusdam quidem ad nisi esse laboriosam iste.\n" +
            "\t\t</h4>\n" +
            "\t</div>\n" +
            "\t<div>\n" +
            "\t\t<ul>\n" +
            "\t\t\t<li>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Incidunt aperiam, minima perspiciatis dicta neque laboriosam temporibus cumque, aliquam libero consectetur, sit dolores nostrum quaerat labore vel accusantium, optio! Voluptate, dolores?</li>\n" +
            "\t\t\t<li>Dolor molestiae quas excepturi, ad! Esse quis, architecto sit dignissimos debitis doloremque a quam consequatur aliquid neque voluptates impedit veritatis nihil ratione, ut sed illo porro iusto, corporis excepturi deleniti.</li>\n" +
            "\t\t\t<li>Odio qui facilis omnis placeat inventore, nostrum perferendis porro. Aliquam consectetur voluptate illum officiis, atque accusamus molestias asperiores. Quod quisquam, excepturi quis illum iure laudantium. Quae aut atque, laboriosam maiores!</li>\n" +
            "\t\t\t<li>Vero delectus ex sed tempora nemo accusamus quia et beatae eos velit voluptate praesentium omnis fugit, totam excepturi eius ut maxime aspernatur nihil voluptatem similique reprehenderit iste veritatis ducimus. Similique.</li>\n" +
            "\t\t\t<li>Cupiditate consequuntur quas nobis, asperiores laudantium incidunt tempora dolorem laboriosam nam culpa! Accusantium, possimus veniam qui voluptate consequatur dolorem dolore laboriosam earum eos magnam! Nostrum, consectetur, beatae! Repudiandae, dolorem, quaerat.</li>\n" +
            "\t\t\t<li>Veritatis debitis alias sit vitae labore repellat eos enim corporis provident amet nobis, explicabo id ipsum eligendi fugiat quidem earum ut atque natus sint, adipisci maiores ex! Veritatis, tempore, odio.</li>\n" +
            "\t\t</ul>\n" +
            "\t</div>\n" +
            "</body>\n" +
            "</html>";
    private String result;

    @Test
    public void response1() {
        headers = new LinkedHashMap<String, String>();
        headers.put(HOST, "vk.com");
        headers.put(CONNECTION, CLOSE_STR);
        response = new HttpResponse(200, headers, STRING.getBytes());
        result = HTTP_VERSION + "200 OK" + ENDL +
                HOST + SPARATOR + "vk.com" + ENDL +
                CONNECTION_CLOSE + ENDL + ENDL +
                STRING;

        assertEquals(result.getBytes().length, response.getResponse().length);
        for (int i = 0; i < response.getResponse().length; i++) {
            assertEquals(result.getBytes()[i], response.getResponse()[i]);
        }
    }

    @Test
    public void response2() {
        headers = new LinkedHashMap<String, String>();
        headers.put(HOST, "jclubteam.slack.com");
        headers.put(CONNECTION, CLOSE_STR);
        response = new HttpResponse(200, headers, STRING1.getBytes());
        result = HTTP_VERSION + "200 OK" + ENDL +
                HOST + SPARATOR + "jclubteam.slack.com" + ENDL +
                CONNECTION_CLOSE + ENDL + ENDL +
                STRING1;

        assertEquals(result.getBytes().length, response.getResponse().length);
        assertEquals(result, new String(response.getResponse()));
    }
}
