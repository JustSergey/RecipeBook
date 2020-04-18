import Commands.CommandHandler;

public class Chat {
    private String id;
    private CommandHandler handler;

    public Chat(String id, String command) {
        this.id = id;

    }

    public String getId() { return id; }
}
