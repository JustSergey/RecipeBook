import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.ArrayList;
import java.util.List;

public class ShowFilters implements CommandHandler {
    private int step;
    private int permission;
    private String filter;

    public ShowFilters() {
        step = 3;
        permission = 0;
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
                List<String> filters = new ArrayList<>();
                filters.add("Кухня");
                filters.add("Трапеза");
                filters.add("Тип");
                List<String> ids = new ArrayList<>();
                ids.add("1");
                ids.add("2");
                ids.add("3");
                keyboard = Keyboards.getInlineKeyboard(filters, ids);
                return HandlerResult.getTextResult(receivedMessage, "Выберите фильтр:", keyboard, false);
            case 1:
                switch (data) {
                    case "1":
                        filter = "meal";
                        keyboard = Keyboards.getChosenFilterKeyboard(filter);
                        return HandlerResult.getTextResult(receivedMessage, "Выберите трапезу", keyboard, false);
                    case "2":
                        filter = "type";
                        keyboard = Keyboards.getChosenFilterKeyboard(filter);
                        return HandlerResult.getTextResult(receivedMessage, "Выберите тип", keyboard, false);
                    case "3":
                        filter = "cuisine";
                        keyboard = Keyboards.getChosenFilterKeyboard(filter);
                        return HandlerResult.getTextResult(receivedMessage, "Выберите кухню", keyboard, false);
                }
                return null;
            case 0:
                List<Recipe> recipes;
                switch (filter) {
                    case "meal":
                        recipes = Services.recipeService.getByMeal(data);
                        return HandlerResult.getRecipeListResult(receivedMessage, recipes, true);
                    case "type":
                        recipes = Services.recipeService.getByType(data);
                        return HandlerResult.getRecipeListResult(receivedMessage, recipes, true);
                    case "cuisine":
                        recipes = Services.recipeService.getByCuisine(data);
                        return HandlerResult.getRecipeListResult(receivedMessage, recipes, true);
                }
                return null;
        }
        return null;
    }
}