package nc.sumy.edu.webcontainer.http2java;

import nc.sumy.edu.webcontainer.http2Java.HttpResponse;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

public class HttpResponseTest {
    private HttpResponse response;
    private Map<String, String> headers;
    private String endl = "\r\n";
    private String sparator = ": ";
    private String httpVersion = "HTTP/1.1 ";
    private String host = "Host";
    private String accept = "Accept";
    private String connection = "Connection";
    private String close  = "close";
    private String connectionClose = "Connection: close";
    private String bodyString1 = "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "\t<meta charset=\"UTF-8\">\n" +
            "\t<title>Document</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "\t\n" +
            "</body>\n" +
            "</html>";
    private String bodyString2 = "<!DOCTYPE html>\n" +
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
    String result;


    @Test
    public void testResponse1() {
        headers = new LinkedHashMap<String, String>() {{
            put(host, "vk.com");
            put(connection, close);
        }};
        response = new HttpResponse(200, headers, bodyString1.getBytes());
        result = httpVersion + "200 OK" + endl +
                host + sparator + "vk.com" + endl  +
                connectionClose + endl + endl +
                bodyString1;

        Assert.assertEquals(result.getBytes().length, response.getResponse().length);
        for (int i = 0; i < response.getResponse().length; i++) {
            Assert.assertEquals(result.getBytes()[i], response.getResponse()[i]);
        }
    }

    @Test
    public void testResponse2() {
        headers = new LinkedHashMap<String, String>() {{
            put(host, "jclubteam.slack.com");
            put(connection, close);
        }};
        response = new HttpResponse(200, headers, bodyString2.getBytes());
        result = httpVersion + "200 OK" + endl +
                host + sparator + "jclubteam.slack.com" + endl  +
                connectionClose + endl + endl +
                bodyString2;

        Assert.assertEquals(result.getBytes().length, response.getResponse().length);
        Assert.assertEquals(result, new String(response.getResponse()));
    }
}
