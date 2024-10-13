package com.iaschowrai.kafka_mailservice.service;


import com.iaschowrai.kafka_mailservice.data.EmailMessageDto;
import com.iaschowrai.kafka_mailservice.entity.EmailMessageEntity;
import com.iaschowrai.kafka_mailservice.entity.EmailMessageStatus;
import com.iaschowrai.kafka_mailservice.mapper.EmailMessageMapper;
import com.iaschowrai.kafka_mailservice.repository.EmailMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class EmailMessageService {
    private final EmailMessageRepository repository;
    private final EmailMessageMapper mapper;

    private final KafkaTemplate<Long, Long> kafkaTemplate;

    public void create(EmailMessageDto emailMessageDto) {
        EmailMessageEntity entity = mapper.to(emailMessageDto);
        entity.setStatus(EmailMessageStatus.PENDING);

        repository.save(entity);

        kafkaTemplate.send("emailMessageTopic", entity.getId());
    }
}
