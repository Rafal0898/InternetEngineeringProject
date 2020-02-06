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
public class RecipeStepController {
    @Autowired
    private RecipeStepRepository recipeStepRepository;
    @Autowired
    private JsonCreator jsonCreator;
    @Autowired
    private RecipeRepository recipeRepository;

    @GetMapping(value = "/recipeStep/{id}")
    public ResponseEntity<String> getRecipeStep(@PathVariable("id") int recipeStepId) {
        RecipeStep recipeStepById = recipeStepRepository.getRecipeStepByRecipe_step_id(recipeStepId);
        if (recipeStepById == null) {
            return new ResponseEntity<>(Responses.NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        RecipeStep recipeStep = recipeStepRepository.getRecipeStepByRecipe_step_id(recipeStepId);
        String json = jsonCreator.createJsonForObject(recipeStep);
        return new ResponseEntity<>(Responses.GOOD + json, HttpStatus.OK);
    }

    @PostMapping(value = "/recipeStep")
    public ResponseEntity<String> addRecipeStep(@RequestBody String body) {
        JSONParser parser = new JSONParser(body);
        try {
            Map<String, String> bodyContent = (Map<String, String>) parser.parse();
            String description = bodyContent.get("description");
            int time = Integer.parseInt(bodyContent.get("time"));
            int recipeId = Integer.parseInt(bodyContent.get("recipeId"));
            if (time < 0) {
                return new ResponseEntity<>(Responses.INVALID_VALUE + " Time can't be less than 0", HttpStatus.BAD_REQUEST);
            }
            RecipeStep recipeStepByDescriptionAndTime = recipeStepRepository.getRecipeStepByDescriptionAnAndTime(description,
                    time);
            if (recipeStepByDescriptionAndTime != null) {
                return new ResponseEntity<>(Responses.ALREADY_EXIST, HttpStatus.CONFLICT);
            }
            Recipe recipe = recipeRepository.getRecipeByRecipe_id(recipeId);
            if (recipe == null) {
                return new ResponseEntity<>(Responses.NOT_FOUND +
                        " There's not recipe you want to add this step to", HttpStatus.NOT_FOUND);
            }
            RecipeStep newRecipeStep = new RecipeStep(description, time, recipe);
            RecipeStep savedRecipeStep = recipeStepRepository.save(newRecipeStep);
            String json = jsonCreator.createJsonForObject(savedRecipeStep);
            return new ResponseEntity<>(Responses.ADDED + json, HttpStatus.CREATED);
        } catch (ParseException e) {
            e.printStackTrace();
            return new ResponseEntity<>(Responses.INVALID_VALUE, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/recipeStep/{id}")
    public ResponseEntity<String> updateRecipeStep(@PathVariable("id") int recipeStepId,
                                                   @RequestBody String body) {
        RecipeStep recipeStepById = recipeStepRepository.getRecipeStepByRecipe_step_id(recipeStepId);
        if (recipeStepById == null) {
            return new ResponseEntity<>(Responses.NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        JSONParser parser = new JSONParser(body);
        try {
            Map<String, String> bodyContent = (Map<String, String>) parser.parse();
            String description = bodyContent.get("description");
            int time = Integer.parseInt(bodyContent.get("time"));
            if (description == null) {
                return new ResponseEntity<>(Responses.INVALID_VALUE, HttpStatus.BAD_REQUEST);
            }
            if (time < 0) {
                return new ResponseEntity<>(Responses.INVALID_VALUE + " Time can't be less than 0", HttpStatus.BAD_REQUEST);
            }
            RecipeStep recipeStepByDescriptionAndTime = recipeStepRepository
                    .getRecipeStepByDescriptionAnAndTime(description, time);
            if (recipeStepByDescriptionAndTime != null
                    && !recipeStepByDescriptionAndTime.getDescription().equals(recipeStepById.getDescription())
                    && !recipeStepByDescriptionAndTime.getTime().equals(recipeStepById.getTime())) {
                return new ResponseEntity<>(Responses.ALREADY_EXIST, HttpStatus.CONFLICT);
            }
            RecipeStep newRecipeStep = new RecipeStep(description, time, recipeStepById.getRecipe());
            newRecipeStep.setRecipe_step_id(recipeStepId);
            RecipeStep savedRecipeStep = recipeStepRepository.save(newRecipeStep);
            String json = jsonCreator.createJsonForObject(savedRecipeStep);
            return new ResponseEntity<>(Responses.GOOD + json, HttpStatus.CREATED);
        } catch (ParseException e) {
            e.printStackTrace();
            return new ResponseEntity<>(Responses.INVALID_VALUE, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/recipeStep/{id}")
    public ResponseEntity<String> deleteRecipeStep(@PathVariable("id") int recipeStepId) {
        RecipeStep recipeStepById = recipeStepRepository.getRecipeStepByRecipe_step_id(recipeStepId);
        if (recipeStepById == null) {
            return new ResponseEntity<>(Responses.NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        RecipeStep recipeStepToDelete = new RecipeStep();
        recipeStepToDelete.setRecipe_step_id(recipeStepId);
        recipeStepRepository.delete(recipeStepToDelete);
        return new ResponseEntity<>(Responses.NO_CONTENT, HttpStatus.NO_CONTENT);
    }
}
