package handlers;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import models.ListItemReadModel;
import pojos.TodoItem;

public class ListHandler implements HttpHandler {

    final ListItemReadModel readModel;

    public ListHandler(ListItemReadModel model) {
        this.readModel = model;
    }

    @Override
    public void handle(HttpExchange t) throws IOException {

        StringBuilder builder = new StringBuilder();

        builder.append("<html lang=\"en\">\n"
                       + "<head>\n"
                       + "    <meta charset=\"UTF-8\">\n"
                       + "    <title>List Items in the List</title>\n"
                       + "</head>\n"
                       + "<body>\n"
                       + "<form action='/add'>");

        builder.append("\n"
                       + "    <input type=\"text\" name=\"itemName\"/>\n"
                       + "    <input type=\"submit\" value=\"Add\" />\n"
                       + "</form>\n"
                       + "\n"
                       + "</body>\n"
                       + "</html>");

        builder.append("<ul>");
        for (TodoItem item: readModel.getUncheckedItems()) {
            builder.append("<li>\n"
                           + "            <a href='/check?id="+ item.getItemId() + "'>"+item.getItemName() +"</a>\n"
                           + "        </li>");
        }


        builder.append("</ul>");

        t.sendResponseHeaders(200, builder.toString().length());
        OutputStream os = t.getResponseBody();
        os.write(builder.toString().getBytes());
        os.close();
    }
}
