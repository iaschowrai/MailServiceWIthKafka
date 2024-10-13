package com.iaschowrai.kafka_mailservice.data;


import com.iaschowrai.kafka_mailservice.entity.EmailMessagePriorities;
import lombok.Data;

import java.util.List;

@Data
public class BulkEmailMessageDto {

    private String from;
    private List<String> to;
    private String subject;
    private String body;

    private EmailMessagePriorities priorities;
}
