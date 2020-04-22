import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.List;

public class Services {
    private static EntityManager em = Persistence.createEntityManagerFactory("BOOK").createEntityManager();

    public static RecipeService recipeService = new RecipeService(em);
    public static UserService userService = new UserService(em);
    public static IngredientService ingredientService = new IngredientService(em);
    public static RecipeIngredientService recipeIngredientService = new RecipeIngredientService(em);

    public static User getUser(String userName){
        List<User> users = Services.userService.getAll();
        for (User user : users)
            if (user.getUserName().equals(userName))
                return user;
        return null;
    }
}