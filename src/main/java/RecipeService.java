import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class RecipeService {
    private EntityManager em;

    public RecipeService(EntityManager em) {
        this.em = em;
    }

    public Recipe add(Recipe recipe) {
        em.getTransaction().begin();
        Recipe recipeDB = em.merge(recipe);
        em.getTransaction().commit();
        return recipeDB;
    }

    public void refresh(Recipe recipe){
        em.refresh(recipe);
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

    public List<String> getButtons(String filter){
        String query = "SELECT DISTINCT(c."+filter+") from Recipe c";
        TypedQuery<String> cuisines = em.createQuery(query, String.class);
        return cuisines.getResultList();
    }

    public  List<Recipe> getByType(String type){
        TypedQuery<Recipe> recipes= em.createQuery("SELECT c From Recipe c WHERE c.type LIKE :type" , Recipe.class)
                .setParameter("type", type);
        return recipes.getResultList();
    }

    public  List<Recipe> getByIngredients(String product){
        String query = "SELECT * from recipes \n" + "inner join recipesingredients as ri on recipes.id = ri.recipeid \n" +
                "inner join ingredients  on ri.ingredientid = ingredients.id\n" +
                "where ingredients.title = '"+product+"'";
        return em.createNativeQuery(query, Recipe.class).getResultList();
    }

    public  List<Recipe> getByCuisine(String cuisine){
        TypedQuery<Recipe> recipes= em.createQuery("SELECT c From Recipe c WHERE c.cuisine LIKE :chosenCuisine" , Recipe.class)
                .setParameter("chosenCuisine", cuisine);
        return recipes.getResultList();
    }

    public  List<Recipe> getByMeal(String meal){
        TypedQuery<Recipe> recipes= em.createQuery("SELECT c From Recipe c WHERE c.meal LIKE :chosenMeal" , Recipe.class)
                .setParameter("chosenMeal", meal);
        return recipes.getResultList();
    }

    public Recipe get(int id) {return em.find(Recipe.class, id);
    }
}