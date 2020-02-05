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
    public ResponseEntity<String> getIngredient(@PathVariable("id") int ingredientId) {
        Ingredient ingredientById = ingredientRepository.getIngredientByIngredient_id(ingredientId);
        if (ingredientById == null) {
            return new ResponseEntity<>(Responses.NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        Ingredient ingredient = ingredientRepository.getIngredientByIngredient_id(ingredientId);
        String json = jsonCreator.createJsonForObject(ingredient);
        return new ResponseEntity<>(Responses.GOOD + json, HttpStatus.OK);
    }

    @GetMapping(value = "/ingredient")
    public ResponseEntity<String> getIngredients(@RequestParam(name = "name", required = false, defaultValue = "") String ingredientName,
                                                 @RequestParam(value = "limit", required = false, defaultValue = "20") int limit,
                                                 @RequestParam(value = "offset", required = false, defaultValue = "0") int offset) {
        List<Ingredient> ingredientList = ingredientRepository.getIngredients(ingredientName, limit, offset);
        if (ingredientList.isEmpty()) {
            return new ResponseEntity<>(Responses.NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        String json = jsonCreator.createJsonForObject(ingredientList);

        return new ResponseEntity<>(Responses.GOOD + json, HttpStatus.OK);
    }

    @PostMapping(value = "/ingredient")
    public ResponseEntity<String> addIngredient(@RequestBody String body) {
        JSONParser parser = new JSONParser(body);
        try {
            Map<String, String> bodyContent = (Map<String, String>) parser.parse();
            String ingredientName = bodyContent.get("ingredientName");
            boolean ifVegan = Boolean.parseBoolean(bodyContent.get("ifVegan"));
            Ingredient ingredient = ingredientRepository.getIngredientByIngredient_name(ingredientName);
            if (ingredient != null) {
                return new ResponseEntity<>(Responses.ALREADY_EXIST, HttpStatus.CONFLICT);
            }
            Ingredient newIngredient = new Ingredient(ingredientName, ifVegan);
            Ingredient savedIngredient = ingredientRepository.save(newIngredient);
            String json = jsonCreator.createJsonForObject(savedIngredient);
            return new ResponseEntity<>(Responses.ADDED + json, HttpStatus.CREATED);
        } catch (ParseException e) {
            e.printStackTrace();
            return new ResponseEntity<>(Responses.INVALID_VALUE, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/ingredient/{id}")
    public ResponseEntity<String> updateIngredient(@PathVariable("id") int ingredientId,
                                                   @RequestBody String body) {
        Ingredient ingredientById = ingredientRepository.getIngredientByIngredient_id(ingredientId);
        if (ingredientById == null) {
            return new ResponseEntity<>(Responses.NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        JSONParser parser = new JSONParser(body);
        try {
            Map<String, String> bodyContent = (Map<String, String>) parser.parse();
            String ingredientName = bodyContent.get("ingredientName");
            boolean ifVegan = Boolean.parseBoolean(bodyContent.get("ifVegan"));
            Ingredient ingredient = ingredientRepository.getIngredientByIngredient_name(ingredientName);
            if (ingredient != null && !ingredient.getIngredient_name().equals(ingredientById.getIngredient_name())) {
                return new ResponseEntity<>(Responses.ALREADY_EXIST, HttpStatus.CONFLICT);
            }
            Ingredient newIngredient = new Ingredient(ingredientName, ifVegan);
            newIngredient.setIngredient_id(ingredientId);
            Ingredient savedIngredient = ingredientRepository.save(newIngredient);
            String json = jsonCreator.createJsonForObject(savedIngredient);
            return new ResponseEntity<>(Responses.GOOD + json, HttpStatus.CREATED);
        } catch (ParseException e) {
            e.printStackTrace();
            return new ResponseEntity<>(Responses.INVALID_VALUE, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/ingredient/{id}")
    public ResponseEntity<String> deleteIngredient(@PathVariable("id") int ingredientId) {
        Ingredient ingredientById = ingredientRepository.getIngredientByIngredient_id(ingredientId);
        if (ingredientById == null) {
            return new ResponseEntity<>(Responses.NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        Ingredient ingredientToDelete = new Ingredient();
        ingredientToDelete.setIngredient_id(ingredientId);
        ingredientRepository.delete(ingredientToDelete);
        return new ResponseEntity<>(Responses.NO_CONTENT, HttpStatus.NO_CONTENT);
    }
}