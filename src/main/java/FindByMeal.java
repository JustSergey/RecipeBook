import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.List;

public class FindByMeal implements CommandHandler {
    private int step;
    private int permission;

    public FindByMeal() {
        step = 3;
        permission = 0;
    }

    public int getPermission() {
        return permission;
    }

    public CommandHandler getInstance(){
        return new FindByMeal();
    }

    public HandlerResult handle(Message receivedMessage, String data) {
        step--;
        if(step == 2 ){
            ReplyKeyboardMarkup keyboard = Keyboards.getChosenFilterKeyboard("meal");
            return HandlerResult.getTextResult(receivedMessage, "Клавиатура обновлена", keyboard, false);
        }
        if (step == 1) {
            String meal = receivedMessage.getText();
            List<Recipe> recipes = Services.recipeService.getByMeal(meal);
            return HandlerResult.getRecipeListResult(receivedMessage, recipes, false);
        }
        if (step == 0){
            ReplyKeyboardMarkup keyboard = Keyboards.getStartKeyboard(receivedMessage.getChat().getUserName());
            return HandlerResult.getTextResult(receivedMessage, "Добро пожаловать в Книгу рецептов", keyboard, true);
        }
        return null;
    }
}
