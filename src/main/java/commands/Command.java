package commands;

public interface Command {
    enum Type {
        ADD_ITEM,
        CHECK_ITEM
    }

    String getItemId();
    Type getType();
}
