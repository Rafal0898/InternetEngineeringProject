package recipe.database.entities;

import javax.persistence.*;

@Entity
@Table(name = "ingredient", uniqueConstraints = @UniqueConstraint(columnNames = {"ID_ingredient"}))
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_ingredient", unique = true, nullable = false)
    private int ingredient_id;

    @Column(name = "name")
    private String ingredient_name;


    @Column(name = "if_vegan")
    boolean if_vegan;


    @ManyToOne
    @JoinColumn(name = "survey", referencedColumnName = "survey_id")
    private Recipe recipe;

    @ManyToOne
    @JoinColumn(name = "user", referencedColumnName = "user_id")
    private User user;

    @Column(name = "rating")
    private int rating;

    public Ingredient() {
    }

    public int getIngredient_id() {
        return ingredient_id;
    }

    public String getIngredient_name() {
        return ingredient_name;
    }

    public boolean isIf_vegan() {
        return if_vegan;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public User getUser() {
        return user;
    }

    public int getRating() {
        return rating;
    }

    public void setIngredient_id(Integer ingredient_id) {
        this.ingredient_id = ingredient_id;
    }

    public void setIngredient_name(String ingredient_name) {
        this.ingredient_name = ingredient_name;
    }

    public void setIf_vegan(boolean if_vegan) {
        this.if_vegan = if_vegan;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public boolean missNesesseryFields(){
        return this.ingredient_name == null;
    }
}
