package com.assessment.recipe.service;

import com.assessment.recipe.entity.Recipe;

import java.util.List;
import java.util.Optional;

public interface RecipeService {
    Optional<Recipe> findById(Long id);

    List<Recipe> findAll();

    Recipe add(Recipe recipe);

    Recipe update(Recipe recipe);

    void delete(Recipe recipe);

    List<Recipe> findAllRecipesUsingFilters(Optional<Boolean> veganFilter,
                                            Optional<Integer> servingsFilter,
                                            Optional<List<String>> includingIngredientsFilter,
                                            Optional<List<String>> excludingIngredientsFilter,
                                            Optional<String> instructionsFilter);
}
