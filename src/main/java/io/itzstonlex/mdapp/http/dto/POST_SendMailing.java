package io.itzstonlex.mdapp.http.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class POST_SendMailing {

    private final String type;
    private final String subject;
    private final String text;
}
