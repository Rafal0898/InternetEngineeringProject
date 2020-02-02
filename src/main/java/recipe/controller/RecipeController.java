package recipe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

import recipe.database.entities.Recipe;
import recipe.database.repositories.RecipeRepository;
import recipe.json.JsonCreator;
import recipe.json.Responses;


@RestController
public class RecipeController {
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private JsonCreator jsonCreator;

    @GetMapping(value = "/recipe/{id}")
    public ResponseEntity<String> getRecipe(@PathVariable("id") int recipeId) {
        List<Recipe> recipeList = recipeRepository.getAllRecipes();
        if (checkIfExists(recipeList, recipeId)) {
            return new ResponseEntity<>(Responses.NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        Recipe recipe = recipeRepository.getRecipeByRecipe_id(recipeId);
        String json = jsonCreator.createJsonForObject(recipe);
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    @GetMapping(value = "/recipe")
    public ResponseEntity<String> getRecipeByParameters(@RequestParam(name = "name", required = false) String name,
                                                        @RequestParam(name = "difficulty", required = false, defaultValue = "medium") Recipe.preparing_dificulty difficulty,
                                                        @RequestParam(name = "minTime", required = false, defaultValue = "0") int minTime,
                                                        @RequestParam(name = "maxTime", required = false, defaultValue = "120") int maxTime,
                                                        @RequestParam(value = "limit", required = false, defaultValue = "15") int limit,
                                                        @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
                                                        @RequestParam(value = "ifVegan", required = false, defaultValue = "0") boolean ifVegan) {
        List<Recipe> recipeList = recipeRepository.getRecipesByParameters(name, maxTime, minTime, difficulty, ifVegan, limit, offset);
        if(recipeList.isEmpty()){
            return new ResponseEntity<>(Responses.NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        String json = jsonCreator.createJsonForObject(recipeList);
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    @PostMapping(value = "/recipe")
    public ResponseEntity<String> addRecipe(@RequestBody Recipe recipe) {
        if(recipe.missNesesseryFields()){
            return new ResponseEntity<>(Responses.INVALID_VALUE, HttpStatus.BAD_REQUEST);
        }
        recipe.setRecipe_id(null);
        Recipe savedRecipe = recipeRepository.save(recipe);
        String json = jsonCreator.createJsonForObject(savedRecipe);
        return new ResponseEntity<>(json, HttpStatus.CREATED);
    }

    @PutMapping(value = "/recipe/{id}")
    public ResponseEntity<String> updateRecipe(@PathVariable("id") int recipeId,
                                               @RequestBody Recipe recipe) {

        List<Recipe> recipeList = recipeRepository.getAllRecipes();
        if(recipeList.isEmpty()){
            return new ResponseEntity<>(Responses.NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        if(recipe.missNesesseryFields()){
            return new ResponseEntity<>(Responses.INVALID_VALUE, HttpStatus.BAD_REQUEST);
        }
        recipe.setRecipe_id(recipeId);
        Recipe savedRecipe = recipeRepository.save(recipe);
        String json = jsonCreator.createJsonForObject(savedRecipe);
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    @DeleteMapping(value = "/recipe/{id}")
    public ResponseEntity<String> deleteRecipe(@PathVariable("id") int recipeId) {
        List<Recipe> recipeList = recipeRepository.getAllRecipes();
        if(recipeList.isEmpty()){
            return new ResponseEntity<>(Responses.NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        Recipe recipeToDelete = new Recipe();
        recipeToDelete.setRecipe_id(recipeId);
        recipeRepository.delete(recipeToDelete);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private boolean checkIfExists(List<Recipe> recipeList, int recipeId) {
        for (Recipe recipe : recipeList) {
            if (Objects.equals(recipe.getRecipe_id(), recipeId)) {
                return false;
            }
        }
        return true;
    }
}
