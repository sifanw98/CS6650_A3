package com.albumstore.util;

import com.albumstore.bean.Review;
import com.google.gson.Gson;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class ReviewConsumer {
    public static void main(String[] argv) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("ec2-54-191-1-166.us-west-2.compute.amazonaws.com"); // EC2 instance public DNS/IP
        factory.setUsername("guest"); // Your RabbitMQ username
        factory.setPassword("guest"); // Your RabbitMQ password

        Connection connection = null;
        Channel channel = null;

        try {
            connection = factory.newConnection();
            channel = connection.createChannel();

            String queueName = "reviewQueue"; // The name of the queue
            Map<String, Object> args = new HashMap<>();
            args.put("x-max-length", 4000); // Set your desired max length
            args.put("x-overflow", "drop-head"); // Drop the oldest messages when the limit is reached
            channel.queueDeclare(queueName, true, false, false, args);

            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                Review review = new Gson().fromJson(message, Review.class);

                try {
                    // Process the message
                    DatabaseUtil.saveReview(review);
                    System.out.println(" [x] Received and processed '" + message + "'");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });

            // Keep the application running
            Thread.sleep(Long.MAX_VALUE);

        } catch (IOException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                if (channel != null && channel.isOpen()) {
                    channel.close();
                }
                if (connection != null && connection.isOpen()) {
                    connection.close();
                }
            } catch (IOException | TimeoutException e) {
                e.printStackTrace();
            }
        }
    }
}
