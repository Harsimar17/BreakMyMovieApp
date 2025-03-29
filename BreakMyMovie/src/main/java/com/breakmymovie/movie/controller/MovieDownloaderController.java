package com.breakmymovie.movie.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.breakmymovie.movie.beans.MoviesInitializer;
import com.breakmymovie.movie.models.MovieDetailsDTO;
import com.breakmymovie.movie.rabbitmq.MessageQueueProducer;
import com.breakmymovie.movie.service.FileProcessingService_IF;
import org.springframework.web.bind.annotation.CrossOrigin;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ws.schild.jave.EncoderException;
import ws.schild.jave.InputFormatException;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class MovieDownloaderController {

	@Autowired
	FileProcessingService_IF fileProcessingService;

	@Autowired
	MessageQueueProducer messageQueueProducer;

	@Autowired
	MoviesInitializer mv;

	@PostMapping("/searchMovie")
	public List<String> searchMovie(@RequestBody MovieDetailsDTO moviesDetails) {
		List<String> returnMovies = mv.returnMovies();

		if (moviesDetails.getSourceVideoName() == null || moviesDetails.getSourceVideoName().isEmpty()) {
			return returnMovies;
		}

		for (String movie : returnMovies) {
			if (movie.toLowerCase().contains(moviesDetails.getSourceVideoName().toLowerCase()))
				return Arrays.asList(movie);
			;
		}

		return null;
	}

	@GetMapping("/getAllChunksForMovie/{movieName}")
	public List<String> getMovieChunks(@PathVariable("movieName") String movieName, HttpServletResponse response)
			throws FileNotFoundException, IOException {

		File file = new File("C:/Users/hasingh/Desktop/movieChunks/" + File.separator + movieName);

		List<String> allChunks = fileProcessingService.getAllChunks(file);

		return allChunks;

	}

	@PostMapping("/downloadChunk")
	public void downloadChunks(@RequestBody Map<String, String> chunkDetails, HttpServletResponse response)
			throws FileNotFoundException, IOException {

		File file = new File("C:/Users/hasingh/Desktop/movieChunks/" + File.separator + chunkDetails.get("movieName")
				+ File.separator + chunkDetails.get("movieChunk"));

		fileProcessingService.downloadChunks(file, response);

	}

	@GetMapping("/deleteMovie/{movieName}")
	public void deleteChunkFolder(@PathVariable("movieName") String movieChunkedName) {
		File movie = new File("C:/Users/hasingh/Desktop/movieChunks/" + File.separator + movieChunkedName);

		fileProcessingService.deleteDirectory(movie);
	}

	@PostMapping("/splitFiles/")
	public void splitFile(@RequestBody MovieDetailsDTO moviesDetails, HttpServletRequest request)
			throws IOException, IllegalArgumentException, InputFormatException, EncoderException {
		messageQueueProducer.sendMessage(moviesDetails, request);

	}
}
