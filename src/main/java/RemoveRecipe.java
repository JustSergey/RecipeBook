import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

public class RemoveRecipe implements CommandHandler {
    private int step;
    private int permission;
    private int id;

    public RemoveRecipe() {
        step = 3;
        permission = 1;
    }

    public int getPermission() {
        return permission;
    }

    public CommandHandler getInstance(){
        return new RemoveRecipe();
    }

    public HandlerResult handle(Message receivedMessage, String data) {
        step--;
        switch (step) {
            case 2:
                List<Recipe> recipes = Services.recipeService.getAll();
                return HandlerResult.getRecipeListResult(receivedMessage, recipes, false);
            case 1:
                try {
                    id = Integer.parseInt(data);
                    String title = Services.recipeService.get(id).getTitle();
                    return HandlerResult.getTextResult(receivedMessage, "Удалить рецепт " + title + "? (Да/Нет)", false);
                } catch (Exception e){
                    return HandlerResult.getTextResult(receivedMessage, "Не удалось удалить рецепт", true);
                }
            case 0:
                if (data.equals("Да")){
                    Services.recipeService.delete(id);
                    return HandlerResult.getTextResult(receivedMessage, "Рецепт удален", true);
                }
                else
                    return HandlerResult.getTextResult(receivedMessage, "Удаление отменено", true);
        }
        return null;
    }
}