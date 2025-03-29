// src/services/movieService.js
import axios from "axios";
export const BASE_URL = "http://localhost:8080";
export const fetchMovies = async (query) => {
  try {
    const response = await axios.post(`${BASE_URL}/searchMovie`, {
      sourceVideoName: query,
    });

    return response.data;
  } catch (error) {
    console.error("Failed to fetch movies", error);
    return [];
  }
};

export const getMovieChunks = async (movieName) => {
  console.log(BASE_URL);
  
  return axios
    .get(`${BASE_URL}/getAllChunksForMovie/${movieName}`)
    .then((data) => {
      return data.data;
    });
};
