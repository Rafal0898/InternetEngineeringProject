package recipe.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import recipe.database.entities.RecipeStep;

public interface RecipeStepRepository extends JpaRepository<RecipeStep, Integer> {
    @Query(value = "SELECT * FROM Recipe_step rs WHERE rs.description = :description AND rs.time = :time", nativeQuery = true)
    RecipeStep getRecipeStepByDescriptionAnAndTime(@Param("description") String description,
                                                   @Param("time") int time);

    @Query(value = "SELECT * FROM Recipe_step rs WHERE rs.recipe_step_id = :id", nativeQuery = true)
    RecipeStep getRecipeStepByRecipe_step_id(@Param("id") int id);

    @Query(value = "SELECT * FROM Recipe_step rs WHERE rs.description LIKE %:description% " +
            "ORDER BY rs.recipe_step_id LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<RecipeStep> getRecipeStepsByDescriptionContaining(@Param("description") String description,
                                                           @Param("limit") int limit,
                                                           @Param("offset") int offset);

    @Query(value = "SELECT * FROM Recipe_step rs WHERE rs.recipe_id = :recipeId " +
            "ORDER BY rs.recipe_step_id LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<RecipeStep> getRecipeStepsForOneRecipe(@Param("recipeId") Integer recipeId,
                                                @Param("limit") int limit,
                                                @Param("offset") int offset);
}
