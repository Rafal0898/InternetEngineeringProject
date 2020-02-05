package recipe.database.entities;

import javax.persistence.*;

@Entity
@Table(name = "ingredient", uniqueConstraints = @UniqueConstraint(columnNames = {"ingredient_id"}))
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ingredient_id", unique = true, nullable = false)
    private int ingredient_id;

    @Column(name = "ingredient_name")
    private String ingredient_name;

    @Column(name = "if_vegan")
    boolean if_vegan;

    public Ingredient() {
    }

    public Ingredient(String ingredient_name, boolean if_vegan) {
        this.ingredient_name = ingredient_name;
        this.if_vegan = if_vegan;
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

    public void setIngredient_id(Integer ingredient_id) {
        this.ingredient_id = ingredient_id;
    }

    public void setIngredient_name(String ingredient_name) {
        this.ingredient_name = ingredient_name;
    }

    public void setIf_vegan(boolean if_vegan) {
        this.if_vegan = if_vegan;
    }
}
