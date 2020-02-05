package recipe.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import recipe.database.entities.Ingredient;

public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {
    @Query(value = "SELECT * FROM Ingredient i WHERE i.ingredient_name = :i_name", nativeQuery = true)
    Ingredient getIngredientByIngredient_name(@Param("i_name") String ingredient_name);

    @Query(value = "SELECT * FROM Ingredient i WHERE i.ingredient_id = :id", nativeQuery = true)
    Ingredient getIngredientByIngredient_id(@Param("id") int id);

    @Query(value = "SELECT * FROM Ingredient i WHERE i.ingredient_name LIKE %:name% " +
            "ORDER BY i.ingredient_name LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Ingredient> getIngredients(@Param("name") String ingredient_name,
                                    @Param("limit") int limit,
                                    @Param("offset") int offset);
}
