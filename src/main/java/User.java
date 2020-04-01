import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String userName;
    private String permission;
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Recipe> recipes;

    public User() {
    }

    public User(String userName) {
        this.userName = userName;
        permission = "user";
        recipes = new ArrayList<>();
    }

    public String getUserName() {
        return userName;
    }
}