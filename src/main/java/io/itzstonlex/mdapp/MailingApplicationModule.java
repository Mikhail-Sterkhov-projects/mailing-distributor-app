package io.itzstonlex.mdapp;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.itzstonlex.mdapp.http.MailingHttpServer;
import io.itzstonlex.mdapp.mailing.MailingService;
import io.itzstonlex.mdapp.properties.EmailMetadataProperties;
import io.itzstonlex.mdapp.properties.HttpServerProperties;
import io.itzstonlex.mdapp.properties.JdbcConnectionProperties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;

public final class MailingApplicationModule extends AbstractModule {

    private static final ClassLoader CLASS_LOADER = MailingApplicationModule.class.getClassLoader();

    @Override
    protected void configure() {
        requireBinding(HttpServerProperties.class);
        requireBinding(JdbcConnectionProperties.class);

        bind(MailingService.class).asEagerSingleton();
        bind(MailingHttpServer.class).asEagerSingleton();
    }

    @Singleton
    @Provides
    public EmailMetadataProperties provideEmailMetadataProperties() {
        var properties = loadProperties("emailMetadata.properties");
        return EmailMetadataProperties.builder()
                .credentialsUsername(properties.getProperty("credentials.username"))
                .credentialsEmail(properties.getProperty("credentials.email"))
                .credentialsPassword(properties.getProperty("credentials.password"))
                .smtpHost(properties.getProperty("smtp.host"))
                .smtpPort(properties.getProperty("smtp.port"))
                .build();
    }

    @Singleton
    @Provides
    public HttpServerProperties provideHttpServerProperties() {
        var properties = loadProperties("httpServer.properties");
        return HttpServerProperties.builder()
                .host(properties.getProperty("host"))
                .port(Integer.parseInt(properties.getProperty("port")))
                .build();
    }

    @Singleton
    @Provides
    public JdbcConnectionProperties provideJdbcConnectionProperties() {
        var properties = loadProperties("jdbcConnection.properties");
        return JdbcConnectionProperties.builder()
                .suitableDriver(properties.getProperty("suitable.driver"))
                .url(properties.getProperty("url"))
                .username(properties.getProperty("username"))
                .password(properties.getProperty("password"))
                .chunkSize(Integer.parseInt(properties.getProperty("chunk.size")))
                .selectorTable(properties.getProperty("selector.table"))
                .selectorColumn(properties.getProperty("selector.column"))
                .build();
    }

    private Properties loadProperties(String filename) {
        var properties = new Properties();
        var propertiesFile = new File(filename);

        if (!propertiesFile.exists()) {
            try {
                Files.copy(CLASS_LOADER.getResourceAsStream(filename), propertiesFile.toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try (var inputStream = new FileInputStream(propertiesFile)) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return properties;
    }
}
