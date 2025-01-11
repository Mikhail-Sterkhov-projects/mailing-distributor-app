package io.itzstonlex.mdapp.mailing.type;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import io.broadcast.engine.BroadcastEngine;
import io.broadcast.engine.BroadcastPipeline;
import io.broadcast.engine.announcement.AnnouncementExtractor;
import io.broadcast.engine.announcement.ContentedAnnouncement;
import io.broadcast.engine.record.extract.RecordExtractor;
import io.broadcast.wrapper.smtp.MailCredentials;
import io.broadcast.wrapper.smtp.SMTPBroadcastDispatcher;
import io.broadcast.wrapper.smtp.SMTPMetadata;
import io.itzstonlex.mdapp.properties.EmailMetadataProperties;
import org.jetbrains.annotations.NotNull;

public class EmailType extends AbstractMailingType {

    private static final boolean CLOSE_CONNECTION_AFTER_QUERY_FLAG = false;

    @Inject
    private EmailMetadataProperties emailMetadataProperties;

    private BroadcastEngine broadcastEngine;

    public EmailType(Provider<Injector> injectorProvider) {
        super(injectorProvider);
    }

    @Override
    public String getTypeName() {
        return "email";
    }

    @Override
    public void broadcast(@NotNull Injector injector, @NotNull ContentedAnnouncement<String> announcement) {
        if (broadcastEngine == null) {
            broadcastEngine = new BroadcastEngine(generateEMailPipeline(injector, announcement));
        }

        broadcastEngine.broadcastNow();
    }

    private BroadcastPipeline<String, ContentedAnnouncement<String>> generateEMailPipeline(Injector injector,
                                                                                           ContentedAnnouncement<String> announcement) {
        var smtpMetadata = SMTPMetadata.builder()
                .senderCredentials(MailCredentials.builder()
                        .username(emailMetadataProperties.getCredentialsUsername())
                        .email(emailMetadataProperties.getCredentialsEmail())
                        .password(emailMetadataProperties.getCredentialsPassword())
                        .build())
                .smtpHost(emailMetadataProperties.getSmtpHost())
                .smtpPort(emailMetadataProperties.getSmtpPort())
                .build();

        var recordExtractor = RecordExtractor.chunkyParallel(createJdbcStringRecordSelector(injector, CLOSE_CONNECTION_AFTER_QUERY_FLAG));
        var announcementExtractor = AnnouncementExtractor.constant(announcement);

        return BroadcastPipeline.createContentedPipeline(String.class, String.class)
                .setDispatcher(new SMTPBroadcastDispatcher(smtpMetadata))
                .setRecordExtractor(recordExtractor)
                .setAnnouncementExtractor(announcementExtractor);
    }
}
