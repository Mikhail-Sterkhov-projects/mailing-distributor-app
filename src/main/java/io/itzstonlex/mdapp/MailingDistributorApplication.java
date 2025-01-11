package io.itzstonlex.mdapp;

import com.google.inject.Stage;
import io.itzstonlex.mdapp.http.MailingHttpServer;

import java.util.List;

public class MailingDistributorApplication {

    public static void main(String[] args) {
        var injector = GuiceStarter.builder()
                .stage(Stage.PRODUCTION)
                .modulesImpls(List.of(new MailingApplicationModule()))
                .modulesClasses(List.of())
                .build()
                .start();

        var httpServer = injector.getInstance(MailingHttpServer.class);
        httpServer.start();
    }
}
