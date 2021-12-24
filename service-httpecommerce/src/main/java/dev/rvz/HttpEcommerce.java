package dev.rvz;

import dev.rvz.servlets.GenerateAllReportsService;
import dev.rvz.servlets.NewOrderServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class HttpEcommerce {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);

        ServletContextHandler servletContextHandler = new ServletContextHandler();
        servletContextHandler.setContextPath("/");
        servletContextHandler.addServlet(new ServletHolder(new NewOrderServlet()), "/new");
        servletContextHandler.addServlet(new ServletHolder(new GenerateAllReportsService()), "/admin/generate-report");

        server.setHandler(servletContextHandler);
        server.start();
        server.join();
    }
}