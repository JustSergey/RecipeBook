import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public class Update implements CommandHandler {
    private int step;
    private int permission;

    public Update() {
        step = 1;
        permission = 0;
    }

    public int getPermission() {
        return permission;
    }

    public CommandHandler getInstance(){
        return new Update();
    }

    public HandlerResult handle(Message receivedMessage, String data) {
        step--;
        if (step == 0) {
            ReplyKeyboardMarkup keyboard = Keyboards.getStartKeyboard(receivedMessage.getChat().getUserName());
            return HandlerResult.getTextResult(receivedMessage, "Клавиатура обновлена", keyboard, true);
        }
        return null;
    }
}