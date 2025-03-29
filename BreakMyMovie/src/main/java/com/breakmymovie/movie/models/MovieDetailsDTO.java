package com.breakmymovie.movie.models;

public class MovieDetailsDTO {

	String sourceVideoName;

	int partDuration = 600;

	public String getSourceVideoName() {
		return sourceVideoName;
	}

	public void setSourceVideoName(String sourceVideoName) {
		this.sourceVideoName = sourceVideoName;
	}

	public int getPartDuration() {
		return partDuration;
	}

	public void setPartDuration(int partDuration) {
		this.partDuration = partDuration;
	}

}
