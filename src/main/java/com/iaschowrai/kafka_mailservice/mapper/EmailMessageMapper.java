package com.iaschowrai.kafka_mailservice.mapper;

import com.iaschowrai.kafka_mailservice.data.EmailMessageDto;
import com.iaschowrai.kafka_mailservice.entity.EmailMessageEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmailMessageMapper {

     EmailMessageDto from(EmailMessageEntity entity);

     EmailMessageEntity to(EmailMessageDto entity);
}

