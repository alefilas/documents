package ru.alefilas;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class Main {

    public static void main(String[] args) throws Exception {
        org.eclipse.jetty.server.Server server = init("documents-controller/src/main/webapp");
        server.start();
        server.join();
    }

    public static org.eclipse.jetty.server.Server init(String webDir) {

        org.eclipse.jetty.server.Server server = new Server(8080);
        WebAppContext wcon = new WebAppContext();

        wcon.setResourceBase(webDir);

        server.setHandler(wcon);

        return server;
    }

}
