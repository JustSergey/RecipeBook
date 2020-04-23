import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.List;

public class FindByType implements CommandHandler {
    private int step;
    private int permission;

    public FindByType() {
        step = 3;
        permission = 0;
    }

    public int getPermission() {
        return permission;
    }

    public CommandHandler getInstance(){
        return new FindByType();
    }

    public HandlerResult handle(Message receivedMessage, String data) {
        step--;
        if(step == 2 ){
            ReplyKeyboardMarkup keyboard = Keyboards.getChosenFilterKeyboard("type");
            return HandlerResult.getTextResult(receivedMessage, "Клавиатура обновлена", keyboard, false);
        }
        if (step == 1) {
            String type = receivedMessage.getText();
            List<Recipe> recipes = Services.recipeService.getByType(type);
            return HandlerResult.getRecipeListResult(receivedMessage, recipes, false);
        }
        if (step == 0){
            ReplyKeyboardMarkup keyboard = Keyboards.getStartKeyboard(receivedMessage.getChat().getUserName());
            return HandlerResult.getTextResult(receivedMessage, "Добро пожаловать в Книгу рецептов", keyboard, true);
        }
        return null;
    }
}
