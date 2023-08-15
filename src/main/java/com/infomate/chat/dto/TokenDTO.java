package com.infomate.chat.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class TokenDTO {

    private String jwt;

    private String pk;

    private String username;

    private LocalDateTime expire;
}
