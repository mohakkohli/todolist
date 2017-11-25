package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import events.AddedItemEvent;
import events.Event;
import pojos.TodoItem;

public class ListItemReadModel {

    private Map<String, TodoItem> todoItemMap = new HashMap<>();

    public void handle(Event event) {
            String id = event.getId();
            if (event.getType().equals(Event.Type.ITEM_ADDED)) {
                TodoItem item = new TodoItem(id,
                                             ((AddedItemEvent)event).getItemName(),
                                             false);
                todoItemMap.put(id, item);
            }
            if (event.getType().equals(Event.Type.ITEM_CHECKED) && todoItemMap.containsKey(id)) {
                TodoItem item = todoItemMap.get(id);
                item.setChecked(true);
            }
    }

    public List<TodoItem> getAllItems() {
        return new ArrayList<>(todoItemMap.values());
    }

    public List<TodoItem> getUncheckedItems() {
        return new ArrayList<>(todoItemMap
                                       .values()
                                       .stream()
                                       .filter(item -> !item.isChecked())
                                       .collect(Collectors.toList())
        );
    }

    public List<TodoItem> getCheckedItems() {
        return new ArrayList<>(todoItemMap
                                       .values()
                                       .stream()
                                       .filter(TodoItem::isChecked)
                                       .collect(Collectors.toList())
        );
    }

    public TodoItem getItem(String itemId) {
        return todoItemMap.get(itemId);
    }
}
