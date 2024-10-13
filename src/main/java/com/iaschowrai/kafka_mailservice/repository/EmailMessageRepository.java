package com.iaschowrai.kafka_mailservice.repository;

import com.iaschowrai.kafka_mailservice.entity.EmailMessageEntity;
import com.iaschowrai.kafka_mailservice.entity.EmailMessageStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmailMessageRepository extends JpaRepository<EmailMessageEntity, Long> {

    List<EmailMessageEntity> findByStatus(EmailMessageStatus emailMessageStatus);
}
