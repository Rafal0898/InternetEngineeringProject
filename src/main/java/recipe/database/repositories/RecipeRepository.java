package recipe.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import recipe.database.entities.Ingredient;
import recipe.database.entities.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
    @Query(value = "SELECT * FROM recipe r", nativeQuery = true)
    List<Recipe> getAllRecipes();
    @Query(value = "SELECT * FROM recipe r WHERE r.recipeID = :id", nativeQuery = true)
    Recipe getRecipeByRecipe_id(@Param("id") int id);
    @Query(value = "SELECT * FROM recipe as r WHERE r.name LIKE %:dish_name%" +
            "AND  ORDER BY r.name LIMIT :limit OFFSET :offset" +
            "AND r.average_time < :max_time AND r.average_time > :min_time" +
            "AND r.prepering_diffuculty = :prepering_difficulty" +
            "AND INNER JOIN all_ingredients as al ON r.ID_recipe = al.recipe \" +\n" +
            "INNER JOIN Ingredient as i ON al.ingredient = i.ID_ingredient WHERE i.if_vegan = :if_vegan", nativeQuery = true)
    List<Recipe> getRecipesByParameters(@Param("name") String dish_name,
                                        @Param("max_time") int max_time, @Param("min_time") int min_time,
                                        @Param("difficulty") Recipe.preparing_dificulty prepering_difficulty,
                                        @Param("if_vegan") boolean if_vegan,
                                        @Param("limit") int limit,
                                        @Param("offset") int offset);
}