import React, { useState } from 'react';
import { Container, Typography, Box } from '@mui/material';
import SearchBar from './components/SearchBar';
import MovieGrid from './components/MovieGrid';
import { fetchMovies, getMovieChunks } from './services/MovieService';

const App = () => {
  const [query, setQuery] = useState('');
  const [movies, setMovies] = useState([]);
  const [movieChunks, setMovieChunks] = useState([]);
  const [parts, setParts] = useState(1);

  const handleSearch = async () => {
    const response = await fetchMovies(query);
    console.log(response);

    setMovies(response);
  };

  const handleDownload = (movie) => {
    console.log(`Downloading ${JSON.stringify(Object.keys(movie)[0])}`);
    getMovieChunks(movie).then((data) => {
      setMovieChunks(data)

    })
  };

  const handleBreakIntoParts = (movie) => {
    console.log(`Breaking ${movie.title} into ${parts} parts`);
  };

  return (
    <Container maxWidth="lg" style={{ marginTop: '20px' }}>
      <Typography variant="h4" align="center" gutterBottom>
        Movie Search
     
      </Typography>
      <SearchBar
        query={query}
        setQuery={setQuery}
        handleSearch={handleSearch}
      />
      <MovieGrid
        movies={movies}
        parts={movieChunks}
        setParts={setParts}
        handleDownload={handleDownload}
        handleBreakIntoParts={handleBreakIntoParts}
      />
    </Container>
  );
};

export default App;