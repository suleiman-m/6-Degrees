package ca.utoronto.utm.mcs;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import org.json.JSONException;
import org.json.*;

import javax.inject.Inject;
import java.io.OutputStream;

public class ReqHandler implements HttpHandler {

    private Neo4jDAO dao;
    // TODO Complete This Class
    @Inject
    public ReqHandler(Neo4jDAO dao){
        this.dao = dao;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            switch (exchange.getRequestMethod()) {
                case "GET":
                    String getURI = exchange.getRequestURI().toString();
                    switch (getURI) {
                        case "http://localhost:8080/api/v1/getActor" -> this.getActor(exchange);
                        case "http://localhost:8080/api/v1/getMovie" -> this.getMovie(exchange);
                        case "http://localhost:8080/api/v1/hasRelationship" -> this.hasRelationship(exchange);
                        case "http://localhost:8080/api/v1/computeBaconNumber" -> this.computeBaconNumber(exchange);
                        case "http://localhost:8080/api/v1/computeBaconPath" -> this.computeBaconPath(exchange);
                        default -> System.out.print("ERROR: getURI in handle in ReqHandler.java line 17 could not be resolved.");
                    }
                case "POST":
                    String postURI = exchange.getRequestURI().toString();
                    switch (postURI) {
                        case "http://localhost:8080/api/v1/addActor" -> this.addActor(exchange);
                        case "http://localhost:8080/api/v1/addMovie" -> this.addMovie(exchange);
                        case "http://localhost:8080/api/v1/addRelationship" -> this.addRelationship(exchange);
                        default -> System.out.print("ERROR: postURI in handle in ReqHandler.java line 17 could not be resolved.");
                    }
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("There was a problem with exchange.getRequestMethod() in ReqHandler.java line 17.");
        }
    }

    public String convertPrintable(JSONObject response){
        String printable = "";

        try {
            printable = response.toString(4);
            printable = printable.charAt(0) + "\n" + printable.substring(1);
            printable = printable.substring(0, printable.length()-1) + "\n" +
                    printable.charAt(printable.length()-1);
            printable = printable.charAt(0) + printable.substring(1, printable.length()-1).indent(4) +
                    printable.charAt(printable.length()-1);
        } catch (Exception e){
            e.printStackTrace();
        }

        return printable;
    }

    public void getActor(HttpExchange r) throws IOException{
        String body = Utils.convert(r.getRequestBody());
        try {
            JSONObject deserialized = new JSONObject(body);

            String actorId;

            if (deserialized.length() >= 1 && deserialized.has("actorId")) {
                actorId = deserialized.getString("actorId");
            } else {
                r.sendResponseHeaders(400, -1);
                return;
            }

            try {
                JSONObject response = this.dao.findActor(actorId);
                String reply = convertPrintable(response);
                r.sendResponseHeaders(200, reply.length());
                OutputStream os = r.getResponseBody();
                os.write(reply.getBytes());
                os.close();
            } catch (Exception e){
                e.printStackTrace();
                r.sendResponseHeaders(404, -1);
            }

        } catch (Exception e) {
            e.printStackTrace();
            r.sendResponseHeaders(500, -1);
        }
    }

    public void getMovie(HttpExchange r) throws IOException{
        String body = Utils.convert(r.getRequestBody());
        try {
            JSONObject deserialized = new JSONObject(body);

            String movieId;

            if (deserialized.length() >= 1 && deserialized.has("movieId")) {
                movieId = deserialized.getString("movieId");
            } else {
                r.sendResponseHeaders(400, -1);
                return;
            }

            try {
                JSONObject response = this.dao.findMovie(movieId);
                String reply = convertPrintable(response);
                r.sendResponseHeaders(200, reply.length());
                OutputStream os = r.getResponseBody();
                os.write(reply.getBytes());
                os.close();
            } catch (Exception e){
                e.printStackTrace();
                r.sendResponseHeaders(404, -1);
            }

        } catch (Exception e) {
            e.printStackTrace();
            r.sendResponseHeaders(500, -1);
        }
    }

    public void hasRelationship(HttpExchange r) throws IOException{
        String body = Utils.convert(r.getRequestBody());
        try {
            JSONObject deserialized = new JSONObject(body);

            String actorId, movieId;

            if (deserialized.length() >= 2 && deserialized.has("actorId") && deserialized.has("movieId")) {
                actorId = deserialized.getString("actorId");
                movieId = deserialized.getString("movieId");
            } else {
                r.sendResponseHeaders(400, -1);
                return;
            }

            try {
                this.dao.findActor(actorId);
                this.dao.findMovie(movieId);
            } catch (Exception e){
                e.printStackTrace();
                r.sendResponseHeaders(404, -1);
                return;
            }

            JSONObject response = this.dao.findRelationship(actorId, movieId);
            String reply = convertPrintable(response);
            r.sendResponseHeaders(200, reply.length());
            OutputStream os = r.getResponseBody();
            os.write(reply.getBytes());
            os.close();

        } catch (Exception e) {
            e.printStackTrace();
            r.sendResponseHeaders(500, -1);
        }
    }

    public void computeBaconNumber(HttpExchange r) throws IOException{
        String body = Utils.convert(r.getRequestBody());
        try {
            JSONObject deserialized = new JSONObject(body);

            String actorId;

            if (deserialized.length() >= 1 && deserialized.has("actorId")) {
                actorId = deserialized.getString("actorId");
            } else {
                r.sendResponseHeaders(400, -1);
                return;
            }

            try {
                this.dao.findActor(actorId);
            } catch (Exception e){
                e.printStackTrace();
                r.sendResponseHeaders(404, -1);
                return;
            }

            JSONObject response = this.dao.computeBaconNumber(actorId);
            String reply = convertPrintable(response);
            r.sendResponseHeaders(200, reply.length());
            OutputStream os = r.getResponseBody();
            os.write(reply.getBytes());
            os.close();

        } catch (Exception e) {
            e.printStackTrace();
            r.sendResponseHeaders(500, -1);
        }
    }

    public void computeBaconPath(HttpExchange r) throws IOException{
        String body = Utils.convert(r.getRequestBody());
        try {
            JSONObject deserialized = new JSONObject(body);

            String actorId;

            if (deserialized.length() >= 1 && deserialized.has("actorId")) {
                actorId = deserialized.getString("actorId");
            } else {
                r.sendResponseHeaders(400, -1);
                return;
            }

            try {
                this.dao.findActor(actorId);
                JSONObject response = this.dao.computeBaconPath(actorId);
                String reply = convertPrintable(response);
                r.sendResponseHeaders(200, reply.length());
                OutputStream os = r.getResponseBody();
                os.write(reply.getBytes());
                os.close();
            } catch (Exception e){
                e.printStackTrace();
                r.sendResponseHeaders(404, -1);
            }

        } catch (Exception e) {
            e.printStackTrace();
            r.sendResponseHeaders(500, -1);
        }
    }

    public void addActor(HttpExchange r) throws IOException{
        String body = Utils.convert(r.getRequestBody());
        try {
            JSONObject deserialized = new JSONObject(body);

            String name, actorId;

            if (deserialized.length() >= 2 && deserialized.has("name") && deserialized.has("actorId")) {
                name = deserialized.getString("name");
                actorId = deserialized.getString("actorId");
            } else {
                r.sendResponseHeaders(400, -1);
                throw new JSONException("JSON not able to be parsed.");
            }

            try {
                this.dao.insertActor(name, actorId);
            } catch (Exception e) {
                r.sendResponseHeaders(500, -1);
                e.printStackTrace();
                return;
            }
            r.sendResponseHeaders(200, -1);
        } catch (Exception e) {
            e.printStackTrace();
            r.sendResponseHeaders(500, -1);
        }
    }

    public void addMovie(HttpExchange r) throws IOException{
        String body = Utils.convert(r.getRequestBody());
        try {
            JSONObject deserialized = new JSONObject(body);

            String name, movieId;

            if (deserialized.length() >= 2 && deserialized.has("name") && deserialized.has("movieId")) {
                name = deserialized.getString("name");
                movieId = deserialized.getString("movieId");
            } else {
                r.sendResponseHeaders(400, -1);
                return;
            }

            try {
                this.dao.insertMovie(name, movieId);
            } catch (Exception e) {
                r.sendResponseHeaders(500, -1);
                e.printStackTrace();
                return;
            }
            r.sendResponseHeaders(200, -1);
        } catch (Exception e) {
            e.printStackTrace();
            r.sendResponseHeaders(500, -1);
        }
    }

    public void addRelationship(HttpExchange r) throws IOException{
        String body = Utils.convert(r.getRequestBody());
        try {
            JSONObject deserialized = new JSONObject(body);

            String actorId, movieId;

            if (deserialized.length() >= 2 && deserialized.has("actorId") && deserialized.has("movieId")) {
                actorId = deserialized.getString("actorId");
                movieId = deserialized.getString("movieId");
            } else {
                r.sendResponseHeaders(400, -1);
                return;
            }

            try {
                this.dao.findActor(actorId);
                this.dao.findMovie(movieId);
            } catch (Exception e){
                e.printStackTrace();
                r.sendResponseHeaders(404, -1);
                return;
            }

            try {
                this.dao.insertRelationship(actorId, movieId);
            } catch (Exception e) {
                r.sendResponseHeaders(500, -1);
                e.printStackTrace();
                return;
            }
            r.sendResponseHeaders(200, -1);
        } catch (Exception e) {
            e.printStackTrace();
            r.sendResponseHeaders(500, -1);
        }
    }
}