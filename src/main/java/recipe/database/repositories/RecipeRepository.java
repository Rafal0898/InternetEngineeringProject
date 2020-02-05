package recipe.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import recipe.database.entities.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
    @Query(value = "SELECT * FROM Recipe r WHERE r.dish_name = :name", nativeQuery = true)
    Recipe getRecipeByDish_name(@Param("name") String dishName);

    @Query(value = "SELECT * FROM Recipe r WHERE r.recipe_id = :id", nativeQuery = true)
    Recipe getRecipeByRecipe_id(@Param("id") int id);

    @Query(value = "SELECT * FROM Recipe as r WHERE r.dish_name LIKE %:name% " +
            "AND r.average_time < :max_time AND r.average_time > :min_time " +
            //"INNER JOIN all_ingredients_in_recipe as al ON r.recipe_id = al.recipe " +
            // "INNER JOIN Ingredient as i ON al.ingredient = i.ingredientIid WHERE i.if_vegan = :if_vegan " +
            "ORDER BY r.dish_name LIMIT :limit OFFSET :offset ", nativeQuery = true)
    List<Recipe> getRecipesByParameters(@Param("name") String dish_name,
                                        @Param("max_time") int max_time, @Param("min_time") int min_time,
                                        //@Param("if_vegan") boolean if_vegan,
                                        @Param("limit") int limit,
                                        @Param("offset") int offset);
}