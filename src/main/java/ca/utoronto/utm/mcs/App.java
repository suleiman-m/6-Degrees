package ca.utoronto.utm.mcs;

public class App
{
    static int port = 8080;

    public static void main(String[] args) {
        // TODO Create Your Server Context Here, There Should Only Be One Context
        ServerComponent componentS = DaggerServerComponent.create();
        ReqHandlerComponent componentR = DaggerReqHandlerComponent.create();

        ReqHandler handler = componentR.buildHandler();
        Server server = componentS.buildServer();
        server.server.createContext("/api/v1", handler);
        server.server.start();
        System.out.printf("Server started on port %d...\n", port);
    }
}
