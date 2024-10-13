package com.iaschowrai.kafka_mailservice.service;

import com.iaschowrai.kafka_mailservice.data.EmailMessageDto;
import com.iaschowrai.kafka_mailservice.entity.EmailMessageEntity;
import com.iaschowrai.kafka_mailservice.entity.EmailMessageStatus;
import com.iaschowrai.kafka_mailservice.mapper.EmailMessageMapper;
import com.iaschowrai.kafka_mailservice.repository.EmailMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailMessageKafkaScheduler {
    // Inject dependencies: repository, email sender service, and mapper
    private final EmailMessageRepository repository;
    private final EmailMessageSenderService emailMessageSenderService;
    private final EmailMessageMapper mapper;

    // ExecutorService for handling concurrent email sending tasks with a thread pool size of 20
    private final ExecutorService executorService = Executors.newFixedThreadPool(20);

    // Semaphore to limit the number of concurrent email sending tasks (max 1000)
    private final Semaphore semaphore = new Semaphore(1000);


    @KafkaListener(topics = "emailMessageTopic" , groupId = "emailMessageTopic")
    public void listenKafkaTopicConfig(Long messageId) throws InterruptedException{

        EmailMessageEntity entity = repository.findById(messageId).orElseThrow();
        EmailMessageDto dto = mapper.from(entity);
        semaphore.acquire();
        executorService.submit(() -> {
            try {
                // Send the email using the email sender service
                emailMessageSenderService.sendEmail(dto);
                // Update the status to SENT if successful
                entity.setStatus(EmailMessageStatus.SENT);
            } catch (Exception e) {
                // Log the error and update the status to FAILED if an exception occurs
                log.error(e.getMessage(), e);
                entity.setStatus(EmailMessageStatus.FAILED);
            } finally {
                // Save the updated entity status back to the repository and release the semaphore permit
                repository.save(entity);
                semaphore.release();
            }
        });
    }
}
