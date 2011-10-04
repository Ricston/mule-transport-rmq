/*
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.rmq;

import java.io.IOException;
import java.util.Map;

import org.mule.DefaultMuleMessage;
import org.mule.api.endpoint.InboundEndpoint;
import org.mule.api.lifecycle.CreateException;
import org.mule.api.service.Service;
import org.mule.api.transport.Connector;
import org.mule.api.transport.MessageAdapter;
import org.mule.transport.AbstractPollingMessageReceiver;
import org.mule.transport.ConnectException;
import org.mule.transport.rmq.essentials.Exchange;
import org.mule.transport.rmq.parsers.RmqEndpointParser;
import org.mule.util.UUID;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;

public class RmqMessagePollingReceiver extends AbstractPollingMessageReceiver {

	// //////////////////////////////////////////////////////////////////////
	// RMQ Connection
	// //////////////////////////////////////////////////////////////////////

	private RmqConnector connector;
	private Channel channel;
	private RmqEndpointParser parser;
	
	private QueueingConsumer consumer;	
	private Exchange exchange;

	// //////////////////////////////////////////////////////////////////////
	// Queue Settings
	// //////////////////////////////////////////////////////////////////////

	private String queue;
	private boolean durable = false;
	private boolean exclusive = false;
	private boolean autoDelete = false;
	private Map<String,Object> arguments = null;

	// //////////////////////////////////////////////////////////////////////
	// Queue Consume Settings
	// //////////////////////////////////////////////////////////////////////

	private boolean noAck = false;
	private String consumerTag = UUID.getUUID();
	private boolean noLocal = true;
	private boolean cExclusive = true;

	// //////////////////////////////////////////////////////////////////////
	// Delivery Settings
	// //////////////////////////////////////////////////////////////////////

	private long timeout = -1;

	// //////////////////////////////////////////////////////////////////////
	// Binding
	// //////////////////////////////////////////////////////////////////////

	// //////////////////////////////////////////////////////////////////////
	// Routing Keys Explained
	// ---------------------------------------------------------------------
	// # : Will allow from 0 to infinite numbers of words to be replaced.
	// * : Will allow one word to be replaced.
	// ---------------------------------------------------------------------
	// Example:
	// *.emp.#
	// Will allow que.emp.aaa.ddd.sss
	// Will allow que.emp.aaa
	// Will allow que.emp
	// But will not allow emp.dud or emp.dud.ddd.ddd.dd
	// //////////////////////////////////////////////////////////////////////

	private String routingKey;

	private boolean bindOnStart = true;
	private boolean unbindOnFinish = true;

	// //////////////////////////////////////////////////////////////////////
	// Methods
	// //////////////////////////////////////////////////////////////////////

	public RmqMessagePollingReceiver(Connector connector, Service service,
			InboundEndpoint endpoint) throws CreateException {
		super(connector, service, endpoint);
		//Retreive connector.		
		this.connector = (RmqConnector) connector;
		//Retreive channel from connector.
		this.channel = this.connector.getChannel();
		//Parse the endpoint URI.
		parser = new RmqEndpointParser(this.endpoint.getEndpointURI().toString());
		//Select exchange.
		importExchange();
		//Set routing pattern.
		routingKey = parser.getRoutingPattern();
		//Set queue.
		setupQueue();
	}
	
	private void setupQueue(){
		queue = parser.getQueueName();
		exclusive = parser.isExclusive();
		durable = parser.isDurable();
		autoDelete = parser.isAutoDelete();
	}
	
	private void importExchange(){
		if(connector.getExchangeMap().get(parser.getExchangeName()) == null){
			exchange = new Exchange(parser.getExchangeName()); 
		} else {
			exchange = connector.getExchangeMap().get(parser.getExchangeName());
		}
	}

	public void doConnect() throws ConnectException {
		// QUEUE HANDLING
		if (queue == null) {
			try {
				queue = channel.queueDeclare().getQueue();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		} else {
			try {
				channel.queueDeclare(queue, durable, exclusive, autoDelete,
						arguments);
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}

		// BINDING HANDLING
		if (bindOnStart) {
			try {
				channel.queueBind(queue, exchange.getName(), routingKey);
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}

		// CONSUMER HANDLING
		this.consumer = new QueueingConsumer(channel);

		try {
			channel.basicConsume(queue, noAck, consumerTag, noLocal,
					cExclusive, consumer);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	public void doDisconnect() throws ConnectException {
		//Unbind on finish.
		if (unbindOnFinish) {
			try {
				channel.queueUnbind(queue, exchange.getName(), routingKey);
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}		
	}

	public void poll() throws Exception {

		//Wait for a new message to arrive, then send it to mule for handling.
		
		Delivery delivery = null;

		if (timeout == -1) {
			delivery = consumer.nextDelivery();
		} else {
			delivery = consumer.nextDelivery(timeout);
		}

		channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

		MessageAdapter adapter = connector.getMessageAdapter(delivery);
		routeMessage(new DefaultMuleMessage(adapter), endpoint.isSynchronous());
	}

	// //////////////////////////////////////////////////////////////////////
	// Getters and Setters
	// //////////////////////////////////////////////////////////////////////

	public void setDurable(boolean durable) {
		this.durable = durable;
	}

	public boolean isDurable() {
		return durable;
	}

	public void setExclusive(boolean exclusive) {
		this.exclusive = exclusive;
	}

	public boolean isExclusive() {
		return exclusive;
	}

	public void setAutoDelete(boolean autoDelete) {
		this.autoDelete = autoDelete;
	}

	public boolean isAutoDelete() {
		return autoDelete;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public long getTimeout() {
		return timeout;
	}

	public void setNoAck(boolean noAck) {
		this.noAck = noAck;
	}

	public boolean isNoAck() {
		return noAck;
	}

	public void setConsumerTag(String consumerTag) {
		this.consumerTag = consumerTag;
	}

	public String getConsumerTag() {
		return consumerTag;
	}

	public void setNoLocal(boolean noLocal) {
		this.noLocal = noLocal;
	}

	public boolean isNoLocal() {
		return noLocal;
	}

	public void setcExclusive(boolean cExclusive) {
		this.cExclusive = cExclusive;
	}

	public boolean iscExclusive() {
		return cExclusive;
	}

	public void setUnbindOnFinish(boolean unbindOnFinish) {
		this.unbindOnFinish = unbindOnFinish;
	}

	public boolean isUnbindOnFinish() {
		return unbindOnFinish;
	}

}