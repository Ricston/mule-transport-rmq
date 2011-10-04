/*
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.rmq;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.mule.transport.AbstractConnector;
import org.mule.transport.rmq.essentials.Exchange;
import org.mule.transport.rmq.parsers.RmqConnectorParser;
import org.mule.api.MuleException;
import org.mule.api.lifecycle.InitialisationException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RmqConnector extends AbstractConnector {
	
	// Default variables.
	public static final String DEFAULT_HOST = "localhost";
	public static final int DEFAULT_PORT = 5672;
	public static final String DEFAULT_USERNAME = "guest";
	public static final String DEFAULT_PASSWORD = "guest";
	public static final String DEFAULT_VIRTUALHOST = "/";

	// //////////////////////////////////////////////////////////////////////
	// RMQ Connection
	// //////////////////////////////////////////////////////////////////////

	private ConnectionFactory connectionFactory;
	private Connection connection;

	private Channel channel;

	private Map<String, Exchange> exchangeMap = new HashMap<String, Exchange>();
	private boolean springAutoName = true;

	// //////////////////////////////////////////////////////////////////////
	// RMQ Connection Variables
	// //////////////////////////////////////////////////////////////////////

	private String connectorURI;

	private String host = DEFAULT_HOST;
	private int port = DEFAULT_PORT;
	private String vhost = DEFAULT_VIRTUALHOST;
	private String username = DEFAULT_USERNAME;
	private String password = DEFAULT_PASSWORD;

	private int channelNumber = -1;

	/* This constant defines the main transport protocol identifier */
	public static final String RMQ = "rmq";

	public void doInitialise() throws InitialisationException {
		//Construct a connection factory.
		connectionFactory = this.createConnectionFactory();
	}

	private ConnectionFactory createConnectionFactory() {
		ConnectionFactory factory = new ConnectionFactory();
		//Construct the factory from the connector URI if available
		//else from the variables.
		if (connectorURI != null) {
			RmqConnectorParser rcp = new RmqConnectorParser(connectorURI);
			host = rcp.getHost();
			port = rcp.getPort();
			vhost = rcp.getVhost();
			username = rcp.getUsername();
			password = rcp.getPassword();
		}
		factory.setHost(host);
		factory.setPort(port);
		if (vhost != null) factory.setVirtualHost(vhost);
		if (password != null) factory.setPassword(password);
		if (username != null) factory.setUsername(username);
		return factory;
	}

	public void doConnect() throws Exception {
		//Create a connection from the connection factory.
		connection = createConnection();
		//Create a channel from the connection.
		channel = connection.createChannel();
		
		//Get the list of exchanges to list.
		Set<String> keys = exchangeMap.keySet();

		//Pass through the map declaring the exchanges. 
		for (String key : keys) {
			if (springAutoName)
				exchangeMap.get(key).setName(key);
			Exchange exchange = exchangeMap.get(key);

			if (exchange.getName() == null) {
				if (exchange.getType().equals("direct"))
					exchange.setName("amq.direct");
				if (exchange.getType().equals("fanout"))
					exchange.setName("amq.fanout");
				if (exchange.getType().equals("topic"))
					exchange.setName("amq.topic");
			}

			channel.exchangeDeclare(exchange.getName(), exchange.getType(),
					exchange.isDurable(), exchange.isAutoDelete(),
					exchange.getArguments());
		}
	}

	private Connection createConnection() {
		//Create a new connection unless an exception is thrown.
		try {
			return connectionFactory.newConnection();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	public synchronized void doDisconnect() throws Exception {
		//Check that channel is not null or is open and close it.
		if (channel != null && channel.isOpen())
			channel.close();
		//Check that connection is not null or is open and close it.
		if (connection != null && connection.isOpen())
			connection.close();
	}

	public void doStart() throws MuleException {
		//EMPTY//
	}

	public void doStop() throws MuleException {
		//EMPTY//
	}

	public void doDispose() {
		//EMPTY//
	}

	public String getProtocol() {
		return RMQ;
	}

	// //////////////////////////////////////////////////////////////////////
	// Getters and Setters
	// //////////////////////////////////////////////////////////////////////

	protected Connection getConnection() {
		return connection;
	}

	protected void setConnection(Connection connection) {
		this.connection = connection;
	}

	protected Channel getChannel() {
		return channel;
	}

	protected void setChannel(Channel channel) {
		this.channel = channel;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getVhost() {
		return vhost;
	}

	public void setVhost(String vhost) {
		this.vhost = vhost;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getChannelNumber() {
		return channelNumber;
	}

	public void setChannelNumber(int channel_no) {
		this.channelNumber = channel_no;
	}

	public void setExchangeMap(Map<String, Exchange> exchangeMap) {
		this.exchangeMap = exchangeMap;
	}

	public Map<String, Exchange> getExchangeMap() {
		return exchangeMap;
	}

	public void setSpringAutoName(boolean springAutoName) {
		this.springAutoName = springAutoName;
	}

	public boolean isSpringAutoName() {
		return springAutoName;
	}

	public void setConnectorURI(String connectorURI) {
		this.connectorURI = connectorURI;
	}

	public String getConnectorURI() {
		return connectorURI;
	}
	
}