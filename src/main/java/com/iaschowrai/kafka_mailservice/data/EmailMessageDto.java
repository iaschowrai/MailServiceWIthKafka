package com.iaschowrai.kafka_mailservice.data;


import lombok.Data;

@Data
public class EmailMessageDto {

    private String from;
    private String to;
    private String subject;
    private String body;
}
