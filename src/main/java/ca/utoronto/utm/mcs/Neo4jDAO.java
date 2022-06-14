package ca.utoronto.utm.mcs;

import org.neo4j.driver.*;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.ArrayList;

// All your database transactions or queries should 
// go in this class
public class Neo4jDAO {
	// TODO Complete This Class

	private final Session session;
	private final Driver driver;

	@Inject
	public Neo4jDAO(Driver driver){
		this.driver = driver;
		this.session = this.driver.session();

		// Adding an initial ABC Kevin Bacon, since baconNumber and baconPath must return him.
		/* INTERNAL CODE FOR TESTING PURPOSES OF KEVIN BACON
		String queryA;
		queryA = "CREATE (a:actor{name: \"%s\", id: \"%s\"})";
		queryA = String.format(queryA, "Kevin Bacon", "nm0000102");
		this.session.run(queryA);

		String queryB;
		queryB = "CREATE (m:movie{name: \"%s\", id: \"%s\"})";
		queryB = String.format(queryB, "Groundhog Day", "nm1111891");
		this.session.run(queryB);

		String queryC;
		queryC = "MATCH (a:actor),(m:movie) WHERE a.id=\"%s\" AND m.id=\"%s\" CREATE (a)-[r:ACTED_IN]->(m);";
		queryC = String.format(queryC, "nm0000102", "nm1111891");
		this.session.run(queryC);

		String queryC1;
		queryC1 = "MATCH (a:actor) WHERE a.id=\"%s\" SET a.movies=[\"%s\"];";
		queryC1 = String.format(queryC1, "nm0000102", "nm1111891");
		this.session.run(queryC1);

		String queryC2;
		queryC2 = "MATCH (m:actor) WHERE m.id=\"%s\" SET m.actors=[\"%s\"];";
		queryC2 = String.format(queryC2, "nm1111891", "nm0000102");
		this.session.run(queryC2);*/
	}

	public void insertActor(String name, String actorId){
		String query;
		query = "CREATE (a:actor{name: \"%s\", id: \"%s\"})";
		query = String.format(query, name, actorId);
		this.session.run(query);
	}

	public void insertMovie(String name, String movieId){
		String query;
		query = "CREATE (m:movie{name:\"%s\",id:\"%s\"})";
		query = String.format(query, name, movieId);
		this.session.run(query);
	}

	public void insertRelationship(String actorId, String movieId){
		String query;
		query = "MATCH (a:actor),(m:movie) WHERE a.id=\"%s\" AND m.id=\"%s\" CREATE (a)-[r:ACTED_IN]->(m);";
		query = String.format(query, actorId, movieId);
		this.session.run(query);

		String query2;
		query2 = "MATCH (a:actor) WHERE a.id=\"%s\" SET a.movies = a.movies + \"%s\";";
		query2 = String.format(query2, actorId, movieId);
		this.session.run(query2);

		String query3;
		query3 = "MATCH (m:movie) WHERE m.id=\"%s\" SET m.actors = m.actors + \"%s\";";
		query3 = String.format(query3, movieId, actorId);
		this.session.run(query3);
	}

	public JSONObject findActor(String actorId){
		String query1, query2;
		JSONObject response = new JSONObject();

		query1 = "MATCH (a:actor) WHERE a.id=\"%s\" RETURN a.name;";
		query1 = String.format(query1, actorId);
		String name = this.session.run(query1).single().get(0).toString();

		query2 = "MATCH (a:actor) WHERE a.id=\"%s\" RETURN a.movies;";
		query2 = String.format(query2, actorId);
		String str_movies = this.session.run(query2).single().get(0).toString();
		str_movies = str_movies.substring(1, str_movies.length()-1);
		String[] movies = str_movies.split(",");

		int counter = 0;
		for (String s: movies){
			if (counter > 0) {
				movies[counter] = s.substring(1);
			}
			counter += 1;
		}

		try {
			response.put("actorId", actorId);
			response.put("name", name);
			response.put("movies", movies);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	public JSONObject findMovie(String movieId){
		String query1, query2;
		JSONObject response = new JSONObject();

		query1 = "MATCH (m:movie) WHERE m.id=\"%s\" RETURN m.name;";
		query1 = String.format(query1, movieId);
		String name = this.session.run(query1).single().get(0).toString();

		query2 = "MATCH (m:movie) WHERE m.id=\"%s\" RETURN m.actors;";
		query2 = String.format(query2, movieId);
		String str_actors = this.session.run(query2).single().get(0).toString();
		str_actors = str_actors.substring(1, str_actors.length()-1);
		String[] actors = str_actors.split(",");

		int counter = 0;
		for (String s: actors){
			if (counter > 0) {
				actors[counter] = s.substring(1);
			}
			counter += 1;
		}

		try {
			response.put("movieId", movieId);
			response.put("name", name);
			response.put("actors", actors);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;
	}

	public JSONObject findRelationship(String actorId, String movieId){
		String query;
		Boolean hasRelationship = false;
		JSONObject response = new JSONObject();

		query = "MATCH (a{id:\"%s\"})-[r:ACTED_IN]->(m{id:\"%s\"}) RETURN r;";
		query = String.format(query, actorId, movieId);
		String relationship = this.session.run(query).single().get(0).toString();

		if (!relationship.equals("")){
			hasRelationship = true;
		}

		try {
			response.put("movieId", movieId);
			response.put("actorId", actorId);
			response.put("hasRelationship", hasRelationship);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	public JSONObject computeBaconNumber(String actorId){
		/*
		METHODOLOGY:
		1. Get all movies actorId has ACTED_IN.
		2. For each movie, log each unique actor once.
		3. Check if any of them are Kevin Bacon.
		4. If not, for each unique actor, log THEIR movies.
		5. For each of THEIR movies, log THOSE actors once.
		6. Check if any of them are Kevin Bacon.
		7. Wipe and repopulate the movie and actor variables holdings.
		8. Establish a counter per loop execution to be the baconNumber.
		 */
		String query1, query2;
		String kevinBaconId = "nm0000102";
		int answer = -1;
		int curr_depth = 0;
		String step_type = "0"; // 0 for checking movies, 1 for checking actors
		JSONObject response = new JSONObject();
		ArrayList<String> curr_actors = new ArrayList<>();
		ArrayList<String> curr_movies = new ArrayList<>();

		curr_actors.add(actorId);
		if (actorId.equals(kevinBaconId)){
			answer = 0;
		}

		while (answer == -1){
			if (step_type.equals("0")){
				curr_depth += 1;
				curr_movies.clear();
				for (String a: curr_actors){
					query1 = "MATCH (a:actor) WHERE a.id=\"%s\" RETURN a.movies;";
					query1 = String.format(query1, a);
					String str_movies = this.session.run(query1).single().get(0).toString();
					// Parsing the list for processing.
					str_movies = str_movies.substring(1, str_movies.length()-1);
					String[] movies = str_movies.split(",");
					int count = 0;
					for (String m: movies){
						if (count > 0) {
							movies[count] = m.substring(1);
						}
						// Avoid duplicate movies.
						if (!curr_movies.contains(movies[count])){
							curr_movies.add(movies[count]);
						}
						count += 1;
					}
				}
				step_type = "1";
			} else if (step_type.equals("1")){
				curr_actors.clear();
				for (String m: curr_movies){
					query2 = "MATCH (m:movie) WHERE m.id=\"%s\" RETURN m.actors;";
					query2 = String.format(query2, m);
					String str_actors = this.session.run(query2).single().get(0).toString();
					// Parsing the list for processing.
					str_actors = str_actors.substring(1, str_actors.length()-1);
					String[] actors = str_actors.split(",");
					int count = 0;
					for (String a: actors){
						if (count > 0) {
							actors[count] = a.substring(1);
						}
						// Avoid duplicate actors.
						if (!curr_actors.contains(actors[count])){
							curr_actors.add(actors[count]);
						}
						count += 1;
					}
				}

				if (curr_actors.contains("nm0000102")){
					answer = curr_depth;
				} else if (curr_depth >= 10){
					// Edge case: An actor is in tons of movies but none even remotely connected to Kevin Bacon.
					// This is to stop the program from overflowing resources.
					answer = 9001;
				}
				step_type = "0";
			}
		}

		try {
			response.put("baconNumber", answer);
		} catch (Exception e){
			e.printStackTrace();
		}
		return response;
	}

	public JSONObject computeBaconPath(String actorId){
		String query;
		JSONObject response = new JSONObject();
		query = "MATCH (a1:actor{name:\"%s\"}),(a2:actor{name:\"%s\"})," +
						"p=shortestPath((a1)-[:ACTED_IN*]-(a2)) RETURN p;";
		query = String.format(query, actorId, "nm0000102");
		String path = this.session.run(query).single().get(0).toString();
		path = path.substring(1, path.length()-1);
		String[] answers = path.split(",");
		int[] toTranslate = new int[answers.length];
		String[] finalAnswer = new String[answers.length+1];
		finalAnswer[0] = actorId;

		int counter = 0;
		for (String s: answers){
			if (counter > 0) {
				answers[counter] = s.substring(1);
			}
			counter += 1;
		}

		int count2 = 0;
		for (String s2: answers){
			System.out.print(s2 + " ");
			int first = answers[count2].lastIndexOf("(");
			int second = answers[count2].lastIndexOf(")");
			toTranslate[count2] = Integer.parseInt(answers[count2].substring(first+1, second));

			count2 += 1;
		}

		int count3 = 0;
		for (int s3: toTranslate){
			String query4;
			query4 = "MATCH (n) WHERE ID(n)=%d RETURN n.id;";
			query4 = String.format(query4, s3);
			finalAnswer[count3+1] = this.session.run(query4).single().get(0).toString();
			finalAnswer[count3+1] = finalAnswer[count3].substring(1, finalAnswer[count3].length()-1);
			System.out.print(finalAnswer[count3]);
			count3 += 1;
		}

		try {
			response.put("baconPath", finalAnswer);
		} catch (Exception e){
			e.printStackTrace();
		}
		return response;
	}
}
