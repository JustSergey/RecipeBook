import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

public class RecipeService {
    public EntityManager em = Persistence.createEntityManagerFactory("BOOK").createEntityManager();

    public Recipe add(Recipe recipe) {
        em.getTransaction().begin();
        Recipe recipeDB = em.merge(recipe);
        em.getTransaction().commit();
        return recipeDB;
    }

    public void delete(int id) {
        em.getTransaction().begin();
        em.remove(get(id));
        em.getTransaction().commit();
    }

    public List<Recipe> getAll() {
        TypedQuery<Recipe> recipes = em.createNamedQuery("Recipe.getAll", Recipe.class);
        return recipes.getResultList();
    }

<<<<<<< Updated upstream
    public Recipe get(int id) {
        return em.find(Recipe.class, id);
=======
    public List<String> getButtons(String filter){
        String query = "SELECT DISTINCT(c."+filter+") from Recipe c";
        TypedQuery<String> buttons = em.createQuery(query, String.class);
        return buttons.getResultList();
    }

    public  List<Recipe> getByParameter(String parameter, String value){
        TypedQuery<Recipe> recipes= em.createQuery("SELECT c From Recipe c WHERE c." + parameter + " LIKE :" + parameter , Recipe.class)
                .setParameter(parameter, value);
        return recipes.getResultList();
    }

    public Recipe get(int id) {return em.find(Recipe.class, id);
>>>>>>> Stashed changes
    }
}