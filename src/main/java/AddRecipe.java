import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.List;

public class AddRecipe implements CommandHandler {
    private int step;
    private String title;
    private String type;
    private String meal;
    private String cuisine;
    private int portions;
    private String time;
    private List<RecipeIngredient> recipeIngredients;

    public AddRecipe() {
        step = 8;
    }

    public boolean execute(Message receivedMessage, Bot bot) {
        step--;
        switch (step) {
            case 7:
                title = receivedMessage.getText();
                bot.sendMessage(receivedMessage, "Введите тип", null);
                return true;
            case 6:
                type = receivedMessage.getText();
                bot.sendMessage(receivedMessage, "Введите трапезу", null);
                return true;
            case 5:
                meal = receivedMessage.getText();
                bot.sendMessage(receivedMessage, "Введите кухню", null);
                return true;
            case 4:
                cuisine = receivedMessage.getText();
                bot.sendMessage(receivedMessage, "Введите количество порций", null);
                return true;
            case 3:
                try {
                    portions = Integer.parseInt(receivedMessage.getText());
                } catch (Exception e) {
                    portions = 0;
                }
                bot.sendMessage(receivedMessage, "Введите время готовки", null);
                return true;
            case 2:
                time = receivedMessage.getText();
                List<Ingredient> ingredients = bot.ingredientService.getAll();
                bot.sendMessage(receivedMessage, getIngredientsInfo(ingredients), null);
                return true;
            case 1:
                recipeIngredients = parseIngredients(receivedMessage.getText(), bot);
                bot.sendMessage(receivedMessage, "Введите инструкцию", null);
                return true;
            case 0:
                String instruction = receivedMessage.getText();
                User author = bot.getUser(receivedMessage.getChat().getUserName());
                Recipe recipe = new Recipe(title, type, meal, cuisine, portions, time, instruction, author);
                Recipe recipeDB = bot.recipeService.add(recipe);
                for (RecipeIngredient recipeIngredient : recipeIngredients){
                    bot.recipeIngredientService.add(new RecipeIngredient(recipeDB, recipeIngredient.getIngredient(), recipeIngredient.getAmount()));
                }
                bot.sendMessage(receivedMessage, "Рецепт добавлен", null);
                return false;
        }
        return false;
    }

    private List<RecipeIngredient> parseIngredients(String text, Bot bot) {
        String[] ingredients = text.split(";");
        List<RecipeIngredient> result = new ArrayList();
        List<Ingredient> ingredientsDB = bot.ingredientService.getAll();
        for (String ingredient : ingredients) {
            String[] ingredientAmount = ingredient.split(":");
            Ingredient ingredientDB = getIngredient(ingredientsDB, ingredientAmount[0]);
            if (ingredientDB == null)
                ingredientDB = bot.ingredientService.add(new Ingredient(ingredientAmount[0]));
            result.add(new RecipeIngredient(null, ingredientDB, ingredientAmount[1]));
        }
        return result;
    }

    private Ingredient getIngredient(List<Ingredient> ingredients, String title) {
        for (Ingredient ingredient : ingredients){
            if (ingredient.getTitle().equals((title)))
                return  ingredient;
        }
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
