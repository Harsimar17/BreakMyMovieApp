import React, { useEffect } from 'react';
import { Grid, Typography } from '@mui/material';
import MovieCard from './MovieCard'; // Adjust this import if needed

const MovieGrid = ({ movies, parts, setParts, handleDownload, handleBreakIntoParts }) => {
  
  useEffect(() => {
    console.log('Movies received:', movies);
    console.log('Parts state:', parts);
  }, [movies, parts]);

  return (
    <Grid container spacing={2}>
      {movies ? (
        movies.map((movie) => (
          <div>
          <MovieCard
            movie={movie}
            handleDownload={handleDownload}
            parts = {parts}
          /></div>
        ))
      ) : (
        <Grid item xs={12}>
          <Typography variant="h6" align="center">
            No movies found.
          </Typography>
        </Grid>
      )}
    </Grid>
  );
};

export default MovieGrid;
