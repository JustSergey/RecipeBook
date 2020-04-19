import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Recipes")
@NamedQuery(name = "Recipe.getAll", query = "SELECT r from Recipe r")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private int id;
    private String title;
    private String type;
    private String meal;
    private String cuisine;
    private int portions;
    private String time;
    @Type(type = "text")
    private String instruction;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authorId")
    private User author;
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeIngredient> recipesIngredients;

    public Recipe() {
    }

    public Recipe(String title, String type, String meal, String cuisine, int portions, String time, String instruction, User author) {
        this.title = title;
        this.type = type;
        this.meal = meal;
        this.cuisine = cuisine;
        this.portions = portions;
        this.time = time;
        this.instruction = instruction;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getMeal() {
        return meal;
    }

    public String getCuisine() {
        return cuisine;
    }

    public int getPortions() {
        return portions;
    }

    public String getTime() {
        return time;
    }

    public User getAuthor() {
        return author;
    }

    public String getInstruction() {
        return instruction;
    }

    public List<RecipeIngredient> getIngredients() {
        return recipesIngredients;
    }
}