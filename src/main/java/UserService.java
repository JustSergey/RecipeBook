import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

public class UserService {
    public EntityManager em = Persistence.createEntityManagerFactory("BOOK").createEntityManager();

    public User add(User user) {
        em.getTransaction().begin();
        User userDB = em.merge(user);
        em.getTransaction().commit();
        return userDB;
    }

    public List<User> getAll() {
        TypedQuery<User> users = em.createNamedQuery("User.getAll", User.class);
        return users.getResultList();
    }

    public User get(int id) {
        return em.find(User.class, id);
    }
}