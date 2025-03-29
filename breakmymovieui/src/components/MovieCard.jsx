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
} from "@mui/material";
import { styled } from "@mui/system";
import { FaChevronDown, FaDownload } from "react-icons/fa";
import axios from "axios";
import { getMovieChunks } from "../services/MovieService";

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

  const handleImageError = (e) => {
    e.target.src =
      "https://images.unsplash.com/photo-1555949963-aa79dcee981c?ixlib=rb-4.0.3";
    e.target.alt = "Fallback development image";
  };

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

  return (
    <StyledCard>
      <CardMedia
        component="img"
        height="200"
        image="https://images.unsplash.com/photo-1661956602116-aa6865609028?ixlib=rb-4.0.3"
        alt="Development resources header image"
        onError={handleImageError}
        loading="lazy"
      />
      <CardContent>
        <Typography variant="h5" component="h2" gutterBottom>
          {movie}
        </Typography>
        <Typography variant="subtitle1" color="textSecondary" gutterBottom>
          Curated collection of useful development tools and resources
        </Typography>

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
    </StyledCard>
  );
};

export default MovieCard;
