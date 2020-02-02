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

import recipe.database.entities.RecipeStep;
import recipe.database.repositories.RecipeStepRepository;
import recipe.json.JsonCreator;
import recipe.json.Responses;

@RestController
public class RecipeStepController {
    @Autowired
    private RecipeStepRepository recipeStepRepository;
    @Autowired
    private JsonCreator jsonCreator;

    @GetMapping(value = "/recipe/{id}")
    public ResponseEntity<String> getRecipeStep(@PathVariable("id") int recipeStepId){
        List<RecipeStep> recipeStepList = recipeStepRepository.getAllRecipeSteps();
        if(checkIfExists(recipeStepList, recipeStepId)){
            return new ResponseEntity<>(Responses.NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        RecipeStep recipeStep = recipeStepRepository.getRecipeStepByRecipe_step_id(recipeStepId);
        String json = jsonCreator.createJsonForObject(recipeStep);
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    @GetMapping(value = "/recipeStep")
    public ResponseEntity<String> getRecipeStepByName(@RequestParam(name = "description", required = false) String description,
                                                      @RequestParam(value = "limit", required = false, defaultValue = "15")int limit,
                                                      @RequestParam(value = "offset", required = false, defaultValue = "0")int offset){
        List<RecipeStep> recipeStepList = recipeStepRepository.getRecipeStepsByDescriptionContaining(description, limit, offset);
        if(recipeStepList.isEmpty()){
            return new ResponseEntity<>(Responses.NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        String json = jsonCreator.createJsonForObject(recipeStepList);
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    @PostMapping(value = "/recipeStep")
    public ResponseEntity<String> addRecipeStep(@RequestBody RecipeStep recipeStep){
        if(recipeStep.missNesesseryFields()){
            return new ResponseEntity<>(Responses.INVALID_VALUE, HttpStatus.BAD_REQUEST);
        }
        recipeStep.setRecipe_step_id(null);
        RecipeStep savedRecipeStep = recipeStepRepository.save(recipeStep);
        String json = jsonCreator.createJsonForObject(savedRecipeStep);
        return new ResponseEntity<>(json, HttpStatus.CREATED);
    }

    @PutMapping(value = "/recipeStep/{id}")
    public ResponseEntity<String> updateRecipeStep(@PathVariable("id") int recipeStepId,
                                                   @RequestBody RecipeStep recipeStep){
        List<RecipeStep> recipeStepList = recipeStepRepository.getAllRecipeSteps();
        if(checkIfExists(recipeStepList, recipeStepId)){
            return new ResponseEntity<>(Responses.NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        if(recipeStep.missNesesseryFields()){
            return new ResponseEntity<>(Responses.INVALID_VALUE, HttpStatus.BAD_REQUEST);
        }
        recipeStep.setRecipe_step_id(recipeStepId);
        RecipeStep savedRecipeStep = recipeStepRepository.save(recipeStep);
        String json = jsonCreator.createJsonForObject(savedRecipeStep);
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    @DeleteMapping(value = "/recipeStep/{id}")
    public ResponseEntity<String> deleteRecipeStep(@PathVariable("id") int recipeStepId){
        RecipeStep recipeStepToDelete = new RecipeStep();
        recipeStepToDelete.setRecipe_step_id(recipeStepId);
        recipeStepRepository.delete(recipeStepToDelete);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private boolean checkIfExists(List<RecipeStep> recipeStepList, int recipeStepId){
        for(RecipeStep recipeStep : recipeStepList){
            if(Objects.equals(recipeStep.getRecipe_step_id(), recipeStepId)){
                return false;
            }
        }
        return true;
    }
}
