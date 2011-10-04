/*
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.rmq;

import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.api.endpoint.OutboundEndpoint;
import org.mule.transport.AbstractMessageDispatcher;
import org.mule.transport.rmq.essentials.Exchange;
import org.mule.transport.rmq.parsers.RmqEndpointParser;

import com.rabbitmq.client.Channel;

public class RmqMessageDispatcher extends AbstractMessageDispatcher {

	// //////////////////////////////////////////////////////////////////////
	// RMQ Connection
	// //////////////////////////////////////////////////////////////////////

	private RmqConnector connector;
	private Channel channel;
	private RmqEndpointParser parser;

	// //////////////////////////////////////////////////////////////////////
	// Exchange Settings
	// //////////////////////////////////////////////////////////////////////

	private Exchange exchange;
	private String topic;

	// //////////////////////////////////////////////////////////////////////
	// Methods
	// //////////////////////////////////////////////////////////////////////

	public RmqMessageDispatcher(OutboundEndpoint endpoint) {
		super(endpoint);
		//Retreive Connector.
		connector = (RmqConnector) endpoint.getConnector();
		//Retreive Channel.
		channel = connector.getChannel();
		//Parse the endpoint and import the Exchange.
		parser = new RmqEndpointParser(this.endpoint.getEndpointURI().toString());
		topic = parser.getRoutingPattern();
		importExchange();
	}

	private void importExchange() {
		//If no exchange has been created initially, create a new one,
		//else retreive the Exchange.
		if (connector.getExchangeMap().get(parser.getExchangeName()) == null) {
			exchange = new Exchange(parser.getExchangeName());
		} else {
			exchange = connector.getExchangeMap().get(parser.getExchangeName());
		}
	}

	public void doConnect() throws Exception {
		// EMPTY//
	}

	public void doDisconnect() throws Exception {
		// EMPTY//
	}

	public void doDispatch(MuleEvent event) throws Exception {
		//Send the message as bytes over the exchange attached with the specified topic (NO REPLY).
		channel.basicPublish(exchange.getName(), topic, null,
				event.getMessageAsBytes());
	}

	public MuleMessage doSend(MuleEvent event) throws Exception {
		//Send the message as bytes over the exchange attached with the specified topic (NO REPLY).
		channel.basicPublish(exchange.getName(), topic, null,
				event.getMessageAsBytes());
		return null;
	}
}