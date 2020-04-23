import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
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

    public static InlineKeyboardMarkup getInlineKeyboard(List<String> titles, List<String> ids){
        if (titles.size() != ids.size())
            return null;

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        for (int i = 0; i < titles.size(); i++) {
            List<InlineKeyboardButton> buttonsRow = new ArrayList<>();
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(titles.get(i));
            button.setCallbackData(ids.get(i));
            buttonsRow.add(button);
            buttons.add(buttonsRow);
        }
        return new InlineKeyboardMarkup(buttons);
    }

    public static InlineKeyboardMarkup getChosenFilterKeyboard(String ChosenFilter){
        List<String> chosenFilterButtons = Services.recipeService.getButtons(ChosenFilter);
        return getInlineKeyboard(chosenFilterButtons, chosenFilterButtons);
    }
}
