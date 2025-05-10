package server;

import com.sun.net.httpserver.HttpServer;
import file.NotFoundException;
import manager.Managers;
import manager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private TaskManager manager;
    private HttpServer httpServer;
    private static final int PORT = 8080;

    public HttpTaskServer(TaskManager manager) {
        this.manager = manager;
    }

    public static void main(String[] args) throws IOException, NotFoundException {
        TaskManager manager = Managers.getDefaultManager();
        HttpTaskServer server = new HttpTaskServer(manager);

        server.start();
        Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
    }

    public void start() throws IOException {
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);

        httpServer.createContext("/tasks", new TaskHandler(manager));
        httpServer.createContext("/subtasks", new SubtaskHandler(manager));
        httpServer.createContext("/epics", new EpicHandler(manager));
        httpServer.createContext("/history", new HistoryHandler(manager));
        httpServer.createContext("/prioritized", new PrioritizedHandler(manager));
        httpServer.start();

        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public void stop() {
        httpServer.stop(0);
    }
}
