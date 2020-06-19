import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class MessageHandler {
    public enum Type {
        Message,
        CallbackQuery
    }

    private List<Chat> processingChats = new ArrayList<>();
    private Dictionary<String, CommandHandler> commands;

    public MessageHandler() {
        commands = new Hashtable<>();
        commands.put("/start", new Start());
        commands.put("/update", new Update());
        commands.put("Полный список", new FullList());
        commands.put("Добавить рецепт", new AddRecipe());
        commands.put("Удалить рецепт", new RemoveRecipe());
        commands.put("Поиск по фильтрам", new ShowFilters());
        commands.put("Поиск по ингредиентам", new FindByIngredients());
    }

    public SendMessage handle(Message receivedMessage, String data, Type type){
        HandlerResult handlerResult = null;
        Chat chat = FindChat(processingChats, receivedMessage.getChatId());
        if (chat != null) {
            handlerResult = chat.handle(receivedMessage, data);
            if (handlerResult == null || handlerResult.isEnd) {
                processingChats.remove(chat);
            }
        } else {
            if (type.equals(Type.Message)) {
                CommandHandler handler = commands.get(data);
                if (handler != null) {
                    handler = handler.getInstance();
                    int permission = handler.getPermission();
                    if (permission > 0) {
                        User user = Services.getUser(receivedMessage.getChat().getUserName());
                        if (user != null && user.getPermission().equals("admin"))
                            handlerResult = handler.handle(receivedMessage, data);
                    } else
                        handlerResult = handler.handle(receivedMessage, data);

                    if (handlerResult != null && !handlerResult.isEnd) {
                        processingChats.add(new Chat(receivedMessage.getChatId(), handler));
                    }
                }
            }
            else if (type.equals(Type.CallbackQuery)) {
                try {
                    int id = Integer.parseInt(data);
                    Recipe recipe = Services.recipeService.get(id);
                    handlerResult = HandlerResult.getTextResult(receivedMessage, getRecipeInfo(recipe), true);
                }
                catch (Exception e) {
                    handlerResult = HandlerResult.getTextResult(receivedMessage, "Не удалось получить рецепт", true);
                }
            }
        }
        if (handlerResult == null)
            return null;
        return handlerResult.sendMessage;
    }

    private Chat FindChat(List<Chat> chats, Long id) {
        for (Chat chat : chats)
            if (chat.getId().equals(id))
                return  chat;
        return null;
    }

    private String getRecipeInfo(Recipe recipe) {
        StringBuilder info = new StringBuilder(recipe.getTitle() + "\n");
        info.append("--------------------\n");
        info.append("Тип:    ").append(recipe.getType()).append("\n");
        info.append("Трапеза:    ").append(recipe.getMeal()).append("\n");
        info.append("Кухня:    ").append(recipe.getCuisine()).append("\n");
        info.append("Порций:    ").append(recipe.getPortions()).append("\n");
        info.append("Время готовки:    ").append(recipe.getTime()).append("\n");
        info.append("Автор:    ").append(recipe.getAuthor().getUserName()).append("\n");
        info.append("--------------------\n");
        info.append("Ингредиенты:\n");

        List<RecipeIngredient> ingredients = recipe.getIngredients();
        for (RecipeIngredient ingredient : ingredients) {
            info.append(ingredient.getIngredient().getTitle()).append(":    ").append(ingredient.getAmount()).append("\n");
        }
        info.append("--------------------\n").append("Инструкция:\n").append(recipe.getInstruction());
        return info.toString();
    }
}