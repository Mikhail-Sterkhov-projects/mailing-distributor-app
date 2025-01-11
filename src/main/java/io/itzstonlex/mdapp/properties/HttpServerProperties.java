package io.itzstonlex.mdapp.properties;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@RequiredArgsConstructor
public class HttpServerProperties {

    private final String host;
    private final int port;
}
