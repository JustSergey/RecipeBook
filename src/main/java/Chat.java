import org.telegram.telegrambots.meta.api.objects.Message;

public class Chat {
    private Long id;
    private CommandHandler handler;

    public Chat(Long id, CommandHandler handler) {
        this.id = id;
        this.handler = handler;
    }

    public HandlerResult handle(Message receivedMessage, String data) {
        return handler.handle(receivedMessage, data);
    }

    public Long getId() {
        return id;
    }
}