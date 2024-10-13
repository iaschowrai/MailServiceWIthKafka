package com.iaschowrai.kafka_mailservice.service;

import com.iaschowrai.kafka_mailservice.data.EmailMessageDto;
import com.iaschowrai.kafka_mailservice.entity.EmailMessageEntity;
import com.iaschowrai.kafka_mailservice.entity.EmailMessageStatus;
import com.iaschowrai.kafka_mailservice.mapper.EmailMessageMapper;
import com.iaschowrai.kafka_mailservice.repository.EmailMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

//@Service
@RequiredArgsConstructor
@Slf4j
public class EmailMessageSchedulerService {

    // Inject dependencies: repository, email sender service, and mapper
    private final EmailMessageRepository repository;
    private final EmailMessageSenderService emailMessageSenderService;
    private final EmailMessageMapper mapper;

    // ExecutorService for handling concurrent email sending tasks with a thread pool size of 20
    private final ExecutorService executorService = Executors.newFixedThreadPool(20);

    // Semaphore to limit the number of concurrent email sending tasks (max 1000)
    private final Semaphore semaphore = new Semaphore(1000);

    // Scheduled method that runs every 1000 milliseconds (1 second) to process email messages
    @Scheduled(fixedDelay = 1000)
    public void schedule() {
        try {
            // Retrieve all email messages with status PENDING from the repository
            List<EmailMessageEntity> messageEntities = repository.findByStatus(EmailMessageStatus.PENDING);

            // Update the status of all retrieved email messages to SENDING and save them back to the repository
            for (EmailMessageEntity entity : messageEntities) {
                entity.setStatus(EmailMessageStatus.SENDING);
                repository.save(entity);
            }

            // Process each email message in the list
            for (EmailMessageEntity entity : messageEntities) {
                entity.setStatus(EmailMessageStatus.SENDING);

                // Convert the entity to a DTO for sending the email
                EmailMessageDto dto = mapper.from(entity);

                // Acquire a permit from the semaphore before sending the email
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
        } catch (Exception e) {
            // Log any errors that occur during the scheduling process
            log.error(e.getMessage(), e);
        }
    }
}
