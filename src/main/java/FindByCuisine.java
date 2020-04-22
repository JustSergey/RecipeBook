import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class FindByCuisine implements CommandHandler {
    private int step;
    public FindByCuisine(){step=1;}



    public boolean execute(Message receivedMessage, Bot bot) {
        step--;
        switch(step){
            case 0:
                String cuisine = receivedMessage.getText();
                List<Recipe> recipes = bot.recipeService.getByCuisine(cuisine);
                bot.sendRecipeList(receivedMessage,recipes);
                ReplyKeyboardMarkup keyboard = bot.getStartKeyboard(receivedMessage.getChat().getUserName());
                bot.sendMessage(receivedMessage, "Добро пожаловать в Книгу рецептов", keyboard);
                return false;
        }
        return false;
    }
}