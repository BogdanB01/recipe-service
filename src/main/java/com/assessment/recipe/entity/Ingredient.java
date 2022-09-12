package com.assessment.recipe.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode(exclude = "recipe")
@Entity
public class Ingredient {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private Double quantity;

    @ManyToOne
    @JoinColumn(name="recipe_id", nullable = false)
    @JsonBackReference
    private Recipe recipe;

}
