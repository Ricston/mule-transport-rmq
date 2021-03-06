/*
 * $Id: RmqMessageReceiver.java 1486 2011-09-30 09:57:05Z claude.mamo $
 * --------------------------------------------------------------------------------------
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
import org.mule.api.MessagingException;
import org.mule.api.MuleException;
import org.mule.api.endpoint.InboundEndpoint;
import org.mule.api.lifecycle.CreateException;
import org.mule.api.service.Service;
import org.mule.api.transport.Connector;
import org.mule.api.transport.MessageAdapter;
import org.mule.transport.AbstractMessageReceiver;

import org.mule.transport.ConnectException;
import org.mule.transport.rmq.essentials.Exchange;
import org.mule.transport.rmq.parsers.RmqEndpointParser;
import org.mule.util.UUID;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.QueueingConsumer.Delivery;

public class RmqMessageReceiver extends AbstractMessageReceiver
{

    // //////////////////////////////////////////////////////////////////////
    // RMQ Connection
    // //////////////////////////////////////////////////////////////////////

    private RmqConnector connector;
    private Channel channel;
    private RmqEndpointParser parser;

    private DefaultConsumer consumer;
    private Exchange exchange;

    // //////////////////////////////////////////////////////////////////////
    // Queue Settings
    // //////////////////////////////////////////////////////////////////////

    private String queue;
    private boolean durable = false;
    private boolean exclusive = false;
    private boolean autoDelete = false;
    private Map<String, Object> arguments = null;

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

    public RmqMessageReceiver(Connector connector, Service service, InboundEndpoint endpoint)
        throws CreateException
    {
        super(connector, service, endpoint);
        // Retreive connector.
        this.connector = (RmqConnector) connector;
        // Retreive channel from connector.
        this.channel = this.connector.getChannel();
        // Parse the endpoint URI.
        parser = new RmqEndpointParser(this.endpoint.getEndpointURI().toString());
        // Select exchange.
        importExchange();
        // Set routing pattern.
        routingKey = parser.getRoutingPattern();
        // Set queue.
        setupQueue();
    }

    private void setupQueue()
    {
        queue = parser.getQueueName();
        exclusive = parser.isExclusive();
        durable = parser.isDurable();
        autoDelete = parser.isAutoDelete();
    }

    private void importExchange()
    {
        if (connector.getExchangeMap().get(parser.getExchangeName()) == null)
        {
            exchange = new Exchange(parser.getExchangeName());
        }
        else
        {
            exchange = connector.getExchangeMap().get(parser.getExchangeName());
        }
    }

    public void doConnect() throws ConnectException
    {
        // QUEUE HANDLING
        if (queue == null)
        {
            try
            {
                queue = channel.queueDeclare().getQueue();
            }
            catch (IOException e)
            {
                logger.error(e.getMessage());
            }
        }
        else
        {
            try
            {
                channel.queueDeclare(queue, durable, exclusive, autoDelete, arguments);
            }
            catch (IOException e)
            {
                logger.error(e.getMessage());
            }
        }

        // BINDING HANDLING
        if (bindOnStart)
        {
            try
            {
                channel.queueBind(queue, exchange.getName(), routingKey);
            }
            catch (IOException e)
            {
                logger.error(e.getMessage());
            }
        }
    }

    public void doDisconnect() throws ConnectException
    {
        if (unbindOnFinish)
        {
            try
            {
                channel.queueUnbind(queue, exchange.getName(), routingKey);
            }
            catch (IOException e)
            {
                logger.error(e.getMessage());
            }
        }
    }

    public void doStart()
    {
        // Create a listening consumer.
        consumer = new DefaultConsumer(channel)
        {
            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body) throws IOException
            {
                long deliveryTag = envelope.getDeliveryTag();

                MessageAdapter adapter;
                try
                {
                    adapter = connector.getMessageAdapter(new Delivery(envelope, properties, body));
                    routeMessage(new DefaultMuleMessage(adapter), endpoint.isSynchronous());
                }
                catch (MessagingException e)
                {
                    logger.error(e.getMessage());
                }
                catch (MuleException e)
                {
                    logger.error(e.getMessage());
                }

                this.getChannel().basicAck(deliveryTag, false);
            }
        };

        try
        {
            channel.basicConsume(queue, noAck, consumerTag, noLocal, cExclusive, null, consumer);
        }
        catch (IOException e)
        {
            logger.error(e.getMessage());
        }
    }

    public void doStop()
    {
        // Stop the consumer from listening.
        try
        {
            channel.basicCancel(consumerTag);
        }
        catch (IOException e)
        {
            logger.error(e.getMessage());
        }
    }

    // //////////////////////////////////////////////////////////////////////
    // Getters and Setters
    // //////////////////////////////////////////////////////////////////////

    public void setDurable(boolean durable)
    {
        this.durable = durable;
    }

    public boolean isDurable()
    {
        return durable;
    }

    public void setExclusive(boolean exclusive)
    {
        this.exclusive = exclusive;
    }

    public boolean isExclusive()
    {
        return exclusive;
    }

    public void setAutoDelete(boolean autoDelete)
    {
        this.autoDelete = autoDelete;
    }

    public boolean isAutoDelete()
    {
        return autoDelete;
    }

    public void setTimeout(long timeout)
    {
        this.timeout = timeout;
    }

    public long getTimeout()
    {
        return timeout;
    }

    public void setNoAck(boolean noAck)
    {
        this.noAck = noAck;
    }

    public boolean isNoAck()
    {
        return noAck;
    }

    public void setConsumerTag(String consumerTag)
    {
        this.consumerTag = consumerTag;
    }

    public String getConsumerTag()
    {
        return consumerTag;
    }

    public void setNoLocal(boolean noLocal)
    {
        this.noLocal = noLocal;
    }

    public boolean isNoLocal()
    {
        return noLocal;
    }

    public void setcExclusive(boolean cExclusive)
    {
        this.cExclusive = cExclusive;
    }

    public boolean iscExclusive()
    {
        return cExclusive;
    }

    public void setUnbindOnFinish(boolean unbindOnFinish)
    {
        this.unbindOnFinish = unbindOnFinish;
    }

    public boolean isUnbindOnFinish()
    {
        return unbindOnFinish;
    }

}
