import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class ShowFilters implements CommandHandler {
    private int step;
    private int permission;
    private String filter;
    private Dictionary<String, String> info;

    public ShowFilters() {
        step = 3;
        permission = 0;
        info = new Hashtable<>();
        info.put("meal", "трапезу");
        info.put("type", "тип");
        info.put("cuisine", "кухню");
    }

    public int getPermission() {
        return permission;
    }

    public CommandHandler getInstance(){
        return new ShowFilters();
    }

    public HandlerResult handle(Message receivedMessage, String data) {
        step--;
        InlineKeyboardMarkup keyboard;
        switch (step) {
            case 2:
                keyboard = Keyboards.getFiltersKeyboard();
                return HandlerResult.getTextResult(receivedMessage, "Выберите фильтр:", keyboard, false);
            case 1:
                filter = data;
                keyboard = Keyboards.getChosenFilterKeyboard(filter);
                return HandlerResult.getTextResult(receivedMessage, "Выберите " + info.get(filter), keyboard, false);
            case 0:
                List<Recipe> recipes = Services.recipeService.getByParameter(filter, data);
                return HandlerResult.getRecipeListResult(receivedMessage, recipes, true);
        }
        return null;
    }
}