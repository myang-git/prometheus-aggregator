package com.north25.prometheus.aggregator;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class TestServer {

	public static void main(String[] args) throws Throwable {
		Server server = new Server();
		ServerConnector connector = new ServerConnector(server);
		connector.setPort(5566);
		server.setConnectors(new Connector[] {connector});
		ServletHandler handler = new ServletHandler();
		handler.addServletWithMapping(new ServletHolder(new MetricAggregatorServlet()), "/prom");
		server.setHandler(handler);
		server.start();
	}

}
