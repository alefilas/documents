import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class StartJetty {

    public static void main(String[] args) throws Exception {
        Server server = init("documents-web/src/main/webapp");
        server.start();
        server.join();
    }

    public static Server init(String webDir) {

        Server server = new Server(8080);
        WebAppContext wcon = new WebAppContext();

        wcon.setResourceBase(webDir);

        server.setHandler(wcon);

        return server;
    }

}
