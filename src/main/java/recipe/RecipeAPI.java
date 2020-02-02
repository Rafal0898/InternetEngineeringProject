package recipe;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

import recipe.json.JsonCreator;
import recipe.json.JsonWebTokenFilter;

@SpringBootApplication
public class RecipeAPI {

    public static void main(String[] args) {
        SpringApplication.run(RecipeAPI.class, args);
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new JsonWebTokenFilter());
        filterRegistrationBean.setUrlPatterns(Arrays.asList("/ingredient/*", "/recipeStep/*", "/recipe/*"));
        return filterRegistrationBean;
    }

    @Bean
    public JsonCreator creator() {
        ObjectMapper objectMapper = new ObjectMapper();
        return new JsonCreator(objectMapper);
    }

}
