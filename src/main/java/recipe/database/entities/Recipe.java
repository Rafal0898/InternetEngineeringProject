package recipe.database.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "recipe", uniqueConstraints = @UniqueConstraint(columnNames = {"recipe_id"}))
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JoinColumn(name = "recipe_id", unique = true, nullable = false)
    private int recipe_id;

    @Column(name = "dish_name")
    private String dish_name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "all_ingredients_in_recipe", joinColumns = @JoinColumn(name = "recipe"), inverseJoinColumns = @JoinColumn(name = "ingredient"))
    Set<Ingredient> ingredients = new HashSet<>();

    @Column(name = "average_time")
    private int average_time;

    @Column(name = "preparing_difficulty")
    int preparing_difficulty;

    public Recipe() {
    }

    public Recipe(String dish_name, int average_time, int preparing_difficulty) {
        this.dish_name = dish_name;
        this.average_time = average_time;
        this.preparing_difficulty = preparing_difficulty;
    }

    public int getRecipe_id() {
        return recipe_id;
    }

    public String getDish_name() {
        return dish_name;
    }

    public Set<Ingredient> getIngredients() {
        return ingredients;
    }

    public int getAverage_time() {
        return average_time;
    }

    public int getPreparing_difficulty() {
        return preparing_difficulty;
    }

    public void setRecipe_id(Integer recipe_id) {
        this.recipe_id = recipe_id;
    }

    public void setDish_name(String dish_name) {
        this.dish_name = dish_name;
    }

    public void setIngredients(Set<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void setAverage_time(int average_time) {
        this.average_time = average_time;
    }

    public void setPreparing_difficulty(int preparing_difficulty) {
        this.preparing_difficulty = preparing_difficulty;
    }
}
