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
            case "FindByCuisine":
                handler = new FindByCuisine();
                break;
            case "FindByMeal":
                handler = new FindByMeal();
                break;
            case "FindByType":
                handler = new FindByType();
                break;
            case "FindByIngredients":
                handler = new FindByIngredients();
        }
    }

    public boolean execute(Message receivedMessage, Bot bot) {
        return handler.execute(receivedMessage, bot);
    }

    public Long getId() { return id; }
}
