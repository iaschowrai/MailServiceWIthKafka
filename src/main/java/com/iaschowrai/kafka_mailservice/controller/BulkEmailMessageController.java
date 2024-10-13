package com.iaschowrai.kafka_mailservice.controller;

import com.iaschowrai.kafka_mailservice.data.BulkEmailMessageDto;
import com.iaschowrai.kafka_mailservice.service.BulkEmailMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bulk-email-messages")
@RequiredArgsConstructor
public class BulkEmailMessageController {

    private final BulkEmailMessageService bulkemailMessageService;

    @PostMapping
    public void create(@RequestBody BulkEmailMessageDto bulkEmailMessageDto){
        bulkemailMessageService.create(bulkEmailMessageDto);

    }
}
