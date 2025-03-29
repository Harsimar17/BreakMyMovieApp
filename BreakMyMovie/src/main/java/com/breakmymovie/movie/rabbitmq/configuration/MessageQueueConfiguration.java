package com.breakmymovie.movie.rabbitmq.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

@Configuration
public class MessageQueueConfiguration {
	@Bean
	public Queue queue() {
		return new Queue("movie-break-queue", false);
	}

	@Bean
	public Exchange exchange() {
		return new DirectExchange("data-exchange");
	}

	@Bean
	public MessageConverter jsonConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	public Binding binding(Queue queue, Exchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with("123").noargs();
	}
}
