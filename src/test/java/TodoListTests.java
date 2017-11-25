import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

public class TodoListTests {

    @Test
    public void testSuccessfullyCheckedItem() throws ItemAlreadyCheckedException {
        String itemId = "Test";

        SignedUpEvent signedUpEvent  = new SignedUpEvent();
        LoggedInEvent loggedInEvent= new LoggedInEvent();
        AddedItemEvent addedItemEvent= new AddedItemEvent(itemId, "buy milk");


        Domain domain= new Domain();
        domain.hydrate(Arrays.asList(signedUpEvent, loggedInEvent, addedItemEvent));
        Event event = domain.execute(new CheckTodoItemCommand(itemId));

        Assert.assertTrue(event.getId().equals(itemId));
    }

    @Test(expected = ItemAlreadyCheckedException.class )
    public void testCantCheckedAlreadyCheckedItem() throws ItemAlreadyCheckedException {
        String itemId = "Test";

        SignedUpEvent signedUpEvent  = new SignedUpEvent();
        LoggedInEvent loggedInEvent= new LoggedInEvent();
        AddedItemEvent addedItemEvent= new AddedItemEvent(itemId, "buy milk");

        ItemIsCheckedEvent itemIsCheckedEvent = new ItemIsCheckedEvent(itemId);

        Domain domain= new Domain();
        domain.hydrate(Arrays.asList(signedUpEvent, loggedInEvent, addedItemEvent, itemIsCheckedEvent));
        Event event = domain.execute(new CheckTodoItemCommand(itemId));


        Assert.fail();
    }

    @Test
    public void testUiCantCheckedAlreadyCheckedItem() {
        String itemId = "Test1";
        String itemId2 = "Test2";

        AddedItemEvent addedItemEvent= new AddedItemEvent(itemId, "buy milk");
        AddedItemEvent addedItemEvent2= new AddedItemEvent(itemId2, "buy bread");

        ItemIsCheckedEvent itemIsCheckedEvent = new ItemIsCheckedEvent(itemId);

        ListItemReadModel model = new ListItemReadModel();
        model.handle(Arrays.asList(addedItemEvent, addedItemEvent2, itemIsCheckedEvent));

        Assert.assertTrue(model.getItems().stream().anyMatch(r -> itemId.equals(r.getId()) && r instanceof ItemIsCheckedEvent));
        Assert.assertFalse(model.getItems().stream().anyMatch(r -> itemId2.equals(r.getId()) && r instanceof ItemIsCheckedEvent));
    }

    @Test
    public void testUI() throws ItemAlreadyCheckedException {
        String itemId = "Test1";
        String itemId2 = "Test2";

        AddedItemEvent addedItemEvent= new AddedItemEvent(itemId, "buy milk");
        AddedItemEvent addedItemEvent2= new AddedItemEvent(itemId2, "buy bread");

        ListItemReadModel model = new ListItemReadModel();
        model.handle(Arrays.asList(addedItemEvent, addedItemEvent2));

        Domain domain= new Domain();
        domain.hydrate(model.getItems());

        Button button = new Button(domain);

        Assert.assertFalse(model.getItems().stream().anyMatch(r -> itemId2.equals(r.getId()) && r instanceof ItemIsCheckedEvent));

        button.check(itemId2);

        Assert.assertTrue(model.getItems().stream().anyMatch(r -> itemId2.equals(r.getId()) && r instanceof ItemIsCheckedEvent));
    }

    private class SignedUpEvent implements Event {

        @Override
        public String getId() {
            return null;
        }
    }

    private class LoggedInEvent implements Event  {

        @Override
        public String getId() {
            return null;
        }
    }

    private class AddedItemEvent implements Event  {

        private String itemId;

        public AddedItemEvent(String itemId, String buy_milk) {
            this.itemId = itemId;
        }

        @Override
        public String getId() {
            return itemId;
        }

        public boolean isChecked() {
            return false;
        }
    }

    private class Domain {

        Set<String> checkdItems = new HashSet<>();

        public void hydrate(List<Event> events) {
            for (Event event : events) {
                if (event instanceof ItemIsCheckedEvent) {
                    checkdItems.add(((ItemIsCheckedEvent)event).id);
                }
            }
        }

        public Event execute(CheckTodoItemCommand checkTodoItemCommand) throws ItemAlreadyCheckedException {

            if (checkdItems.contains(checkTodoItemCommand.getItemId())) {
                throw new ItemAlreadyCheckedException();
            }
            return new ItemIsCheckedEvent(checkTodoItemCommand.itemId);
        }
    }

    private interface Event {
        String getId();
    }

    private class CheckTodoItemCommand {

        private String itemId;

        public CheckTodoItemCommand(String itemId) {
            this.itemId = itemId;
        }

        public String getItemId() {
            return itemId;
        }
    }

    private class ItemIsCheckedEvent implements Event {

        public ItemIsCheckedEvent(String id) {
            this.id = id;
        }

        String id;
        public String getId() {
            return id;
        }

        public boolean isChecked() {
            return true;
        }
    }

    private class ItemAlreadyCheckedException extends Exception {

    }

    private class ListItemReadModel {

        List<Event> listeItemEvents = new ArrayList<>();

        public void handle(List<Event> events) {
            listeItemEvents.addAll(events);
        }

        public List<Event> getItems() {
            return listeItemEvents;
        }

    }

    private class Button {

        //private final ListItemReadModel listItemReadModel;
        private final Domain domain;

        /*public Button(ListItemReadModel model) {
            this.listItemReadModel = model;
        }*/

        public Button(Domain domain) {
            this.domain = domain;
        }

        public void check(String itemId) throws ItemAlreadyCheckedException {
            domain.execute(new CheckTodoItemCommand(itemId));
        }
    }
}
