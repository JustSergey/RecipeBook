import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public class ShowFilters implements CommandHandler {
    private int step;
    private int permission;

    public ShowFilters() {
        step = 1;
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
        if (step == 0) {
            ReplyKeyboardMarkup keyboard = Keyboards.getFiltersKeyboard();
            return HandlerResult.getTextResult(receivedMessage, "Клавиатура обновлена", keyboard, true);
        }
        return null;
    }
}