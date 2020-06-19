import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Bot extends TelegramLongPollingBot {
    private MessageHandler messageHandler = new MessageHandler();

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new Bot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void onUpdateReceived(Update update) {
        SendMessage sendMessage = null;
        if (update.hasMessage()) {
            Message receivedMessage = update.getMessage();
            if (receivedMessage.hasText())
                sendMessage = messageHandler.handle(receivedMessage, receivedMessage.getText(), MessageHandler.Type.Message);
        }
        else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            sendMessage = messageHandler.handle(callbackQuery.getMessage(), callbackQuery.getData(), MessageHandler.Type.CallbackQuery);
        }
        if (sendMessage != null) {
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    public String getBotUsername() {
        return "RecipeBookBestBot";
    }

    public String getBotToken() {
        return "993294029:AAHcypS57Dh4nrF1ky2i_BXiPv0w-3QM9FQ";
    }
}
