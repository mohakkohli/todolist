package events;

public class AddedItemEvent implements Event {

    private String itemId;
    private String itemName;

    public AddedItemEvent(String itemId, String itemName) {
        this.itemId = itemId;
        this.itemName = itemName;
    }

    @Override
    public String getId() {
        return itemId;
    }

    @Override
    public Type getType() {
        return Type.ITEM_ADDED;
    }

    public String getItemName() {
        return itemName;
    }
}
