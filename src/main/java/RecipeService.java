import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

public class RecipeService {
    public EntityManager em = Persistence.createEntityManagerFactory("BOOK").createEntityManager();

    public void add(Recipe recipe) {
        em.getTransaction().begin();
        Recipe recipeDB = em.merge(recipe);
        em.getTransaction().commit();
    }

    public List<Recipe> getAll() {
        TypedQuery<Recipe> recipes = em.createNamedQuery("Recipe.getAll", Recipe.class);
        return recipes.getResultList();
    }

    public Recipe get(int id) {
        return em.find(Recipe.class, id);
    }
}