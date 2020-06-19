import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.List;

public class AddRecipe implements CommandHandler {
    private int step;
    private int permission;
    private String title;
    private String type;
    private String meal;
    private String cuisine;
    private int portions;
    private String time;
    private List<RecipeIngredient> recipeIngredients;

    public AddRecipe() {
        step = 9;
        permission = 1;
    }

    public int getPermission() {
        return permission;
    }

    public CommandHandler getInstance(){
        return new AddRecipe();
    }

    public HandlerResult handle(Message receivedMessage, String data) {
        step--;
        switch (step) {
            case 8:
                return HandlerResult.getTextResult(receivedMessage, "Введите название", false);
            case 7:
                title = data;
                return HandlerResult.getTextResult(receivedMessage, "Введите тип", false);
            case 6:
                type = data;
                return HandlerResult.getTextResult(receivedMessage, "Введите трапезу", false);
            case 5:
                meal = data;
                return HandlerResult.getTextResult(receivedMessage, "Введите кухню", false);
            case 4:
                cuisine = data;
                return HandlerResult.getTextResult(receivedMessage, "Введите количество порций", false);
            case 3:
                try {
                    portions = Integer.parseInt(data);
                } catch (Exception e) {
                    portions = 0;
                }
                return HandlerResult.getTextResult(receivedMessage, "Введите время готовки", false);
            case 2:
                time = data;
                List<Ingredient> ingredients = Services.ingredientService.getAll();
                return HandlerResult.getTextResult(receivedMessage, getIngredientsInfo(ingredients), false);
            case 1:
                recipeIngredients = parseIngredients(data);
                return HandlerResult.getTextResult(receivedMessage, "Введите инструкцию", false);
            case 0:
                User author = Services.getUser(receivedMessage.getChat().getUserName());
                Recipe recipe = new Recipe(title, type, meal, cuisine, portions, time, data, author);
                Recipe recipeDB = Services.recipeService.add(recipe);
                for (RecipeIngredient recipeIngredient : recipeIngredients){
                    Services.recipeIngredientService.add(new RecipeIngredient(recipeDB, recipeIngredient.getIngredient(), recipeIngredient.getAmount()));
                }
                Services.recipeService.refresh(recipeDB);
                return HandlerResult.getTextResult(receivedMessage, "Рецепт добавлен", true);
        }
        return null;
    }

    private List<RecipeIngredient> parseIngredients(String text) {
        String[] ingredients = text.split(";");
        List<RecipeIngredient> result = new ArrayList<>();
        List<Ingredient> ingredientsDB = Services.ingredientService.getAll();
        for (String ingredient : ingredients) {
            String[] ingredientAmount = ingredient.split(":");
            if (ingredientAmount.length == 2 && ingredientAmount[0].length() > 0 && ingredientAmount[1].length() > 0) {
                Ingredient ingredientDB = getIngredient(ingredientsDB, ingredientAmount[0]);
                if (ingredientDB == null)
                    ingredientDB = Services.ingredientService.add(new Ingredient(ingredientAmount[0]));
                result.add(new RecipeIngredient(null, ingredientDB, ingredientAmount[1]));
            }
        }
        return result;
    }

    private Ingredient getIngredient(List<Ingredient> ingredients, String title) {
        for (Ingredient ingredient : ingredients)
            if (ingredient.getTitle().toLowerCase().equals((title.toLowerCase())))
                return  ingredient;
        return null;
    }

    private String getIngredientsInfo(List<Ingredient> ingredients) {
        StringBuilder info = new StringBuilder("Список ингредиентов:\n");
        for (Ingredient ingredient : ingredients){
            info.append(ingredient.getTitle()).append("\n");
        }
        info.append("--------------------\n");
        info.append("Введите игредиенты в формате ингредиент1:количество1;ингредиент2:количество2; и т.д.\n");
        info.append("Если вашего ингредиента нет в списке, он добавится автоматически\n");
        return info.toString();
    }
}