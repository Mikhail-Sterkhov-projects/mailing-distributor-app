package io.itzstonlex.mdapp.http.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class RESPONSE_MailingSent {

    private final String status;
}
