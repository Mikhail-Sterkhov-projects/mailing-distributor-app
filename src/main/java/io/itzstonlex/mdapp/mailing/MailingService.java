package io.itzstonlex.mdapp.mailing;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import io.broadcast.engine.announcement.Announcement;
import io.itzstonlex.mdapp.mailing.type.EmailType;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public final class MailingService {

    private final Provider<Injector> injectorProvider;
    private final Map<String, MailingType<?>> cache = new HashMap<>();

    @Inject
    void postConstruct() {
        mappingType(new EmailType(injectorProvider));
    }

    private void mappingType(MailingType<?> mailingType) {
        injectorProvider.get().injectMembers(mailingType);
        cache.put(mailingType.getTypeName().toLowerCase(), mailingType);
    }

    public MailingType<?> get(@NotNull String typeName) {
        return cache.get(typeName.toLowerCase());
    }

    public <A extends Announcement> void callBroadcast(@NotNull String typeName, @NotNull A announcement) {
        @SuppressWarnings("unchecked")
        MailingType<A> mailingType = (MailingType<A>) get(typeName);

        if (mailingType != null) {
            System.out.printf("[*] \"%s\": %s%n", mailingType.getTypeName(), announcement);

            mailingType.broadcast(announcement);
        }
    }
}
