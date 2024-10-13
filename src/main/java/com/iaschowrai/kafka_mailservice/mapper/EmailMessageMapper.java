package com.iaschowrai.kafka_mailservice.mapper;

import com.iaschowrai.kafka_mailservice.data.EmailMessageDto;
import com.iaschowrai.kafka_mailservice.entity.EmailMessageEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmailMessageMapper {

     EmailMessageDto from(EmailMessageEntity entity);

     EmailMessageEntity to(EmailMessageDto entity);
}


//@Component
//public class EmailMessageMapper {
//     public EmailMessageDto from(EmailMessageEntity entity) {
//          if (entity == null) {
//               return null;
//          }
//          EmailMessageDto dto = new EmailMessageDto();
//          dto.setId(entity.getId());
//          dto.setSubject(entity.getSubject());
//          dto.setBody(entity.getBody());
//          // Map other fields similarly
//          return dto;
//     }
//
//     public EmailMessageEntity to(EmailMessageDto dto) {
//          if (dto == null) {
//               return null;
//          }
//          EmailMessageEntity entity = new EmailMessageEntity();
//          entity.setId(dto.getId());
//          entity.setSubject(dto.getSubject());
//          entity.setBody(dto.getBody());
//          // Map other fields similarly
//          return entity;
//     }
//}
