package com.iaschowrai.kafka_mailservice.repository;

import com.iaschowrai.kafka_mailservice.entity.BulkEmailMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BulkEmailMessageRepository extends JpaRepository<BulkEmailMessageEntity, Long> {


}
