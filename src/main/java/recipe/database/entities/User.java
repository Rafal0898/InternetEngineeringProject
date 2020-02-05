package recipe.database.entities;

import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.persistence.*;
import javax.websocket.DecodeException;

@Entity
@Table(name = "user", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id"}))
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id", unique = true, nullable = false)
    private int user_id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "prepared_dishes")
    private int prepared_dishes;

    @Column(name = "advancement_level")
    private int advancement_level;

    @ManyToOne
    @JoinColumn(name = "favourite_dish_id", referencedColumnName = "recipe_id")
    Recipe recipe;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt(12));
        this.prepared_dishes = 0;
        this.advancement_level = 1;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getUsername() {
        return username;
    }

    public int getPrepared_dishes() {
        return prepared_dishes;
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

    public void setPrepared_dishes(int prepered_dishes) {
        this.prepared_dishes = prepered_dishes;
    }

    public void setAdvancement_level(int advancement_level) {
        this.advancement_level = advancement_level;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public boolean checkPassword(String password) throws DecodeException {
        return BCrypt.checkpw(password, this.password);
    }
}
