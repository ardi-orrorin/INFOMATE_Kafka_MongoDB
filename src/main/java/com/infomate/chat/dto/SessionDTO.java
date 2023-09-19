package com.infomate.chat.dto;


import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SessionDTO {

    private int membercode;

    private String memberName;

    private String sessionId;

    private String JWT;
}
