package com.breakmymovie.movie.rabbitmq;

import java.io.IOException;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.breakmymovie.movie.models.MovieDetailsDTO;
import com.breakmymovie.movie.service.FileProcessingService_IF;

import ws.schild.jave.EncoderException;
import ws.schild.jave.InputFormatException;

@Component
public class MessageQueueConsumer {

	@Autowired
	FileProcessingService_IF fileProcessingService;

	@RabbitListener(queues = "movie-break-queue")
	public void receiveMessage(MovieDetailsDTO moviesDetails) throws InputFormatException, EncoderException, IOException {
		// Handle the received message here

		fileProcessingService.breakFileAndSaveIt(moviesDetails, null);
	}
}
