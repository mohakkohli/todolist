package domain;

import java.util.List;

import commands.AddTodoItemCommand;
import commands.Command;
import events.AddedItemEvent;
import events.Event;
import events.ItemIsCheckedEvent;
import exceptions.ItemAlreadyCheckedException;
import exceptions.ItemDoesNotExistException;

public class Domain {

    private List<Event> eventList;

    public void hydrate(List<Event> events) {
        eventList = events;
    }

    public Event execute(Command command) throws ItemAlreadyCheckedException, ItemDoesNotExistException {

        for (Event event : eventList) {
            if (event.getId().equals(command.getItemId()) && event instanceof ItemIsCheckedEvent) {
                throw new ItemAlreadyCheckedException();
            }
        }
        if (command.getType().equals(Command.Type.ADD_ITEM)) {
            return new AddedItemEvent(command.getItemId(), ((AddTodoItemCommand)command).getItemName());
        }

        if (command.getType().equals(Command.Type.CHECK_ITEM)) {
            for (Event event : eventList) {
                if (event.getId().equals(command.getItemId()) && event instanceof AddedItemEvent) {
                    return new ItemIsCheckedEvent(command.getItemId());
                }
            }
            throw new ItemDoesNotExistException();
        }
        return null;
    }
}
