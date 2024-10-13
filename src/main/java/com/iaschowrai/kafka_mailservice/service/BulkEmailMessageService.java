package com.iaschowrai.kafka_mailservice.service;


import com.iaschowrai.kafka_mailservice.data.BulkEmailMessageDto;
import com.iaschowrai.kafka_mailservice.data.EmailMessageDto;
import com.iaschowrai.kafka_mailservice.entity.BulkEmailMessageEntity;
import com.iaschowrai.kafka_mailservice.mapper.BulkEmailMessageMapper;
import com.iaschowrai.kafka_mailservice.repository.BulkEmailMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class BulkEmailMessageService {
    private final BulkEmailMessageRepository repository;
    private final BulkEmailMessageMapper mapper;
    private final EmailMessageSenderService emailMessageSenderService;

    private final EmailMessageService emailMessageService;

    public void create(BulkEmailMessageDto bulkEmailMessageDto) {

        BulkEmailMessageEntity entity = mapper.to(bulkEmailMessageDto);
        repository.save(entity);

        for (String to : bulkEmailMessageDto.getTo()) {
            EmailMessageDto emailMessageDto = new EmailMessageDto();
            emailMessageDto.setFrom(bulkEmailMessageDto.getFrom());
            emailMessageDto.setTo(to);
            emailMessageDto.setSubject(bulkEmailMessageDto.getSubject());
            emailMessageDto.setBody(bulkEmailMessageDto.getBody());
            emailMessageDto.setPriorities(bulkEmailMessageDto.getPriorities());

            emailMessageService.create(emailMessageDto);
        }
    }


}
