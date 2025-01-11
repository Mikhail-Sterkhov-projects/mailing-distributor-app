import com.jrest.http.client.HttpClients;
import com.jrest.mvc.model.Content;
import com.jrest.mvc.model.HttpResponse;

public class TestHttpClient {

    private static final String REQUEST_URI = "http://localhost:8098/sendMailing";
    private static final String PAYLOAD_TEXT = """
                        {
                          "type": "email",
                          "subject": "Greeting",
                          "text": "Привет! Это тестовое сообщение, если оно дошло, значит стонлекс лох"
                        }
                        """;

    public static void main(String[] args) {
        var httpClient = HttpClients.createClient();
        var httpResponseOpt = httpClient.executePost(REQUEST_URI, Content.fromText(PAYLOAD_TEXT));

        System.out.println(httpResponseOpt);

        if (httpResponseOpt.isPresent()) {
            HttpResponse httpResponse = httpResponseOpt.get();

            System.out.println(httpResponse.getCode());
            System.out.println(httpResponse.getContent());
        }
    }
}
