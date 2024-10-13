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
import java.util.concurrent.TimeUnit;

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


    // Semaphore to limit the number of concurrent email sending tasks (max 1000 for each priority level)
    private final Semaphore semaphoreLow = new Semaphore(1000);
    private final Semaphore semaphoreMedium = new Semaphore(1000);
    private final Semaphore semaphoreHigh = new Semaphore(1000);

    // Listens to the Kafka topic for low priority email message IDs
    @KafkaListener(topics = "emailMessageTopicLow", groupId = "emailMessageTopic")
    public void emailMessageTopicLow(Long messageId) throws InterruptedException {
        semaphoreLow.acquire(); // Acquire a permit from the semaphore
        sendEmailMessage(messageId, semaphoreLow); // Send the email
    }

    // Listens to the Kafka topic for medium priority email message IDs
    @KafkaListener(topics = "emailMessageTopicMedium", groupId = "emailMessageTopic")
    public void emailMessageTopicMedium(Long messageId) throws InterruptedException {
        semaphoreMedium.acquire(); // Acquire a permit from the semaphore
        sendEmailMessage(messageId, semaphoreMedium); // Send the email
    }

    // Listens to the Kafka topic for high priority email message IDs
    @KafkaListener(topics = "emailMessageTopicHigh", groupId = "emailMessageTopic")
    public void emailMessageTopicHigh(Long messageId) throws InterruptedException {
        semaphoreHigh.acquire(); // Acquire a permit from the semaphore
        sendEmailMessage(messageId, semaphoreHigh); // Send the email
    }

    // Method to send the email message
    private void sendEmailMessage(Long messageId, Semaphore semaphore) throws InterruptedException {
        // Retrieve email message entity from the database
        EmailMessageEntity entity = repository.findById(messageId).orElseThrow(() -> {
            log.error("EmailMessageEntity not found for ID: {}", messageId);
            return new RuntimeException("Email message not found");
        });

        EmailMessageDto dto = mapper.from(entity); // Convert entity to DTO

        // Submit the email sending task to the executor service
        executorService.submit(() -> {
            try {
                // Send the email using the email sender service
                emailMessageSenderService.sendEmail(dto);
                // Update the status to SENT if successful
                entity.setStatus(EmailMessageStatus.SENT);
            } catch (Exception e) {
                // Log the error and update the status to FAILED if an exception occurs
                log.error("Failed to send email for ID: {}. Error: {}", messageId, e.getMessage(), e);
                entity.setStatus(EmailMessageStatus.FAILED);
            } finally {
                // Save the updated entity status back to the repository and release the semaphore permit
                repository.save(entity);
                semaphore.release(); // Release the semaphore permit
            }
        });
    }

    // Optional: Add a method to shut down the executor service gracefully
    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException ex) {
            executorService.shutdownNow();
        }
    }
}
