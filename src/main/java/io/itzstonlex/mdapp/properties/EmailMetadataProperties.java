package io.itzstonlex.mdapp.properties;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@RequiredArgsConstructor
public class EmailMetadataProperties {

    private final String credentialsUsername;
    private final String credentialsEmail;
    private final String credentialsPassword;

    private final String smtpHost;
    private final String smtpPort;
}
