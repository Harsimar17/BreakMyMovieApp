package com.breakmymovie.movie.serviceImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.breakmymovie.movie.beans.MoviesInitializer;
import com.breakmymovie.movie.models.MovieDetailsDTO;
import com.breakmymovie.movie.service.FileProcessingService_IF;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.InputFormatException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;
import ws.schild.jave.info.MultimediaInfo;

@Service
public class FileProcessingService implements FileProcessingService_IF {

	private static final String ROOT_PATH_FOR_CHUNKS_MOVIES_AND_SERIES = "C:/Users/hasingh/Desktop/movieChunks/";
	private static final String LOCAL_MOVIE_STORAGE_PATH = "C:/Users/hasingh/Desktop/mvs/";

	private String getActualMovieName(String actualMovieName) throws IOException {
		MoviesInitializer mv = new MoviesInitializer();
		List<String> returnMovies = mv.returnMovies();
		for (String movie : returnMovies) {
			if (movie.equalsIgnoreCase(actualMovieName.trim())) {
				return movie;
			}
		}
		return "";
	}

	private File[] getMovieFiles(String movieName) {
		File file = new File(LOCAL_MOVIE_STORAGE_PATH + movieName);
		return file.listFiles();
	}

	private EncodingAttributes prepareEncodingAttributes() {
		AudioAttributes audio = new AudioAttributes();
		audio.setCodec("aac");
		audio.setBitRate(128000);
		audio.setChannels(2);
		audio.setSamplingRate(44100);

		VideoAttributes video = new VideoAttributes();
		video.setCodec("h264");
		video.setBitRate(1000000);

		EncodingAttributes attrs = new EncodingAttributes();
		attrs.setAudioAttributes(audio);
		attrs.setVideoAttributes(video);
		return attrs;
	}

	private String getFileNameWithoutExtension(String fileName) {
		return fileName.substring(0, fileName.lastIndexOf("."));
	}

	private File createChunkFolderForMovie(String actualMovieName) {
		File movieFolder = new File(ROOT_PATH_FOR_CHUNKS_MOVIES_AND_SERIES + actualMovieName);
		if (!movieFolder.exists()) {
			movieFolder.mkdirs();
		}
		return movieFolder;

	}

	public List<String> encodeChunks(File videoFile, MultimediaObject multimediaObject, File movieFolder,
			String baseMovieName, int chunkDuration, int totalDurationSec, int numChunks, EncodingAttributes baseAttrs)
			throws EncoderException {

		List<String> chunkedFileNames = new ArrayList<>();
		Encoder encoder = new Encoder();

		for (int i = 0; i < numChunks; i++) {
			int startTime = i * chunkDuration;
			int duration = Math.min(chunkDuration, totalDurationSec - startTime);

			baseAttrs.setOffset((float) startTime);
			baseAttrs.setDuration((float) duration);

			String outputFileName = i + "_" + baseMovieName + ".mkv";
			chunkedFileNames.add(outputFileName);

			File targetFile = new File(movieFolder.getAbsolutePath() + File.separator + outputFileName);
			encoder.encode(multimediaObject, targetFile, baseAttrs);
		}

		return chunkedFileNames;
	}

	@Override
	public List<String> breakFileAndSaveIt(MovieDetailsDTO moviesDetails, HttpServletRequest request)
			throws InputFormatException, EncoderException, IOException {

		String movieName = getActualMovieName(moviesDetails.getSourceVideoName());
		File[] movieFiles = getMovieFiles(movieName);

		if (movieFiles == null || movieFiles.length == 0) {
			return Collections.emptyList();
		}

		File videoFile = movieFiles[0];
		int chunkDuration = moviesDetails.getPartDuration();

		MultimediaObject multimediaObject = new MultimediaObject(videoFile);
		MultimediaInfo info = multimediaObject.getInfo();
		int totalDurationSec = (int) (info.getDuration() / 1000);

		int numChunks = (int) Math.ceil((double) totalDurationSec / chunkDuration);
		EncodingAttributes encodingAttributes = prepareEncodingAttributes();

		String baseMovieName = getFileNameWithoutExtension(videoFile.getName());
		File movieFolder = createChunkFolderForMovie(moviesDetails.getSourceVideoName());

		return encodeChunks(videoFile, multimediaObject, movieFolder, baseMovieName, chunkDuration, totalDurationSec,
				numChunks, encodingAttributes);
	}

	@Override
	public void downloadChunks(File file, HttpServletResponse response) throws FileNotFoundException, IOException {
		if (!file.exists()) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		long fileLength = file.length();

		try (RandomAccessFile randomFile = new RandomAccessFile(file, "r")) {

			// Full download
			response.setContentType(Files.probeContentType(file.toPath()));
			response.setContentLengthLong(fileLength);
			response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");

			OutputStream out = response.getOutputStream();
			FileCopyUtils.copy(new FileInputStream(file), out);
		}
	}

	@Override
	public boolean deleteDirectory(File dir) {
		if (dir == null || !dir.exists())
			return false;

		File[] contents = dir.listFiles();
		if (contents != null) {
			for (File file : contents) {
				if (file.isDirectory()) {
					deleteDirectory(file); // Recursive delete
				} else {
					file.delete(); // Delete file
				}
			}
		}
		return dir.delete(); // Delete the now-empty folder
	}

	@Override
	public List<String> getAllChunks(File file) {
		// TODO Auto-generated method stub

		File[] listFiles = file.listFiles();

		List<String> chunkNames = new ArrayList<>();

		if(listFiles != null ) 
		{
			for (File f : listFiles) {

				if (!f.getName().endsWith(".png"))
					chunkNames.add(f.getName());
			}
			
		}
		return chunkNames;
	}
}
