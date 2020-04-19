import org.telegram.telegrambots.meta.api.objects.Message;

public class RemoveRecipe implements CommandHandler {
    private int step;
    private int id;

    public RemoveRecipe() {step = 2;}

    public boolean execute(Message receivedMessage, Bot bot) {
        step--;
        switch (step) {
            case 1:
                try {
                    id = Integer.parseInt(receivedMessage.getText());
                    String title = bot.recipeService.get(id).getTitle();
                    bot.sendMessage(receivedMessage, "Удалить рецепт " + title + "? (Да/Нет)", null);
                    return true;
                } catch (Exception e){
                    bot.sendMessage(receivedMessage, "Не удалось удалить рецепт", null);
                    return true;
                }
            case 0:
                if (receivedMessage.getText().equals("Да")){
                    bot.recipeService.delete(id);
                    bot.sendMessage(receivedMessage, "Рецепт удален", null);
                }
                else
                    bot.sendMessage(receivedMessage, "Удаление отменено", null);
                return false;
        }
        return false;
    }
}
