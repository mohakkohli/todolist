package handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import commands.AddTodoItemCommand;
import domain.Domain;
import exceptions.ItemDoesNotExistException;
import events.Event;
import exceptions.ItemAlreadyCheckedException;
import models.ListItemReadModel;

public class AddHandler implements HttpHandler {

    private final ListItemReadModel readModel;
    private final List<Event> eventList;

    public AddHandler(ListItemReadModel model, List<Event> events) {
        this.readModel = model;
        this.eventList = events;
    }

    @Override
    public void handle(HttpExchange t) throws IOException {

        String query = t.getRequestURI().getQuery();
        String itemName = query.substring(query.indexOf("=")+1, query.length());

        StringBuilder  builder = new StringBuilder();
        builder.append("<html lang=\"en\">\n"
                       + "<head>\n"
                       + "    <meta charset=\"UTF-8\">\n"
                       + "    <title>Add Item to the List</title>\n"
                       + "</head>\n"
                       + "<body>\n");
        try {
            Domain domain = new Domain();
            domain.hydrate(eventList);
            Event event = domain.execute(new AddTodoItemCommand(itemName));
            eventList.add(event);
            readModel.handle(event);
            builder.append("Item: " + itemName + " is added");
        } catch (ItemAlreadyCheckedException e) {
            builder.append("Item: " + itemName + " already added");
        } catch (ItemDoesNotExistException itemDoesnotExist) {
            builder.append("Item: " + itemName + " doesn't exist");
        }
        builder.append("<br/><br/><a href='/list'> Show Todo List</a ></body ></html>");
        t.sendResponseHeaders(200, builder.toString().length());
        OutputStream os = t.getResponseBody();
        os.write(builder.toString().getBytes());
        os.close();
    }
}
