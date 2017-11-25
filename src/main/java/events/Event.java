package events;

public interface Event {
    String getId();
    Type getType();

    enum Type{
        ITEM_ADDED,
        ITEM_CHECKED
    }
}
