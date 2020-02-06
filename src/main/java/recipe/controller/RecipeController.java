package recipe.controller;

import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
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
import java.util.Map;

import recipe.database.entities.Recipe;
import recipe.database.entities.RecipeStep;
import recipe.database.repositories.RecipeRepository;
import recipe.database.repositories.RecipeStepRepository;
import recipe.json.JsonCreator;
import recipe.json.Responses;


@RestController
public class RecipeController {
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private JsonCreator jsonCreator;
    @Autowired
    private RecipeStepRepository recipeStepRepository;

    @GetMapping(value = "/recipe/{id}")
    public ResponseEntity<String> getRecipe(@PathVariable("id") Integer recipeId) {
        Recipe recipeById = recipeRepository.getRecipeByRecipe_id(recipeId);
        if (recipeById == null) {
            return new ResponseEntity<>(Responses.NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        Recipe recipe = recipeRepository.getRecipeByRecipe_id(recipeId);
        String json = jsonCreator.createJsonForObject(recipe);
        return new ResponseEntity<>(Responses.GOOD + json, HttpStatus.OK);
    }

    @GetMapping(value = "/recipe")
    public ResponseEntity<String> getRecipeByParameters(@RequestParam(name = "name", required = false, defaultValue = "") String name,
                                                        @RequestParam(name = "minTime", required = false, defaultValue = "0") int minTime,
                                                        @RequestParam(name = "maxTime", required = false, defaultValue = "120") int maxTime,
                                                        //@RequestParam(value = "ifVegan", required = false, defaultValue = "0") boolean ifVegan
                                                        @RequestParam(value = "limit", required = false, defaultValue = "15") int limit,
                                                        @RequestParam(value = "offset", required = false, defaultValue = "0") int offset) {
        List<Recipe> recipeList = recipeRepository.getRecipesByParameters(name, maxTime, minTime,// ifVegan,
                limit, offset);
        if (recipeList.isEmpty()) {
            return new ResponseEntity<>(Responses.NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        String json = jsonCreator.createJsonForObject(recipeList);
        return new ResponseEntity<>(Responses.GOOD + json, HttpStatus.OK);
    }

    @GetMapping(value = "recipe/{id}/recipeSteps")
    public ResponseEntity<String> getAllRecipeStepsForOneRecipe(@PathVariable(name = "id") Integer recipeId,
                                                                @RequestParam(value = "limit", required = false, defaultValue = "15") int limit,
                                                                @RequestParam(value = "offset", required = false, defaultValue = "0") int offset) {
        Recipe recipeById = recipeRepository.getRecipeByRecipe_id(recipeId);
        if (recipeById == null) {
            return new ResponseEntity<>(Responses.NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        List<RecipeStep> recipeStepList = recipeStepRepository.getRecipeStepsForOneRecipe(recipeId, limit, offset);
        if (recipeStepList.isEmpty()) {
            return new ResponseEntity<>(Responses.NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        String json = jsonCreator.createJsonForObject(recipeStepList);
        return new ResponseEntity<>(Responses.GOOD + json, HttpStatus.OK);
    }

    @PostMapping(value = "/recipe")
    public ResponseEntity<String> addRecipe(@RequestBody String body) {
        JSONParser parser = new JSONParser(body);
        try {
            Map<String, String> bodyContent = (Map<String, String>) parser.parse();
            String dishName = bodyContent.get("dishName");
            int averageTime = Integer.parseInt(bodyContent.get("averageTime"));
            int preparingDifficulty = Integer.parseInt(bodyContent.get("preparingDifficulty"));
            if (dishName == null) {
                return new ResponseEntity<>(Responses.INVALID_VALUE, HttpStatus.BAD_REQUEST);
            }
            if (averageTime < 1) {
                return new ResponseEntity<>(Responses.INVALID_VALUE + " Average time must be greater than 1", HttpStatus.BAD_REQUEST);
            }
            if (preparingDifficulty < 1 || preparingDifficulty > 10) {
                return new ResponseEntity<>("Preparing difficulty should be between 1 and 10", HttpStatus.BAD_REQUEST);
            }
            Recipe recipe = recipeRepository.getRecipeByDish_name(dishName);
            if (recipe != null) {
                return new ResponseEntity<>(Responses.ALREADY_EXIST, HttpStatus.CONFLICT);
            }
            Recipe newRecipe = new Recipe(dishName, averageTime, preparingDifficulty);
            Recipe savedRecipe = recipeRepository.save(newRecipe);
            String json = jsonCreator.createJsonForObject(savedRecipe);
            return new ResponseEntity<>(Responses.ADDED + json, HttpStatus.CREATED);
        } catch (ParseException e) {
            e.printStackTrace();
            return new ResponseEntity<>(Responses.INVALID_VALUE, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/recipe/{id}")
    public ResponseEntity<String> updateRecipe(@PathVariable("id") int recipeId,
                                               @RequestBody String body) {
        Recipe recipeById = recipeRepository.getRecipeByRecipe_id(recipeId);
        if (recipeById == null) {
            return new ResponseEntity<>(Responses.NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        JSONParser parser = new JSONParser(body);
        try {
            Map<String, String> bodyContent = (Map<String, String>) parser.parse();
            String dishName = bodyContent.get("dishName");
            int averageTime = Integer.parseInt(bodyContent.get("averageTime"));
            int preparingDifficulty = Integer.parseInt(bodyContent.get("preparingDifficulty"));
            if (dishName == null) {
                return new ResponseEntity<>(Responses.INVALID_VALUE, HttpStatus.BAD_REQUEST);
            }
            if (averageTime < 1) {
                return new ResponseEntity<>(Responses.INVALID_VALUE + " Average time must be greater than 1", HttpStatus.BAD_REQUEST);
            }
            if (preparingDifficulty < 1 || preparingDifficulty > 10) {
                return new ResponseEntity<>("Preparing difficulty should be between 1 and 10", HttpStatus.BAD_REQUEST);
            }
            Recipe recipe = recipeRepository.getRecipeByDish_name(dishName);
            if (recipe != null && !recipe.getDish_name().equals(recipeById.getDish_name())) {
                return new ResponseEntity<>(Responses.ALREADY_EXIST, HttpStatus.CONFLICT);
            }
            Recipe newRecipe = new Recipe(dishName, averageTime, preparingDifficulty);
            newRecipe.setRecipe_id(recipeId);
            Recipe savedRecipe = recipeRepository.save(newRecipe);
            String json = jsonCreator.createJsonForObject(savedRecipe);
            return new ResponseEntity<>(Responses.GOOD + json, HttpStatus.CREATED);
        } catch (ParseException e) {
            e.printStackTrace();
            return new ResponseEntity<>(Responses.INVALID_VALUE, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/recipe/{id}")
    public ResponseEntity<String> deleteRecipe(@PathVariable("id") int recipeId) {
        Recipe recipeById = recipeRepository.getRecipeByRecipe_id(recipeId);
        if (recipeById == null) {
            return new ResponseEntity<>(Responses.NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        Recipe recipeToDelete = new Recipe();
        recipeToDelete.setRecipe_id(recipeId);
        recipeRepository.delete(recipeToDelete);
        return new ResponseEntity<>(Responses.NO_CONTENT, HttpStatus.NO_CONTENT);
    }
}
