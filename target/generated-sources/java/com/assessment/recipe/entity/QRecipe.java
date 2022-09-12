package com.assessment.recipe.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRecipe is a Querydsl query type for Recipe
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecipe extends EntityPathBase<Recipe> {

    private static final long serialVersionUID = 213580024L;

    public static final QRecipe recipe = new QRecipe("recipe");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final SetPath<Ingredient, QIngredient> ingredients = this.<Ingredient, QIngredient>createSet("ingredients", Ingredient.class, QIngredient.class, PathInits.DIRECT2);

    public final StringPath instructions = createString("instructions");

    public final StringPath name = createString("name");

    public final NumberPath<Integer> servings = createNumber("servings", Integer.class);

    public final BooleanPath vegan = createBoolean("vegan");

    public QRecipe(String variable) {
        super(Recipe.class, forVariable(variable));
    }

    public QRecipe(Path<? extends Recipe> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRecipe(PathMetadata metadata) {
        super(Recipe.class, metadata);
    }

}

