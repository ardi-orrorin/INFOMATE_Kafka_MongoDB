package com.infomate.chat.dto;

import lombok.*;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class MessageDTO {

    private int sender;

    private List<Integer> receiveList;

    private int chatRoomNo;

    private String subject;

    private Object message;

    private String url;

    private LocalDateTime createDate;




}
