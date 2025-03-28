package com.breakmymovie.movie.beans;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class MoviesInitializer {

	private static final String MOVIES_STORAGE_LOCAL = "C:/Users/hasingh/Desktop/mvs/";
	static List<String> movieNames = new ArrayList<>();

	@Bean
	public List<String> getMovies() {
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
