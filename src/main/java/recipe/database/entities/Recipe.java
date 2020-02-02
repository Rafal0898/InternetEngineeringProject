package recipe.database.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "recipe", uniqueConstraints=@UniqueConstraint(columnNames = {"ID_recipe"}))
public class Recipe {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @JoinColumn(name = "ID_recipe", unique=true, nullable = false)
    private int recipe_id;

    @Column(name = "dish_name")
    private String dish_name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="all_ingredients_in_recipe", joinColumns = @JoinColumn(name = "recipe"), inverseJoinColumns = @JoinColumn(name = "ingredient"))
    Set<Ingredient> ingredients = new HashSet<>();

    @Column(name = "average_time")
    private int average_time;

    public enum preparing_dificulty { easy, medium, hard }
    @Column(name = "preparing_difficulty")
    preparing_dificulty preparing_dificulty;


    public Recipe(){}

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

    public Recipe.preparing_dificulty getPreparing_dificulty() {
        return preparing_dificulty;
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

    public void setPreparing_dificulty(Recipe.preparing_dificulty preparing_dificulty) {
        this.preparing_dificulty = preparing_dificulty;
    }
    public boolean missNesesseryFields(){
        return this.dish_name == null;
    }
}
