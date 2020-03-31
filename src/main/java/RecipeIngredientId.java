import java.io.Serializable;

public class RecipeIngredientId implements Serializable {
    int recipe;
    int ingredient;

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
