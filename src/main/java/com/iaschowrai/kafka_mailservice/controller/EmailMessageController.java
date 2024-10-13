package com.iaschowrai.kafka_mailservice.controller;

import com.iaschowrai.kafka_mailservice.data.EmailMessageDto;
import com.iaschowrai.kafka_mailservice.service.EmailMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/email-messages")
@RequiredArgsConstructor
public class EmailMessageController {

    private final EmailMessageService emailMessageService;

    @PostMapping
    public void create(@RequestBody EmailMessageDto EmailMessageDto){
        emailMessageService.create(EmailMessageDto);

    }
}
