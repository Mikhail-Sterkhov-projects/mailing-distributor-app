package io.itzstonlex.mdapp.mailing.type;

import com.google.inject.Injector;
import com.google.inject.Provider;
import io.broadcast.engine.announcement.ContentedAnnouncement;
import io.broadcast.wrapper.jdbc.JdbcRecordMetadata;
import io.broadcast.wrapper.jdbc.JdbcRecordSelector;
import io.itzstonlex.mdapp.mailing.MailingType;
import io.itzstonlex.mdapp.properties.JdbcConnectionProperties;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.sql.DriverManager;

@RequiredArgsConstructor
public abstract class AbstractMailingType implements MailingType<ContentedAnnouncement<String>> {

    private final Provider<Injector> injectorProvider;

    @Override
    public final void broadcast(@NotNull ContentedAnnouncement<String> announcement) {
        broadcast(injectorProvider.get(), announcement);
    }

    protected abstract void broadcast(@NotNull Injector injector, @NotNull ContentedAnnouncement<String> announcement);

    @SneakyThrows
    protected final JdbcRecordSelector<String> createJdbcStringRecordSelector(Injector injector) {
        var jdbcConnectionProperties = injector.getInstance(JdbcConnectionProperties.class);

        if (jdbcConnectionProperties.getSuitableDriver() != null) {
            Class.forName(jdbcConnectionProperties.getSuitableDriver());
        }
        var connection = DriverManager.getConnection(
                jdbcConnectionProperties.getUrl(),
                jdbcConnectionProperties.getUsername(),
                jdbcConnectionProperties.getPassword());

        return new JdbcRecordSelector<>(
                JdbcRecordMetadata.builder()
                        .connection(connection)
                        .table(jdbcConnectionProperties.getSelectorTable())
                        .idColumn(jdbcConnectionProperties.getSelectorColumn())
                        .chunkSize(jdbcConnectionProperties.getChunkSize())
                        .autoCloseable(true)
                        .build());
    }
}
