import javax.persistence.EntityManager;

public class RecipeIngredientService {
    private EntityManager em;

    public RecipeIngredientService(EntityManager em) {
        this.em = em;
    }

    public RecipeIngredient add(RecipeIngredient recipeIngredient) {
        em.getTransaction().begin();
        RecipeIngredient recipeDB = em.merge(recipeIngredient);
        em.getTransaction().commit();
        return recipeDB;
    }

    public RecipeIngredient get(Recipe recipe) {
        return em.find(RecipeIngredient.class, recipe);
    }
}
