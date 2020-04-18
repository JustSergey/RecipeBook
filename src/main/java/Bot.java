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
    public RecipeService recipeService = new RecipeService();
    public UserService userService = new UserService();
    public IngredientService ingredientService = new IngredientService();
    public RecipeIngredientService recipeIngredientService = new RecipeIngredientService();
    public List<Chat> processingChats = new ArrayList<>();

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new Bot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(Message receivedMessage, String text, ReplyKeyboardMarkup keyboard) {
        SendMessage sendMessage = new SendMessage();
        //message.enableMarkdown(true);
        sendMessage.setChatId(receivedMessage.getChatId().toString());
        sendMessage.setText(text);
        if (keyboard != null)
            sendMessage.setReplyMarkup(keyboard);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasText()) {
                Chat chat = FindChat(processingChats, message.getChatId());
                if (chat != null) {
                    if (!chat.execute(message, this))
                        processingChats.remove(chat);
                } else {
                    ReplyKeyboardMarkup keyboard;
                    switch (message.getText()) {
                        case "/start":
                            keyboard = getKeyboard(message.getChat().getUserName());
                            sendMessage(message, "Добро пожаловать в Книгу рецептов", keyboard);
                            break;
                        case "/update":
                            keyboard = getKeyboard(message.getChat().getUserName());
                            sendMessage(message, "Клавиатура обновлена", keyboard);
                            break;
                        case "Список":
                            List<Recipe> recipes = recipeService.getAll();
                            sendRecipeList(message, recipes);
                            break;
                        case "Добавить рецепт":
                            User user = getUser(message.getChat().getUserName());
                            if (user != null && user.getPermission().equals("admin")){
                                processingChats.add(new Chat(message.getChatId(), "AddRecipe"));
                                sendMessage(message, "Введите название", null);
                            }
                            break;
                        case "Удалить рецепт":
                            break;
                    }
                }
            }
        } else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            int id = Integer.parseInt(callbackQuery.getData());
            Recipe recipe = recipeService.get(id);
            sendMessage(callbackQuery.getMessage(), getRecipeInfo(recipe), null);
        }
    }

    private Chat FindChat(List<Chat> chats, Long id) {
        for (Chat chat : chats) {
            if (chat.getId().equals(id)) {
                return  chat;
            }
        }
        return null;
    }

    public User getUser(String userName){
        List<User> users = userService.getAll();
        for (User user : users) {
            if (user.getUserName().equals(userName)){
                return user;
            }
        }
        return null;
    }

    private String getRecipeInfo(Recipe recipe) {
        StringBuilder info = new StringBuilder(recipe.getTitle() + "\n");
        info.append("--------------------\n");
        info.append("Тип:    ").append(recipe.getType()).append("\n");
        info.append("Трапеза:    ").append(recipe.getMeal()).append("\n");
        info.append("Кухня:    ").append(recipe.getCuisine()).append("\n");
        info.append("Порций:    ").append(recipe.getPortions()).append("\n");
        info.append("Время готовки:    ").append(recipe.getTime()).append("\n");
        info.append("Автор:    ").append(recipe.getAuthor().getUserName()).append("\n");
        info.append("--------------------\n");
        info.append("Ингредиенты:\n");

        List<RecipeIngredient> ingredients = recipe.getIngredients();
        for (RecipeIngredient ingredient : ingredients) {
            info.append(ingredient.getIngredient().getTitle()).append(":    ").append(ingredient.getAmount()).append("\n");
        }
        info.append("--------------------\n").append("Инструкция:\n").append(recipe.getInstruction());
        return info.toString();
    }

    public void sendRecipeList(Message receivedMessage, List<Recipe> recipes) {
        SendMessage message = new SendMessage();
        message.enableMarkdown(true);
        message.setChatId(receivedMessage.getChatId().toString());
        message.setText("Выберите рецепт:");

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        for (Recipe recipe : recipes) {
            List<InlineKeyboardButton> buttonsRow = new ArrayList<>();
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(recipe.getTitle());
            button.setCallbackData("r" + recipe.getId());
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

    private ReplyKeyboardMarkup getKeyboard(String userName) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        String permission = "user";
        User user = getUser(userName);
        if (user != null) {
            permission = user.getPermission();
        }

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton("Список"));
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

    public String getBotUsername() {
        return "RecipeBookBestBot";
    }

    public String getBotToken() {
        return "993294029:AAHcypS57Dh4nrF1ky2i_BXiPv0w-3QM9FQ";
    }
}
