package io.itzstonlex.mdapp.mailing.type;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import io.broadcast.engine.BroadcastEngine;
import io.broadcast.engine.BroadcastPipeline;
import io.broadcast.engine.announcement.AnnouncementExtractor;
import io.broadcast.engine.announcement.ContentedAnnouncement;
import io.broadcast.engine.event.BroadcastListener;
import io.broadcast.engine.record.extract.RecordExtractor;
import io.broadcast.wrapper.smtp.MailCredentials;
import io.broadcast.wrapper.smtp.SMTPBroadcastDispatcher;
import io.broadcast.wrapper.smtp.SMTPMetadata;
import io.itzstonlex.mdapp.properties.EmailMetadataProperties;
import org.jetbrains.annotations.NotNull;

public class EmailType extends AbstractMailingType {

    @Inject
    private EmailMetadataProperties emailMetadataProperties;

    public EmailType(Provider<Injector> injectorProvider) {
        super(injectorProvider);
    }

    @Override
    public String getTypeName() {
        return "email";
    }

    @Override
    public void broadcast(@NotNull Injector injector, @NotNull ContentedAnnouncement<String> announcement) {
        var pipeline = generateEMailPipeline(injector, announcement);
        var broadcastEngine = new BroadcastEngine(pipeline);

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

        var recordExtractor = RecordExtractor.chunkyParallel(createJdbcStringRecordSelector(injector));
        var announcementExtractor = AnnouncementExtractor.constant(announcement);

        return BroadcastPipeline.createContentedPipeline(String.class, String.class)
                .setDispatcher(new SMTPBroadcastDispatcher(smtpMetadata))
                .setRecordExtractor(recordExtractor)
                .setAnnouncementExtractor(announcementExtractor)
                .addListener(BroadcastListener.stdout());
    }
}
