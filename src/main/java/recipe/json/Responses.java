package recipe.json;

public class Responses {
    public final static String GOOD = "200, everything ok.\n";
    public final static String ADDED = "201, adding completed.\n";
    public final static String NO_CONTENT = "204, successful deleted.\n";
    public final static String INVALID_VALUE = "Error 400, you put invalid value!\n";
    public final static String UNAUTHORIZED = "Error 401, you need to be loged in!\n";
    public final static String NOT_FOUND = "Error 404, not found!\n";
    public final static String ALREADY_EXIST = "Error 409, record already exists!\n";
}
