package com.hasithat.kafkaproducer.controller;


import com.hasithat.kafka.dto.PaymentRequest;
import com.hasithat.kafka.dto.ProducerRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.UUID;

@RestController
public class ProducerController {

    @Value("${test.topic.name}")
    private String testTopicName;

    @Value("${payment.topic.name}")
    private String paymentTopicName;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @GetMapping("/publish/{message}")
    public void sendMessage(@PathVariable String message) {
        kafkaTemplate.send(testTopicName, message);
    }

    @PostMapping("/payment")
    public String doPayment(@RequestBody ProducerRequest<PaymentRequest> paytmRequest) {
        PaymentRequest paymentRequest = paytmRequest.getPayload();
        paymentRequest.setTransactionId(UUID.randomUUID().toString());
        paymentRequest.setTxDate(new Date());
        kafkaTemplate.send(paymentTopicName, paymentRequest);
        return "payment instantiate successfully...";
    }
}
