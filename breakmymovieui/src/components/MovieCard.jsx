import React, { useState } from "react";
import {
  Card,
  CardMedia,
  CardContent,
  Typography,
  Accordion,
  AccordionSummary,
  AccordionDetails,
  Box,
  IconButton,
  Link,
  Button,
  Alert,
  Snackbar,
} from "@mui/material";
import { styled } from "@mui/system";
import { FaChevronDown, FaDownload } from "react-icons/fa";
import axios from "axios";
import { breakMovieAsync, getMovieChunks } from "../services/MovieService";

// Styled components
const StyledCard = styled(Card)(({ theme }) => ({
  maxWidth: {
    xs: "100%",
    sm: "550px",
    md: "600px",
  },
  margin: "20px auto",
  transition: "all 0.3s ease",
  "&:hover": {
    transform: "translateY(-5px)",
    boxShadow: "0 8px 20px rgba(0,0,0,0.12)",
  },
}));

const StyledAccordion = styled(Accordion)({
  "&:before": {
    display: "none",
  },
  boxShadow: "none",
  borderRadius: "8px",
  marginBottom: "8px",
});

const LinkItem = styled(Link)({
  display: "flex",
  alignItems: "center",
  padding: "8px",
  textDecoration: "none",
  color: "inherit",
  borderRadius: "4px",
  transition: "background-color 0.2s",
  "&:hover": {
    backgroundColor: "rgba(0,0,0,0.04)",
  },
});

// Main component
const MovieCard = ({ movie, parts, setParts, handleBreakIntoParts }) => {
  const [movieChunks, setMovieChunks] = useState([]);
  const [open, setOpen] = React.useState(false);

  const handleDownload = async (movie) => {
    const data = await getMovieChunks(movie);
    setMovieChunks(data);
  };

  const handleDownloadPart = async (url, body = {}) => {
    const response = await axios.post(url, body, {
      responseType: "arraybuffer",
      headers: {
        "Content-Type": "application/json",
        Accept: "video/mkv",
      },
    });

    const blob = new Blob([response.data], { type: "video/mkv" });
    const blobUrl = window.URL.createObjectURL(blob);

    const link = document.createElement("a");
    link.href = blobUrl;
    link.setAttribute("download", "video.mkv");
    document.body.appendChild(link);
    link.click();
    link.remove();

    window.URL.revokeObjectURL(blobUrl);
    return response;
  };

  const sendVideoToQueue = (movieName, duration) => {
    const obj = {
      sourceVideoName: movieName,
      partDuration: duration,
    };
    breakMovieAsync(obj).then((data) => {
      setOpen(true);
    });
  };
  const handleClose = (reason) => {
    if (reason === "clickaway") {
      return;
    }

    setOpen(false);
  };

  return (
    <CardContent>
      <Typography variant="h5" component="h2" gutterBottom>
        {movie}
      </Typography>
      <Typography variant="subtitle1" color="textSecondary" gutterBottom>
        Curated collection of useful development tools and resources
      </Typography>

      {/* Break It Button */}
      <Box sx={{ mt: 2 }}>
        <Button
          variant="contained"
          color="error"
          onClick={() => sendVideoToQueue(movie, 1800)}
        >
          Click to create parts of 30 minutes
        </Button>
        <Snackbar open={open} autoHideDuration={6000} onClose={handleClose}>
          <Alert
            onClose={handleClose}
            severity="success"
            variant="filled"
            sx={{ width: "100%" }}
          >
            You will be notified when your movies is broken into parts
          </Alert>
        </Snackbar>
      </Box>

      <Box sx={{ mt: 2 }}>
        <StyledAccordion>
          <AccordionSummary
            expandIcon={<FaChevronDown />}
            onClick={() => handleDownload(movie)}
          >
            <Box sx={{ display: "flex", alignItems: "center", gap: 1 }}>
              <Typography>Click to see parts of movie</Typography>
            </Box>
          </AccordionSummary>
          <AccordionDetails>
            <Box sx={{ display: "flex", flexDirection: "column", gap: 1 }}>
              {movieChunks.length > 0 ? (
                movieChunks.map((link, index) => (
                  <LinkItem key={index} rel="noopener noreferrer">
                    <IconButton
                      size="small"
                      sx={{ mr: 1 }}
                      onClick={() =>
                        handleDownloadPart(
                          "http://localhost:8080/downloadChunk",
                          {
                            movieName: movie,
                            movieChunk: link,
                          }
                        )
                      }
                    >
                      Part {index}
                    </IconButton>
                    <Typography>{link.title}</Typography>
                    <FaDownload
                      onClick={() =>
                        handleDownloadPart(
                          "http://localhost:8080/downloadChunk",
                          {
                            movieName: movie,
                            movieChunk: link,
                          }
                        )
                      }
                      style={{
                        marginLeft: "auto",
                        fontSize: "0.9rem",
                        cursor: "pointer",
                      }}
                    />
                  </LinkItem>
                ))
              ) : (
                <Typography>Nothing to show</Typography>
              )}
            </Box>
          </AccordionDetails>
        </StyledAccordion>
      </Box>
    </CardContent>
  );
};

export default MovieCard;
