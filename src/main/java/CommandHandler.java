import org.telegram.telegrambots.meta.api.objects.Message;

public interface CommandHandler {
    public boolean execute(Message receivedMessage, Bot bot);
}
