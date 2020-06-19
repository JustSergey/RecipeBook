import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class HandlerResult {
    public SendMessage sendMessage;
    public boolean isEnd;

    public HandlerResult(SendMessage sendMessage, boolean isEnd) {
        this.sendMessage = sendMessage;
        this.isEnd = isEnd;
    }

    public HandlerResult(SendMessage sendMessage) {
        this.sendMessage = sendMessage;
        this.isEnd = true;
    }

    public static HandlerResult getTextResult(Message receivedMessage, String text, boolean isEnd) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(receivedMessage.getChatId());
        sendMessage.setText(text);
        return new HandlerResult(sendMessage, isEnd);
    }

    public static HandlerResult getTextResult(Message receivedMessage, String text, ReplyKeyboard keyboard, boolean isEnd) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(receivedMessage.getChatId());
        sendMessage.setText(text);
        if (keyboard != null)
            sendMessage.setReplyMarkup(keyboard);
        return new HandlerResult(sendMessage, isEnd);
    }

    public static HandlerResult getRecipeListResult(Message receivedMessage, List<Recipe> recipes, boolean isEnd) {
        SendMessage sendMessage = new SendMessage();
        //sendMessage.enableMarkdown(true);
        sendMessage.setChatId(receivedMessage.getChatId());
        sendMessage.setText("Выберите рецепт:");

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        for (Recipe recipe : recipes) {
            List<InlineKeyboardButton> buttonsRow = new ArrayList<>();
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(recipe.getTitle());
            button.setCallbackData(Integer.toString(recipe.getId()));
            buttonsRow.add(button);
            buttons.add(buttonsRow);
        }
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup(buttons);
        sendMessage.setReplyMarkup(keyboard);
        return new HandlerResult(sendMessage, isEnd);
    }
}
