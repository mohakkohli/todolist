package commands;

public class CheckTodoItemCommand implements Command {

    private String itemId;

    public CheckTodoItemCommand(String itemId) {
        this.itemId = itemId;
    }

    @Override
    public String getItemId() {
        return itemId;
    }

    @Override
    public Type getType() {
        return Type.CHECK_ITEM;
    }
}
