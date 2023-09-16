package com.infomate.chat.dto;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class TokenDTO {

    private String jwt;

    private String pk;

    private String sessionId;

    private String username;

    private LocalDateTime expire;
}
