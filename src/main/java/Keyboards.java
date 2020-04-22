import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class Keyboards {
    public static ReplyKeyboardMarkup getStartKeyboard(String userName) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        String permission = "user";
        User user = Services.getUser(userName);
        if (user != null) {
            permission = user.getPermission();
        }

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton("Полный список"));
        keyboardRow.add(new KeyboardButton("Поиск по фильтрам"));
        keyboardRow.add(new KeyboardButton("Поиск по ингредиентам"));

        keyboardRowList.add(keyboardRow);
        if (permission.equals("admin")) {
            keyboardRow = new KeyboardRow();
            keyboardRow.add(new KeyboardButton("Добавить рецепт"));
            keyboardRow.add(new KeyboardButton("Удалить рецепт"));
            keyboardRowList.add(keyboardRow);
        }
        replyKeyboardMarkup.setKeyboard(keyboardRowList);

        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup getFiltersKeyboard(){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton("Кухня"));
        keyboardRow.add(new KeyboardButton("Трапеза"));
        keyboardRow.add(new KeyboardButton("Тип"));
        keyboardRowList.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup getChosenFilterKeyboard(String ChosenFilter){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();

        List<String> chosenFilterButtons = Services.recipeService.getButtons(ChosenFilter);
        for (String filterButton: chosenFilterButtons){
            keyboardRow.add(new KeyboardButton(filterButton));
        };
        keyboardRowList.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        return replyKeyboardMarkup;
    }
}
