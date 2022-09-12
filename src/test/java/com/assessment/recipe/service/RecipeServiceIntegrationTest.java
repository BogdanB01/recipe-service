package com.assessment.recipe.service;

import com.assessment.recipe.entity.Recipe;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class RecipeServiceIntegrationTest {

    @Autowired
    private RecipeServiceImpl recipeService;

    @Test
    @Sql("/import_recipes.sql")
    public void testFindAllRecipesUsingFilters_allVeganRecipes() {
        // arrange
        final Optional<Boolean> veganFilter = Optional.of(true);
        // act
        List<Recipe> veganRecipes = recipeService.findAllRecipesUsingFilters(veganFilter,
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
        // assert
        Assertions.assertEquals(2, veganRecipes.size());
    }

    @Test
    @Sql("/import_recipes.sql")
    public void testFindAllRecipesUsingFilters_4ServingsAndPotatoesAsIngredient() {
        // arrange
        final Optional<Integer> servingsFilter = Optional.of(4);
        final Optional<List<String>> includingIngredientsFilter = Optional.of(Collections.singletonList("potato"));
        // act
        List<Recipe> recipes = recipeService.findAllRecipesUsingFilters(Optional.empty(),
                servingsFilter, includingIngredientsFilter, Optional.empty(), Optional.empty());
        // assert
        Assertions.assertEquals(1, recipes.size());
    }

    @Test
    @Sql("/import_recipes.sql")
    public void testFindAllRecipesUsingFilters_excludingSalmonAndHavingGrillInInstructions() {
        // arrange
        final Optional<List<String>> excludingIngredientsFilter = Optional.of(Collections.singletonList("salmon"));
        final Optional<String> instructionsFilter = Optional.of("grill");
        // act
        List<Recipe> recipes = recipeService.findAllRecipesUsingFilters(Optional.empty(),
                Optional.empty(), Optional.empty(), excludingIngredientsFilter, instructionsFilter);
        // assert
        Assertions.assertEquals(1, recipes.size());
    }
}
