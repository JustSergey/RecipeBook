import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {
    static RecipeService recipeService = new RecipeService();

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new Bot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(Message receivedMessage, String text) {
        SendMessage message = new SendMessage();
        //message.enableMarkdown(true);
        message.setChatId(receivedMessage.getChatId().toString());
        message.setText(text);
        try {
            setButtons(message);
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasText()) {
                switch (message.getText()) {
                    case "/start":
                        sendMessage(message, "Добро пожаловать в Книгу рецептов");
                        break;
                    case "Список":
                        List<Recipe> recipes = recipeService.getAll();
                        sendList(message, recipes);
                        break;
                }
            }
        } else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            int id = Integer.parseInt(callbackQuery.getData());
            Recipe recipe = recipeService.get(id);
            sendMessage(callbackQuery.getMessage(), getRecipeInfo(recipe));
        }
    }

    private String getRecipeInfo(Recipe recipe) {
        StringBuilder info = new StringBuilder(recipe.getTitle() + "\n" +
                "--------------------\n" +
                "Тип:    " + recipe.getType() + "\n" +
                "Трапеза:    " + recipe.getMeal() + "\n" +
                "Кухня:    " + recipe.getCuisine() + "\n" +
                "Порций:    " + recipe.getPortions() + "\n" +
                "Время готовки:    " + recipe.getTime() + "\n" +
                "Автор:    " + recipe.getAuthor().getUserName() + "\n" +
                "--------------------\n" +
                "Ингредиенты:\n");

        List<RecipeIngredient> ingredients = recipe.getIngredients();
        for (RecipeIngredient ingredient : ingredients) {
            info.append(ingredient.getIngredient().getTitle()).append(":    ").append(ingredient.getAmount()).append("\n");
        }
        info.append("--------------------\n" + "Инструкция:\n").append(recipe.getInstruction());
        return info.toString();
    }

    public void sendList(Message receivedMessage, List<Recipe> recipes) {
        SendMessage message = new SendMessage();
        message.enableMarkdown(true);
        message.setChatId(receivedMessage.getChatId().toString());
        message.setText("Выберите рецепт:");

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
        message.setReplyMarkup(keyboard);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void setButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton("Список"));
        keyboardRowList.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);

        sendMessage.setReplyMarkup(replyKeyboardMarkup);
    }

    public String getBotUsername() {
        return "RecipeBookBestBot";
    }

    public String getBotToken() {
        return "993294029:AAHcypS57Dh4nrF1ky2i_BXiPv0w-3QM9FQ";
    }
}
