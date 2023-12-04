package com.albumstore.config;public class RabbitMQConfig {
    private String host;
    private String username;
    private String password;
    private String queueName;

    public RabbitMQConfig() {
        loadProperties();
    }

    private void loadProperties() {
        // Hardcoding RabbitMQ configuration
        this.host = "localhost"; // Change to your RabbitMQ server address if different
        this.username = "guest"; // Default username, change if you've set a different one
        this.password = "guest"; // Default password, change if you've set a different one
        this.queueName = "reviewQueue"; // Name of your RabbitMQ queue
    }

    // Getters
    public String getHost() {
        return host;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getQueueName() {
        return queueName;
    }
}
