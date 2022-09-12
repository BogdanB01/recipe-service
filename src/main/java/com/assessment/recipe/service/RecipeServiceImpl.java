package com.assessment.recipe.service;

import com.assessment.recipe.entity.QRecipe;
import com.assessment.recipe.entity.Recipe;
import com.assessment.recipe.repository.RecipeRepository;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;

    @Autowired
    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public Optional<Recipe> findById(Long id) {
        return recipeRepository.findById(id);
    }

    @Override
    public List<Recipe> findAll() {
        return recipeRepository.findAll();
    }

    @Override
    public Recipe add(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    @Override
    public Recipe update(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    @Override
    public void delete(Recipe recipe) {
        recipeRepository.delete(recipe);
    }

    @Override
    public List<Recipe> findAllRecipesUsingFilters(Optional<Boolean> veganFilter,
                                                   Optional<Integer> servingsFilter,
                                                   Optional<List<String>> includingIngredientsFilter,
                                                   Optional<List<String>> excludingIngredientsFilter,
                                                   Optional<String> instructionsFilter) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        veganFilter.ifPresent(vegan -> booleanBuilder.and(QRecipe.recipe.vegan.eq(vegan)));
        servingsFilter.ifPresent(servings -> booleanBuilder.and(QRecipe.recipe.servings.eq(servings)));
        includingIngredientsFilter.ifPresent(includingIngredients -> booleanBuilder.and(QRecipe.recipe.ingredients.any().name.in(includingIngredients)));
        excludingIngredientsFilter.ifPresent(excludingIngredients -> booleanBuilder.andNot(QRecipe.recipe.ingredients.any().name.in(excludingIngredients)));
        instructionsFilter.ifPresent(instructions -> booleanBuilder.and(QRecipe.recipe.instructions.containsIgnoreCase(instructions)));

        if (booleanBuilder.getValue() != null) {
            Iterable<Recipe> recipes = recipeRepository.findAll(booleanBuilder.getValue());
            return StreamSupport.stream(recipes.spliterator(), false)
                    .collect(Collectors.toList());
        } else {
            return recipeRepository.findAll();
        }
    }
}
