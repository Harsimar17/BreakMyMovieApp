package com.breakmymovie.movie.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.breakmymovie.movie.beans.MoviesInitializer;
import com.breakmymovie.movie.models.MovieDetailsDTO;
import com.breakmymovie.movie.service.FileProcessingService_IF;
import com.breakmymovie.movie.serviceImpl.FileProcessingService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ws.schild.jave.EncoderException;
import ws.schild.jave.InputFormatException;

@RestController
public class MovieDownloaderController {

	@Autowired
	FileProcessingService_IF fileProcessingService;
	
	@Autowired
	MoviesInitializer mv;

	@GetMapping("/searchMovie/{movieName}")
	public String searchMovie(@PathVariable("movieName") String movieName) {
		List<String> returnMovies = mv.returnMovies();

		for (String movie : returnMovies) {
			if (movie.toLowerCase().contains(movieName.toLowerCase()))
				return movie;
		}

		return "No movie present with this name";
	}

	@GetMapping("/downloadChunk/{movieName}/{movieChunk}")
	public void downloadChunks(@PathVariable("movieChunk") String movieChunk,
			@PathVariable("movieName") String movieName, HttpServletResponse response)
			throws FileNotFoundException, IOException {

		File file = new File(
				"C:/Users/hasingh/Desktop/movieChunks/" + File.separator + movieName + File.separator + movieChunk);

		fileProcessingService.downloadChunks(file, response);

	}

	@GetMapping("/deleteMovie/{movieName}")
	public void deleteChunkFolder(@PathVariable("movieName") String movieChunkedName) {
		File movie = new File("C:/Users/hasingh/Desktop/movieChunks/" + File.separator + movieChunkedName);

		fileProcessingService.deleteDirectory(movie);
	}

	@GetMapping("/splitFiles/")
	public CompletableFuture<Void> splitFile(@RequestBody MovieDetailsDTO moviesDetails, HttpServletRequest request)
			throws IOException, IllegalArgumentException, InputFormatException, EncoderException {

		CompletableFuture<Void> runAsync = CompletableFuture.runAsync(() -> {
			try {
				fileProcessingService.breakFileAndSaveIt(moviesDetails.getSourceVideoName(), request);
			} catch (Exception e) {
				System.out.println(e);
			}
		});

		return runAsync;

	}
}
