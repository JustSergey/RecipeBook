import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Ingredients")
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String title;
    @OneToMany(mappedBy = "ingredient", cascade = CascadeType.ALL)
    private List<RecipeIngredient> recipesIngredients;

    public Ingredient(){
    }

    public Ingredient(String title){
        this.title = title;
    }
}
