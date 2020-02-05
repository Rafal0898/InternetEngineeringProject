package recipe.json;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

import recipe.database.entities.User;

public class JsonWebTokenUtils {
    private JsonWebTokenUtils() {
    }

    public static String getJsonWebToken(User user) {
        long currentTime = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("id", user.getUser_id())
                .setIssuedAt(new Date(currentTime))
                .signWith(SignatureAlgorithm.HS512, user.getPassword())
                .compact();
    }
}
