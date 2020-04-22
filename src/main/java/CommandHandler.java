import org.telegram.telegrambots.meta.api.objects.Message;

public interface CommandHandler {
    int getPermission();
    CommandHandler getInstance();
    HandlerResult handle(Message receivedMessage, String data);
}