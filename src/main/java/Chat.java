import org.telegram.telegrambots.meta.api.objects.Message;

public class Chat {
    private Long id;
    private CommandHandler handler;

    public Chat(Long id, String command) {
        this.id = id;
        switch (command){
            case "AddRecipe":
                handler = new AddRecipe();
                break;
            case "RemoveRecipe":
                handler = new RemoveRecipe();
                break;
        }
    }

    public boolean execute(Message receivedMessage, Bot bot) {
        return handler.execute(receivedMessage, bot);
    }

    public Long getId() { return id; }
}
