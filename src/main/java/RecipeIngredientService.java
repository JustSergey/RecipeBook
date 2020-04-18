import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

public class RecipeIngredientService {
    public EntityManager em = Persistence.createEntityManagerFactory("BOOK").createEntityManager();

    public RecipeIngredient add(RecipeIngredient recipeIngredient) {
        em.getTransaction().begin();
        RecipeIngredient recipeDB = em.merge(recipeIngredient);
        em.getTransaction().commit();
        return recipeDB;
    }

    public RecipeIngredient get(Recipe recipe) {
        return em.find(RecipeIngredient.class, recipe);
    }
}
