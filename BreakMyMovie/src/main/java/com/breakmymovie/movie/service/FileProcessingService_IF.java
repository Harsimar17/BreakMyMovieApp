package com.breakmymovie.movie.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.breakmymovie.movie.models.MovieDetailsDTO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ws.schild.jave.EncoderException;
import ws.schild.jave.InputFormatException;

public interface FileProcessingService_IF {
	public void downloadChunks(File file, HttpServletResponse response) throws FileNotFoundException, IOException;

	public boolean deleteDirectory(File dir);

	public List<String> breakFileAndSaveIt(MovieDetailsDTO moviesDetails, HttpServletRequest request)
			throws InputFormatException, EncoderException, IOException;
	
	public List<String> getAllChunks(File file);
}
