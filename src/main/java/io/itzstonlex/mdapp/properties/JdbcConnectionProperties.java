package io.itzstonlex.mdapp.properties;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@RequiredArgsConstructor
public class JdbcConnectionProperties {

    private final String suitableDriver;
    private final String url;
    private final String username;
    private final String password;

    private final int chunkSize;

    private final String selectorTable;
    private final String selectorColumn;
}
