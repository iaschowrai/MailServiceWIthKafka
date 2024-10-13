package com.iaschowrai.kafka_mailservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class BulkEmailMessageEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "\"from\"")
    private String from;

    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "\"to\"",joinColumns = @JoinColumn(name = "bulk_email_message_id"))
    @Column(name = "\"to\"")
    private List<String> to;
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String body;

}
