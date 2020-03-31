import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Recipes")
@NamedQuery(name = "Recipe.getAll", query = "SELECT r from Recipe r")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String title;
    private String type;
    private String meal;
    private String cuisine;
    private int portions;
    private String time;
    private String instruction;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authorId")
    private User author;

    /*
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "RecipesIngredients",
            joinColumns =  { @JoinColumn(name = "recipeId") },
            inverseJoinColumns = { @JoinColumn(name = "ingredientId" ) }
            )
    private List<Ingredient> ingredients;
     */

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeIngredient> recipesIngredients;

    public Recipe() {
    }

    public Recipe(String title, String type, String meal, String cuisine, int portions, String time, String instruction){
        this.title = title;
        this.type = type;
        this.meal = meal;
        this.cuisine = cuisine;
        this.portions = portions;
        this.time = time;
        this.instruction = instruction;
    }

    public String getTitle(){
        return title;
    }

    public String getInstruction(){
        return instruction;
    }
}
