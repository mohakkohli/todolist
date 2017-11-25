import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import com.sun.net.httpserver.HttpServer;
import events.Event;
import handlers.AddHandler;
import handlers.CheckHandler;
import handlers.ListHandler;
import models.ListItemReadModel;

public class TodoList {

    public static void main(String[] args) throws Exception {
        System.out.println("Server Start at http://localhost:8000/list");

        List<Event> events = new ArrayList<>();

        ListItemReadModel readModel = new ListItemReadModel();

        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/list", new ListHandler(readModel));
        server.createContext("/check", new CheckHandler(readModel, events));
        server.createContext("/add", new AddHandler(readModel, events));
        server.setExecutor(null); // creates a default executor
        server.start();
    }
}
