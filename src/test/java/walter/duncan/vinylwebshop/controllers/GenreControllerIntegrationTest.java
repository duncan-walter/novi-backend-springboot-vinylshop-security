package walter.duncan.vinylwebshop.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import walter.duncan.vinylwebshop.dtos.genre.GenreRequestDto;
import walter.duncan.vinylwebshop.dtos.genre.GenreResponseDto;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
public class GenreControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getGenreById_shouldReturnGenre_whenGenreExists() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/genres/{id}", 1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.name").value("Halloween"))
                .andExpect(jsonPath("$.description").value("Very spooky"));
    }

    @Test
    void createGenre_shouldCreateAndReturnGenre() throws Exception {
        // Arrange
        var genreRequestDto = new GenreRequestDto();
        ReflectionTestUtils.setField(genreRequestDto, "name", "Jazz");
        ReflectionTestUtils.setField(genreRequestDto, "description", "Smooth");

        // Act
        var response = mockMvc.perform(post("/genres").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(genreRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        // Assert
        var responseBody = response.getResponse().getContentAsString();
        var genreResponseDto = objectMapper.readValue(responseBody, GenreResponseDto.class);

        assertNotNull(ReflectionTestUtils.getField(genreResponseDto, "id"));
        assertEquals("Jazz", ReflectionTestUtils.getField(genreResponseDto, "name"));
        assertEquals("Smooth", ReflectionTestUtils.getField(genreResponseDto, "description"));
    }
}

// Note to self: there are two ways of checking response values, a jsonPath way and an object mapper way.