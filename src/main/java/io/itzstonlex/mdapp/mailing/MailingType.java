package io.itzstonlex.mdapp.mailing;

import io.broadcast.engine.announcement.Announcement;
import org.jetbrains.annotations.NotNull;

public interface MailingType<A extends Announcement> {

    String getTypeName();

    void broadcast(@NotNull A announcement);
}
