package events;

public class ItemIsCheckedEvent implements Event {

    private String id;

    public ItemIsCheckedEvent(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Type getType() {
        return Type.ITEM_CHECKED;
    }
}
