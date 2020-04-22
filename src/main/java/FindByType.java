import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.List;

public class FindByType implements CommandHandler {
    private int step;
    public FindByType(){step=1;}



    public boolean execute(Message receivedMessage, Bot bot) {
        step--;
        switch(step){
            case 0:
                String filter = receivedMessage.getText();
                List<Recipe> recipes = bot.recipeService.getByType(filter);
                bot.sendRecipeList(receivedMessage,recipes);
                ReplyKeyboardMarkup keyboard = bot.getStartKeyboard(receivedMessage.getChat().getUserName());
                bot.sendMessage(receivedMessage, "Добро пожаловать в Книгу рецептов", keyboard);
                return false;
        }
        return false;
    }
}