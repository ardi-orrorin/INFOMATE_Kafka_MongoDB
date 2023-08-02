package com.infomate.common;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ResponseDTO {

    private int statusCode;

    private String message;

    private Object data;
}
