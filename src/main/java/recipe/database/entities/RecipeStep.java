package recipe.database.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "recipe_step", uniqueConstraints = @UniqueConstraint(columnNames = {"ID_recipe_step"}))

public class RecipeStep {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_recipe_step", unique = true, nullable = false)
    private int recipe_step_id;

    @Column(name = "description")
    private String description;

    @Column(name = "time")
    private int time;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "all_ingredients", joinColumns = @JoinColumn(name = "recipe_step"), inverseJoinColumns = @JoinColumn(name = "ingredient"))
    Set<Ingredient> ingredients = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "recipe_id", referencedColumnName = "ID_recipe")
    Recipe recipe;


    public RecipeStep() {
    }

    public void setRecipe_step_id(Integer recipe_step_id) {
        this.recipe_step_id = recipe_step_id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setIngredients(Set<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }


    public int getRecipe_step_id() {
        return recipe_step_id;
    }

    public String getDescription() {
        return description;
    }

    public int getTime() {
        return time;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public boolean missNesesseryFields() {
        return this.description == null || this.recipe == null;
    }

}
