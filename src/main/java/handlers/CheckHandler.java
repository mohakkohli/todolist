package handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import commands.CheckTodoItemCommand;
import domain.Domain;
import exceptions.ItemDoesNotExistException;
import events.Event;
import exceptions.ItemAlreadyCheckedException;
import models.ListItemReadModel;

public class CheckHandler implements HttpHandler {

    private final ListItemReadModel readModel;
    private final List<Event> eventList;

    public CheckHandler(ListItemReadModel model, List<Event> events) {
        this.readModel = model;
        this.eventList = events;
    }

    @Override
    public void handle(HttpExchange t) throws IOException {

        String query = t.getRequestURI().getQuery();
        String itemId = query.substring(query.indexOf("=")+1, query.length());

        StringBuilder  builder = new StringBuilder();
        builder.append("<html lang=\"en\">\n"
                       + "<head>\n"
                       + "    <meta charset=\"UTF-8\">\n"
                       + "    <title>Check Items from the List</title>\n"
                       + "</head>\n"
                       + "<body>\n");
        try {
            Domain domain = new Domain();
            domain.hydrate(eventList);
            Event event = domain.execute(new CheckTodoItemCommand(itemId));
            eventList.add(event);
            readModel.handle(event);
            builder.append("Item: " + readModel.getItem(itemId) + " is checked");
        } catch (ItemAlreadyCheckedException e) {
            builder.append("Item: " + readModel.getItem(itemId) + " is already checked");
        } catch (ItemDoesNotExistException itemDoesnotExist) {
            builder.append("Item Not found");
        }
        builder.append("<br/><br/><a href='/list'> Show Todo List</a>");
        t.sendResponseHeaders(200, builder.toString().length());
        OutputStream os = t.getResponseBody();
        os.write(builder.toString().getBytes());
        os.close();
    }
}
