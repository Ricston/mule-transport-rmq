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
import org.mule.api.MuleMessage;
import org.mule.api.endpoint.InboundEndpoint;
import org.mule.transport.AbstractMessageRequester;
import org.mule.transport.rmq.essentials.Exchange;
import org.mule.transport.rmq.parsers.RmqEndpointParser;
import org.mule.util.UUID;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.GetResponse;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;

/**
 * <code>FullMessageRequester</code> TODO document
 */
public class RmqMessageRequester extends AbstractMessageRequester {

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

	private boolean bindOnStart = false;
	
	private boolean unbindOnFinish = false;

	// //////////////////////////////////////////////////////////////////////
	// Methods
	// //////////////////////////////////////////////////////////////////////

	public RmqMessageRequester(InboundEndpoint endpoint) {
		super(endpoint);
		
		this.connector = (RmqConnector) connector;
		this.channel = this.connector.getChannel();
		parser = new RmqEndpointParser(this.endpoint.getEndpointURI().toString());
		exchange = this.connector.getExchangeMap().get(parser.getExchangeName());
		routingKey = parser.getRoutingPattern();
		queue = parser.getQueueName();
	}

	protected MuleMessage doRequest(long timeout) throws Exception {
		GetResponse response = channel.basicGet(queue, noAck);		
		QueueingConsumer.Delivery delivery = new Delivery(response.getEnvelope(), response.getProps(), response.getBody());		
		return new DefaultMuleMessage(connector.getMessageAdapter(delivery));
	}

	public void doConnect() throws Exception {	
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
		if (bindOnStart && routingKey != null) {
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
					cExclusive, null, consumer);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}		
	}

	public void doDisconnect() throws Exception {
		//Unbind on finish.
		if (unbindOnFinish && routingKey != null) {
			try {
				channel.queueUnbind(queue, exchange.getName(), routingKey);
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}		
	}

	public void doDispose() {
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