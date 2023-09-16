package com.infomate.chat.dto;


import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SessionDTO {

    int membercode;

    String sessionId;

    String JWT;
}
