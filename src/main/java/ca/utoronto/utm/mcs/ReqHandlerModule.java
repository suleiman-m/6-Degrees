package ca.utoronto.utm.mcs;

import dagger.Module;
import dagger.Provides;
import io.github.cdimascio.dotenv.Dotenv;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;

@Module
public class ReqHandlerModule {
	// TODO Complete This Module

	private final String username = "neo4j";
	private final String password = "123456";

	@Provides
	public Neo4jDAO provideNeo4jDAO(){
		// This code is used to get the neo4j address, you must use this so that we can mark :)
		Dotenv dotenv = Dotenv.load();
		String addr = dotenv.get("NEO4J_ADDR");
		System.out.println(addr);
		Driver driver = GraphDatabase.driver(String.format("bolt://%s:7687", addr), AuthTokens.basic(this.username, this.password));
		return new Neo4jDAO(driver);
	}

}
