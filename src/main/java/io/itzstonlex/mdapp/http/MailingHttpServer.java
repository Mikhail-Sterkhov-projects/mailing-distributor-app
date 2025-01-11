package io.itzstonlex.mdapp.http;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jrest.http.server.HttpServer;
import com.jrest.mvc.model.HttpProtocol;
import io.itzstonlex.mdapp.mailing.MailingService;
import io.itzstonlex.mdapp.properties.HttpServerProperties;
import lombok.RequiredArgsConstructor;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class MailingHttpServer {

    private final MailingService mailingService;
    private final HttpServerProperties properties;

    public void start() {
        var socketAddress = new InetSocketAddress(
                properties.getHost(),
                properties.getPort()
        );
        var httpServer = HttpServer.builder()
                .socketAddress(socketAddress)
                .protocol(HttpProtocol.HTTP_1_0)
                .executorService(Executors.newCachedThreadPool())
                .build();

        System.out.println("Listening http-server on " + socketAddress);

        httpServer.registerRepository(new MailingHttpRequestHandler(mailingService));
        httpServer.bind();
    }
}
