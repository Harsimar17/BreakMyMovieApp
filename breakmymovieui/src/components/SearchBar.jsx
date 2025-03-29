import React from 'react';
import { Box, TextField, Button } from '@mui/material';

const SearchBar = ({ query, setQuery, handleSearch }) => (
  <Box display="flex" justifyContent="center" mb={2}>
    <TextField
      variant="outlined"
      label="Search for a movie..."
      value={query}
      onChange={(e) => setQuery(e.target.value)}
      style={{ marginRight: '10px', flex: 1 }}
    />
    <Button variant="contained" color="primary" onClick={handleSearch}>
      Search
    </Button>
  </Box>
);

export default SearchBar;