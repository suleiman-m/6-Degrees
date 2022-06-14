package ca.utoronto.utm.mcs;

import com.sun.net.httpserver.HttpServer;
import javax.inject.Inject;

public class Server {

	public HttpServer server;
  // TODO Complete This Class
	@Inject
	public Server(HttpServer server){
		this.server = server;
	}
}
