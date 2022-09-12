package com.assessment.recipe.rest;

import com.assessment.recipe.entity.Recipe;
import com.assessment.recipe.rest.exception.BadRequestException;
import com.assessment.recipe.rest.exception.NotFoundException;
import com.assessment.recipe.service.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/recipes")
@Slf4j
public class RecipeController {

    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    /**
     * Returns the recipes matching given filters (AND condition)
     * @param veganFilter if present, filter the vegan recipes
     * @param servingsFilter if present, filter the recipes by the number of servings
     * @param includingIngredientsFilter if present, filter the recipes that contain the given list of ingredients
     * @param excludingIngredientsFilter if present, filter the recipes that do not contain the given list of ingredients
     * @param instructionsFilter if present, filter the recipes that contain the given instructions in the text
     * @return the recipes that match the filters
     */
    @GetMapping
    public List<Recipe> getAllRecipesFiltered(@RequestParam(name = "vegan", required = false) Optional<Boolean> veganFilter,
                               @RequestParam(name = "servings", required = false) Optional<Integer> servingsFilter,
                               @RequestParam(name = "containing", required = false) Optional<List<String>> includingIngredientsFilter,
                               @RequestParam(name = "excluding", required = false) Optional<List<String>> excludingIngredientsFilter,
                               @RequestParam(name = "text", required = false) Optional<String> instructionsFilter) {

        return recipeService.findAllRecipesUsingFilters(veganFilter, servingsFilter, includingIngredientsFilter, excludingIngredientsFilter, instructionsFilter);
    }

    /**
     * Returns the recipe with the given id
     * @param id the id of the recipe that is fetched
     * @return the recipe with the given id
     */
    @GetMapping(value = "/{id}")
    public Recipe getById(@PathVariable final Long id) {
        return recipeService.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Recipe with id = %s does not exist", id)));
    }

    /**
     * Creates the given recipe
     * @param recipe the recipe that is created
     * @return the created recipe
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Recipe create(@RequestBody final Recipe recipe) {
        return recipeService.add(recipe);
    }

    /**
     * Updates the given recipe
     * @param id the id of the recipe that is updated
     * @param recipe the recipe with the updated information
     * @return the updated recipe
     */
    @PutMapping(value = "/{id}")
    public Recipe update(@PathVariable final Long id, @RequestBody final Recipe recipe) {
        if (!id.equals(recipe.getId())) {
            throw new BadRequestException(String.format("Id from path variable = %s is not equal with id from request body = %s", id, recipe.getId()));
        }
        Recipe dbRecipe = recipeService.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Recipe with id = %s does not exist", id)));
        mapRecipe(recipe, dbRecipe);

        return recipeService.update(dbRecipe);
    }

    private void mapRecipe(Recipe source, Recipe destination) {
        destination.setName(source.getName());
        destination.setServings(source.getServings());
        destination.setVegan(source.getVegan());
        destination.getIngredients().forEach(destination::removeIngredient);
        source.getIngredients().forEach(destination::addIngredient);
    }

    /**
     * Deletes the recipe with the given id
     * @param id the id of the recipe that is deleted
     */
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable final Long id) {
        Recipe recipe = recipeService.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Recipe with id = %s does not exist", id)));
        recipeService.delete(recipe);
    }

}
