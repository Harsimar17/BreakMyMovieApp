package com.breakmymovie.movie.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.breakmymovie.movie.models.MovieDetailsDTO;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class MessageQueueProducer {
	@Autowired
	private RabbitTemplate rabbitTemplate;

	public void sendMessage(MovieDetailsDTO moviesDetails, HttpServletRequest request) {
		rabbitTemplate.convertAndSend("data-exchange", "123", moviesDetails);
	}
}
