import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

public class FullList implements CommandHandler {
    private int step;
    private int permission;

    public FullList() {
        step = 1;
        permission = 0;
    }

    public int getPermission() {
        return permission;
    }

    public CommandHandler getInstance(){
        return new FullList();
    }

    public HandlerResult handle(Message receivedMessage, String data) {
        step--;
        if (step == 0) {
            List<Recipe> recipes = Services.recipeService.getAll();
            return HandlerResult.getRecipeListResult(receivedMessage, recipes, true);
        }
        return null;
    }
}