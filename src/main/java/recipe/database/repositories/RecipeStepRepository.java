package recipe.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import recipe.database.entities.Recipe;
import recipe.database.entities.RecipeStep;

public interface RecipeStepRepository extends JpaRepository<RecipeStep, Integer> {
    @Query(value = "SELECT * FROM recipe_step rs", nativeQuery = true)
    List<RecipeStep> getAllRecipeSteps();

    @Query(value = "SELECT * FROM recipe_step rs WHERE rs.recipeID = :id", nativeQuery = true)
    RecipeStep getRecipeStepByRecipe_step_id(@Param("id") int id);

    @Query(value = "SELECT * FROM recipe_step rs WHERE rs.description LIKE %:description%" +
            "AND  ORDER BY rs.name LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<RecipeStep> getRecipeStepsByDescriptionContaining(@Param("description") String description,
                                                           @Param("limit") int limit,
                                                           @Param("offset") int offset);
}
