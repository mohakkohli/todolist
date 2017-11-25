package commands;

import java.util.UUID;

public class AddTodoItemCommand implements Command {

    private String itemId;
    private String itemName;

    public AddTodoItemCommand(String itemName) {
        this.itemId = UUID.randomUUID().toString();
        this.itemName = itemName;
    }

    public String getItemName() {
        return itemName;
    }

    @Override
    public String getItemId() {
        return itemId;
    }

    @Override
    public Type getType() {
        return Type.ADD_ITEM;
    }
}
