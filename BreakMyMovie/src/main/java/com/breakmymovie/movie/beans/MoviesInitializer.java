package com.breakmymovie.movie.beans;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class MoviesInitializer {

	private static final String MOVIES_STORAGE_LOCAL = "C:/Users/hasingh/Desktop/mvs/";
	static List<String> movieNames = new ArrayList<>();

	@Bean
	public List<String> getMovies() throws IOException {
		File movies = new File(MOVIES_STORAGE_LOCAL);

		File[] listFiles = movies.listFiles();

		for (File f : listFiles) {
			movieNames.add(f.getName());
		}

		return movieNames;
	}

	public List<String> returnMovies() {
		return movieNames;
	}
}
