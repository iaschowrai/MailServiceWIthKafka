package com.iaschowrai.kafka_mailservice.mapper;

import com.iaschowrai.kafka_mailservice.data.BulkEmailMessageDto;
import com.iaschowrai.kafka_mailservice.entity.BulkEmailMessageEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BulkEmailMessageMapper {

     BulkEmailMessageDto from(BulkEmailMessageEntity entity);

     BulkEmailMessageEntity to(BulkEmailMessageDto entity);
}

