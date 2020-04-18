import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

public class UserService {
    public EntityManager em = Persistence.createEntityManagerFactory("BOOK").createEntityManager();

    public void add(User user) {
        em.getTransaction().begin();
        User recipeDB = em.merge(user);
        em.getTransaction().commit();
    }

    public List<User> getAll() {
        TypedQuery<User> users = em.createNamedQuery("User.getAll", User.class);
        return users.getResultList();
    }

    public User get(int id) {
        return em.find(User.class, id);
    }
}