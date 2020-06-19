import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.ArrayList;
import java.util.List;

public class FindByIngredients implements CommandHandler {
    private int step;
    private int permission;
    private List<Ingredient> ingredients;

    public FindByIngredients() {
        step = 2;
        permission = 0;
    }

    public int getPermission() {
        return permission;
    }

    public CommandHandler getInstance(){
        return new FindByIngredients();
    }

    public HandlerResult handle(Message receivedMessage, String data) {
        step--;
        if(step == 1 ){
            ingredients = Services.ingredientService.getAll();
            return HandlerResult.getTextResult(receivedMessage, getIngredientsInfo(ingredients), false);
        }
        if (step == 0) {
            List<Ingredient> ingredients = parseIngredients(data);
            List<Recipe> recipes = getRecipesByIngredients(Services.recipeService.getAll(), ingredients);
            if (recipes.isEmpty() || ingredients.isEmpty())
                return HandlerResult.getTextResult(receivedMessage, "Рецептов с такими ингредиентами не найдено!", true);
            else
                return HandlerResult.getRecipeListResult(receivedMessage, recipes, true);
        }
        return null;
    }

    private List<Recipe> getRecipesByIngredients(List<Recipe> recipes, List<Ingredient> ingredients){
        List<Recipe> result = new ArrayList<>();
        for (Recipe recipe : recipes){
            List<RecipeIngredient> recipeIngredients = recipe.getIngredients();
            boolean isDone = true;
            for (Ingredient ingredient : ingredients){
                boolean isFind = false;
                for (RecipeIngredient recipeIngredient : recipeIngredients){
                    if (recipeIngredient.getIngredient().getId() == ingredient.getId()){
                        isFind = true;
                        break;
                    }
                }
                if (!isFind){
                    isDone = false;
                    break;
                }
            }
            if (isDone){
                result.add(recipe);
            }
        }
        return result;
    }

    private List<Ingredient> parseIngredients(String text) {
        String[] ingredientTitles = text.split(",");
        List<Ingredient> ingredients = new ArrayList<>();
        for (String ingredientTitle : ingredientTitles) {
            String title = ingredientTitle.trim();
            for (Ingredient ingredient : this.ingredients) {
                if (ingredient.getTitle().toLowerCase().equals(title.toLowerCase())) {
                    ingredients.add(ingredient);
                    break;
                }
            }
        }
        return ingredients;
    }

    private String getIngredientsInfo(List<Ingredient> ingredients) {
        StringBuilder info = new StringBuilder("Список ингредиентов:\n");
        for (Ingredient ingredient : ingredients){
            info.append(ingredient.getTitle()).append("\n");
        }
        return info.toString();
    }
}
