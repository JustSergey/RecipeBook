import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public class Start implements CommandHandler {
    private int step;
    private int permission;

    public Start() {
        step = 1;
        permission = 0;
    }

    public int getPermission() {
        return permission;
    }

    public CommandHandler getInstance(){
        return new Start();
    }

    public HandlerResult handle(Message receivedMessage, String data) {
        step--;
        if (step == 0) {
            ReplyKeyboardMarkup keyboard = Keyboards.getStartKeyboard(receivedMessage.getChat().getUserName());
            return HandlerResult.getTextResult(receivedMessage, "Добро пожаловать в Книгу рецептов", keyboard, true);
        }
        return null;
    }
}