package com.infomate.chat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import reactor.core.publisher.Flux;

import java.awt.print.Printable;
import java.time.LocalDateTime;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class MessageDTO {

    private String header;

    private int sender;

    private int chatRoomNo;

    private Object message;

    private boolean isRead;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createDate;

}
