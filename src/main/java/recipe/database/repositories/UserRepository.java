package recipe.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import recipe.database.entities.Ingredient;
import recipe.database.entities.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query(value = "SELECT name, favourite_dish_id, prepared_dishes, advancement_level FROM User u", nativeQuery = true)
    List<User> getAllUsers();

    @Query(value = "SELECT name, favourite_dish_id, prepared_dishes, advancement_level FROM User u WHERE u.userID = :id", nativeQuery = true)
    User getUserByUser_id(@Param("id") int id);

    @Query(value = "SELECT name, favourite_dish_id, prepared_dishes, advancement_level FROM User u WHERE u.username = :name" +
            "AND u.recipe = :favourite_dish ORDER BY u.username LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<User> getUsersByParameters(@Param("name") String name,
                              @Param("favourite_dish") int favouriteDish,
                              @Param("limit") int limit,
                              @Param("offset") int offset);
}
