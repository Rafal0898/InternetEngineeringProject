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

import recipe.json.JsonCreator;
import recipe.json.Responses;
import recipe.database.entities.Ingredient;
import recipe.database.repositories.IngredientRepository;


@RestController
public class IngredientController {
    @Autowired
    private IngredientRepository ingredientRepository;
    @Autowired
    private JsonCreator jsonCreator;

    @GetMapping(value = "/ingredient/{id}")
    public ResponseEntity<String> getIngredient( @PathVariable("id") int ingredientId){
        List<Ingredient> ingredientList = ingredientRepository.getAllIngredients();
        if(checkIfExists(ingredientList, ingredientId)){
            return new ResponseEntity<>(Responses.NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        Ingredient ingredient = ingredientRepository.getIngredientByIngredient_id(ingredientId);
        String json = jsonCreator.createJsonForObject(ingredient);
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    @GetMapping(value = "/ingredient")
    public ResponseEntity<String> getIngredients(@RequestParam(name = "name", required = false) String ingredientName,
                                                 @RequestParam(value = "limit", required = false, defaultValue = "20")int limit,
                                                 @RequestParam(value = "offset", required = false, defaultValue = "0")int offset){
        if(ingredientName.contains(".*\\d.*")){
            return new ResponseEntity<>(Responses.INVALID_VALUE, HttpStatus.BAD_REQUEST);
        }
        List<Ingredient> ingredientList = ingredientRepository.getIngredients(ingredientName, limit, offset);
        if(ingredientList.isEmpty()){
            return new ResponseEntity<>(Responses.NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        String json = jsonCreator.createJsonForObject(ingredientList);

        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    @PostMapping(value = "/ingredient")
    public ResponseEntity<String> addIngredient(@RequestBody Ingredient ingredient){
        if(ingredient.missNesesseryFields()){
            return new ResponseEntity<>(Responses.INVALID_VALUE, HttpStatus.BAD_REQUEST);
        }
        ingredient.setIngredient_id(null);
        Ingredient savedIngredient = ingredientRepository.save(ingredient);
        String json = jsonCreator.createJsonForObject(savedIngredient);
        return new ResponseEntity<>(json, HttpStatus.CREATED);
    }

    @PutMapping(value = "/ingredient/{id}")
    public ResponseEntity<String> updateIngredient(@PathVariable("id") int ingredientId,
                                                   @RequestBody Ingredient ingredient){
        List<Ingredient> ingredientList = ingredientRepository.getAllIngredients();
        if(checkIfExists(ingredientList, ingredientId)){
            return new ResponseEntity<>(Responses.NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        if(ingredient.missNesesseryFields()){
            return new ResponseEntity<>(Responses.INVALID_VALUE, HttpStatus.BAD_REQUEST);
        }
        ingredient.setIngredient_id(ingredientId);
        Ingredient savedIngredient = ingredientRepository.save(ingredient);
        String json = jsonCreator.createJsonForObject(savedIngredient);
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    @DeleteMapping(value = "/ingredient/{id}")
    public ResponseEntity<String> deleteIngredient(@PathVariable("id") int ingredientId){
        List<Ingredient> ingredientList = ingredientRepository.getAllIngredients();
        if(checkIfExists(ingredientList, ingredientId)){
            return new ResponseEntity<>(Responses.NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        Ingredient ingredientToDelete = new Ingredient();
        ingredientToDelete.setIngredient_id(ingredientId);
        ingredientRepository.delete(ingredientToDelete);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private boolean checkIfExists(List<Ingredient> ingredientList, int ingredientId){
        for(Ingredient ingredient : ingredientList){
            if(Objects.equals(ingredient.getIngredient_id(), ingredientId)){
                return false;
            }
        }
        return true;
    }
}
