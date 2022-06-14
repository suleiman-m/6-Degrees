package ca.utoronto.utm.mcs;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import dagger.Module;
import dagger.Provides;

@Module
public class ServerModule {

  // TODO Complete This Module
	@Provides
	public HttpServer provideHttpServer() {
		HttpServer server = null;
		try {
			server = HttpServer.create(new InetSocketAddress("0.0.0.0", 8080), 0);
		} catch (Exception e){
			e.printStackTrace();
		}
		return server;
	}


}
