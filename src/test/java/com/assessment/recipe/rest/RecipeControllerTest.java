package com.assessment.recipe.rest;

import com.assessment.recipe.entity.Ingredient;
import com.assessment.recipe.entity.Recipe;
import com.assessment.recipe.service.RecipeServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(RecipeController.class)
public class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecipeServiceImpl recipeService;

    @Autowired
    private ObjectMapper objectMapper;

    private Recipe recipe;

    private Ingredient createIngredient(Long id, String name, Double quantity, Recipe recipe) {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(id);
        ingredient.setName(name);
        ingredient.setQuantity(quantity);
        ingredient.setRecipe(recipe);
        return ingredient;
    }

    @BeforeEach
    public void setup() {
        this.recipe = new Recipe();
        this.recipe.setId(1L);
        this.recipe.setName("Test recipe");
        this.recipe.setServings(4);
        this.recipe.setVegan(true);
        this.recipe.setInstructions("Test instructions");

        Set<Ingredient> ingredients = new HashSet<>();
        ingredients.add(createIngredient(1L,"Test ingredient 1", 0.1, recipe));
        ingredients.add(createIngredient(2L,"Test ingredient 2", 0.2, recipe));
        this.recipe.setIngredients(ingredients);
    }

    @Test
    public void testGetById_recipeExists() throws Exception {
        final Long id = 1L;
        Mockito.when(recipeService.findById(id)).thenReturn(Optional.of(recipe));
        mockMvc.perform(MockMvcRequestBuilders.get(String.format("/api/recipes/%s", id)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Test recipe"));
    }

    @Test
    public void testGetById_recipeDoesNotExist() throws Exception {
        final Long id = 1L;
        Mockito.when(recipeService.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get(String.format("/api/recipes/%s", id)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Recipe with id = 1 does not exist"));
    }

    private String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    @Test
    public void testGetAllRecipesFiltered() throws Exception {

        Mockito.when(recipeService.findAllRecipesUsingFilters(any(), any(), any(), any(), any())).thenReturn(Collections.singletonList(recipe));

        Map<String, String> requestParameters = Map.of(
                "vegan", "true",
                "servings", "4",
                "containing", "Test ingredient 1",
                "excluding", "Test ingredient 3",
                "text", "Test"
        );
        String encodedURL = requestParameters.keySet().stream()
                .map(key -> key + "=" + encodeValue(requestParameters.get(key)))
                .collect(Collectors.joining("&", "/api/recipes?", ""));

        mockMvc.perform(MockMvcRequestBuilders.get(encodedURL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value("Test recipe"));
    }

    @Test
    public void testCreateRecipe() throws Exception {
        Mockito.when(recipeService.add(recipe)).thenReturn(recipe);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(recipe))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Test recipe"));
    }

    @Test
    public void testUpdateRecipe_recipeExists() throws Exception {
        final Long id = 1L;
        final Recipe updatedRecipe = new Recipe();
        updatedRecipe.setId(id);
        updatedRecipe.setName("Updated name");

        Mockito.when(recipeService.findById(id)).thenReturn(Optional.of(recipe));
        Mockito.when(recipeService.update(any())).thenReturn(updatedRecipe);

        mockMvc.perform(MockMvcRequestBuilders.put(String.format("/api/recipes/%s", 1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(updatedRecipe)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Updated name"));
    }

    @Test
    public void testUpdateRecipe_recipeDoesNotExist() throws Exception {
        final Long id = 1L;
        Mockito.when(recipeService.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.put(String.format("/api/recipes/%s", 1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(recipe)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Recipe with id = 1 does not exist"));
    }

    @Test
    public void testUpdateRecipe_idFromPathVariableDifferentThanIdFromRequestBody() throws Exception {
        final Long id = 1L;

        mockMvc.perform(MockMvcRequestBuilders.put("/api/recipes/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(recipe)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Id from path variable = 2 is not equal with id from request body = 1"));
    }

    @Test
    public void testDeleteRecipe_existingRecipe() throws Exception {
        final Long id = 1L;
        Mockito.when(recipeService.findById(id)).thenReturn(Optional.of(recipe));

        mockMvc.perform(MockMvcRequestBuilders.delete(String.format("/api/recipes/%s", id)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testDeleteRecipe_recipeDoesNotExist() throws Exception {
        final Long id = 1L;
        Mockito.when(recipeService.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.delete(String.format("/api/recipes/%s", id)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Recipe with id = 1 does not exist"));
    }
}