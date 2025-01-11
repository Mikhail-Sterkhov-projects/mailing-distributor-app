package io.itzstonlex.mdapp.http;

import com.jrest.mvc.model.Content;
import com.jrest.mvc.model.HttpRequest;
import com.jrest.mvc.model.HttpResponse;
import com.jrest.mvc.persistence.HttpPost;
import com.jrest.mvc.persistence.HttpServer;
import io.broadcast.engine.announcement.ContentedAnnouncement;
import io.itzstonlex.mdapp.http.dto.POST_SendMailing;
import io.itzstonlex.mdapp.http.dto.RESPONSE_MailingSent;
import io.itzstonlex.mdapp.mailing.MailingService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@HttpServer
@RequiredArgsConstructor
public class MailingHttpRequestHandler {

    private final MailingService mailingService;

    @HttpPost("/sendMailing")
    public HttpResponse postMailing(@NotNull HttpRequest httpRequest) {
        var payload = httpRequest.getContent().fromJson(POST_SendMailing.class);
        var isSent = sendMailing(payload);

        Content content;
        if (isSent) {
            content = Content.fromEntityJson(new RESPONSE_MailingSent("success"));
        } else {
            content = Content.fromEntityJson(new RESPONSE_MailingSent("unknown type"));
        }
        return isSent ? HttpResponse.ok(content) : HttpResponse.badRequest(content);
    }

    private boolean sendMailing(POST_SendMailing payload) {
        var payloadType = payload.getType();

        if (mailingService.get(payloadType) == null) {
            return false;
        }

        var announcement = new ContentedAnnouncement<>(
                payload.getSubject(),
                payload.getText()
        );

        mailingService.callBroadcast(payloadType, announcement);
        return true;
    }
}
