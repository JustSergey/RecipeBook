import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

public class IngredientService {
    public EntityManager em = Persistence.createEntityManagerFactory("BOOK").createEntityManager();

    public Ingredient add(Ingredient ingredient) {
        em.getTransaction().begin();
        Ingredient ingredientDB = em.merge(ingredient);
        em.getTransaction().commit();
        return ingredientDB;
    }

    public List<Ingredient> getAll() {
        TypedQuery<Ingredient> users = em.createNamedQuery("Ingredient.getAll", Ingredient.class);
        return users.getResultList();
    }

    public Ingredient get(int id) {
        return em.find(Ingredient.class, id);
    }
}
