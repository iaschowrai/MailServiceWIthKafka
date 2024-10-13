package com.iaschowrai.kafka_mailservice.data;


import com.iaschowrai.kafka_mailservice.entity.EmailMessagePriorities;
import lombok.Data;

@Data
public class EmailMessageDto {

    private String from;
    private String to;
    private String subject;
    private String body;

    private EmailMessagePriorities priorities;
}
