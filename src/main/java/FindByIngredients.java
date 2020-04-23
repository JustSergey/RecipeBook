import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.List;

public class FindByIngredients implements CommandHandler {
    private int step;
    private int permission;

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
            List<Ingredient> ingredients = Services.ingredientService.getAll();
            return HandlerResult.getTextResult(receivedMessage, getIngredientsInfo(ingredients), false);
        }
        if (step == 0) {
            String product = receivedMessage.getText();
            List<Recipe> recipes = Services.recipeService.getByIngredients(product);
            if (recipes.isEmpty())
                return HandlerResult.getTextResult(receivedMessage, "Рецептов с таким ингредиентом не найдено!", false);
            else
                return HandlerResult.getRecipeListResult(receivedMessage, recipes, true);
        }
        return null;
    }

    private String getIngredientsInfo(List<Ingredient> ingredients) {
        StringBuilder info = new StringBuilder("Список ингредиентов:\n");
        for (Ingredient ingredient : ingredients){
            info.append(ingredient.getTitle()).append("\n");
        }
        return info.toString();
    }
}
