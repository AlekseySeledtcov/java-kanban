package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

abstract class SimpleGetHandler<T> extends BaseHttpHandler implements HttpHandler {
    protected abstract T getData();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().equals("GET")) {
            sendText(exchange, serialize(getData()), 200);
        } else {
            exchange.sendResponseHeaders(405, -1); // Method Not Allowed
        }
    }
}
