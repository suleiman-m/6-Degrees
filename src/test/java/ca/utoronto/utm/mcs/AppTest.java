package ca.utoronto.utm.mcs;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

// TODO Please Write Your Tests For CI/CD In This Class. You will see
// these tests pass/fail on github under github actions.
public class AppTest {

    public static JSONObject actor1 = new JSONObject();
    public static JSONObject actor2 = new JSONObject();
    public static JSONObject movie1 = new JSONObject();
    public static JSONObject relation1 = new JSONObject();
    public static JSONObject insufficientData = new JSONObject();

    @BeforeAll
    static void setUp(){
        String[] args = new String[0];
        try {
            App.main(args);
        } catch (Exception e){
            e.printStackTrace();
        }

        String actorName = "Denzel Washington";
        String actorId = "nm1001213";
        try {
            actor1.put("name", actorName);
            actor1.put("actorId", actorId);
            relation1.put("actorId", actorId);
            insufficientData.put("name", actorName);
        } catch (Exception e){
            e.printStackTrace();
        }

        String actorName2 = "Henry Rutherford";
        String actorId2 = "nm1001231";
        try {
            actor2.put("name", actorName2);
            actor2.put("actorId", actorId2);
        } catch (Exception e){
            e.printStackTrace();
        }

        String movieName = "Our Planet";
        String movieId = "nm1251671";
        try {
            movie1.put("name", movieName);
            movie1.put("movieId", movieId);
            relation1.put("movieId", movieId);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void addActorPass() throws IOException, InterruptedException {
        URL url = new URL("http://localhost:8080/api/v1/addActor");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.connect();
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        // following is required for using getResponseCode()
        conn.setDoOutput(true);
        OutputStream os = conn.getOutputStream();
        os.write(actor1.toString().getBytes(StandardCharsets.UTF_8));
        os.close();
        // NOTE: Giving some time for database to run its queries.
        TimeUnit.SECONDS.sleep(4);

        assertEquals(conn.getResponseCode(), 200);
        conn.disconnect();
    }

    @Test
    public void addActorFail() throws IOException, InterruptedException {
        URL url = new URL("http://localhost:8080/api/v1/addActor");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.connect();
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        OutputStream os = conn.getOutputStream();
        os.write(insufficientData.toString().getBytes(StandardCharsets.UTF_8));
        os.close();
        TimeUnit.SECONDS.sleep(4);

        assertEquals(conn.getResponseCode(), 400);
        conn.disconnect();
    }

    @Test
    public void addMoviePass() throws IOException, InterruptedException {
        URL url = new URL("http://localhost:8080/api/v1/addMovie");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.connect();
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        OutputStream os = conn.getOutputStream();
        os.write(movie1.toString().getBytes(StandardCharsets.UTF_8));
        os.close();
        TimeUnit.SECONDS.sleep(4);

        assertEquals(conn.getResponseCode(), 200);
        conn.disconnect();
    }

    @Test
    public void addMovieFail() throws IOException, InterruptedException {
        URL url = new URL("http://localhost:8080/api/v1/addMovie");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.connect();
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        OutputStream os = conn.getOutputStream();
        os.write(insufficientData.toString().getBytes(StandardCharsets.UTF_8));
        os.close();
        TimeUnit.SECONDS.sleep(4);

        assertEquals(conn.getResponseCode(), 400);
        conn.disconnect();
    }

    @Test
    public void addRelationshipPass() throws IOException, InterruptedException {
        URL url = new URL("http://localhost:8080/api/v1/addRelationship");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.connect();
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        OutputStream os = conn.getOutputStream();
        os.write(relation1.toString().getBytes(StandardCharsets.UTF_8));
        os.close();
        TimeUnit.SECONDS.sleep(4);

        assertEquals(conn.getResponseCode(), 200);
        conn.disconnect();
    }

    @Test
    public void addRelationshipFail() throws IOException, InterruptedException {
        URL url = new URL("http://localhost:8080/api/v1/addRelationship");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.connect();
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        OutputStream os = conn.getOutputStream();
        os.write(insufficientData.toString().getBytes(StandardCharsets.UTF_8));
        os.close();
        TimeUnit.SECONDS.sleep(4);

        assertEquals(conn.getResponseCode(), 400);
        conn.disconnect();
    }

    @Test
    public void getActorPass() throws IOException, InterruptedException {
        URL url = new URL("http://localhost:8080/api/v1/getActor");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.connect();
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        OutputStream os = conn.getOutputStream();
        os.write(actor1.toString().getBytes(StandardCharsets.UTF_8));
        os.close();
        TimeUnit.SECONDS.sleep(4);

        assertEquals(conn.getResponseCode(), 200);
        conn.disconnect();
    }

    @Test
    public void getActorFail() throws IOException, InterruptedException {
        URL url = new URL("http://localhost:8080/api/v1/getActor");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.connect();
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        String actorId = "no_such_id";
        OutputStream os = conn.getOutputStream();
        os.write(actorId.getBytes(StandardCharsets.UTF_8));
        os.close();
        TimeUnit.SECONDS.sleep(4);

        assertEquals(conn.getResponseCode(), 404);
        conn.disconnect();
    }

    @Test
    public void getMoviePass() throws IOException, InterruptedException {
        URL url = new URL("http://localhost:8080/api/v1/getMovie");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.connect();
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        OutputStream os = conn.getOutputStream();
        os.write(movie1.toString().getBytes(StandardCharsets.UTF_8));
        os.close();
        TimeUnit.SECONDS.sleep(4);

        assertEquals(conn.getResponseCode(), 200);
        conn.disconnect();
    }

    @Test
    public void getMovieFail() throws IOException, InterruptedException {
        URL url = new URL("http://localhost:8080/api/v1/getMovie");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.connect();
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        OutputStream os = conn.getOutputStream();
        os.write(insufficientData.toString().getBytes(StandardCharsets.UTF_8));
        os.close();
        TimeUnit.SECONDS.sleep(4);

        assertEquals(conn.getResponseCode(), 400);
        conn.disconnect();
    }

    @Test
    public void hasRelationshipPass() throws IOException, InterruptedException {
        URL url = new URL("http://localhost:8080/api/v1/hasRelationship");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.connect();
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        OutputStream os = conn.getOutputStream();
        os.write(relation1.toString().getBytes(StandardCharsets.UTF_8));
        os.close();
        TimeUnit.SECONDS.sleep(4);

        assertEquals(conn.getResponseCode(), 200);
        conn.disconnect();
    }

    @Test
    public void hasRelationshipFail() throws IOException, InterruptedException {
        URL url = new URL("http://localhost:8080/api/v1/hasRelationship");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.connect();
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        OutputStream os = conn.getOutputStream();
        os.write(insufficientData.toString().getBytes(StandardCharsets.UTF_8));
        os.close();
        TimeUnit.SECONDS.sleep(4);

        assertEquals(conn.getResponseCode(), 400);
        conn.disconnect();
    }

    @Test
    public void computeBaconNumberPass() throws IOException, InterruptedException {
        URL url = new URL("http://localhost:8080/api/v1/computeBaconNumber");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.connect();
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        OutputStream os = conn.getOutputStream();
        os.write(actor1.toString().getBytes(StandardCharsets.UTF_8));
        os.close();
        // NOTE: testing the baconNumber/Path may take up to 10 seconds.
        TimeUnit.SECONDS.sleep(10);

        assertEquals(conn.getResponseCode(), 200);
        conn.disconnect();
    }

    @Test
    public void computeBaconNumberFail() throws IOException, InterruptedException {
        URL url = new URL("http://localhost:8080/api/v1/computeBaconNumber");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.connect();
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        OutputStream os = conn.getOutputStream();
        os.write(insufficientData.toString().getBytes(StandardCharsets.UTF_8));
        os.close();
        TimeUnit.SECONDS.sleep(4);

        assertEquals(conn.getResponseCode(), 400);
        conn.disconnect();
    }

    @Test
    public void computeBaconPathPass() throws IOException, InterruptedException {
        URL url = new URL("http://localhost:8080/api/v1/computeBaconPath");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.connect();
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        OutputStream os = conn.getOutputStream();
        os.write(actor1.toString().getBytes(StandardCharsets.UTF_8));
        os.close();
        // NOTE: testing the baconNumber/Path may take up to 10 seconds.
        TimeUnit.SECONDS.sleep(10);

        assertEquals(conn.getResponseCode(), 200);
        conn.disconnect();
    }

    @Test
    public void computeBaconPathFail() throws IOException, InterruptedException {
        URL url = new URL("http://localhost:8080/api/v1/computeBaconPath");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.connect();
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        OutputStream os = conn.getOutputStream();
        os.write(actor1.toString().getBytes(StandardCharsets.UTF_8));
        os.close();
        TimeUnit.SECONDS.sleep(4);

        assertEquals(conn.getResponseCode(), 400);
        conn.disconnect();
    }
}
