package recipe.database.entities;

import javax.persistence.*;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = {"ID_user"}))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_user", unique = true, nullable = false)
    private int user_id;

    @Column(name = "name")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "prepered_dishes")
    private int prepered_dishes;

    @Column(name = "advancement_level")
    private int advancement_level;

    @ManyToOne
    @JoinColumn(name = "favourite_dish_id", referencedColumnName = "ID_recipe")
    Recipe recipe;

    public User() {
    }

    public User(String name) {
        this.username = name;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getUsername() {
        return username;
    }

    public int getPrepered_dishes() {
        return prepered_dishes;
    }

    public int getAdvancement_level() {
        return advancement_level;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPrepered_dishes(int prepered_dishes) {
        this.prepered_dishes = prepered_dishes;
    }

    public void setAdvancement_level(int advancement_level) {
        this.advancement_level = advancement_level;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public boolean missNesesseryFields() {
        return this.username == null || this.password == null;
    }
}
