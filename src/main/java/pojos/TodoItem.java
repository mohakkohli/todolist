package pojos;

public class TodoItem {
    private String itemId;
    private String itemName;
    private boolean isChecked;

    public TodoItem(String itemId, String itemName, boolean isChecked) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.isChecked = isChecked;
    }
    public String getItemId() {
        return itemId;
    }
    public String getItemName() {
        return itemName;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }


    @Override
    public String toString() {
        return itemName;
    }
}
