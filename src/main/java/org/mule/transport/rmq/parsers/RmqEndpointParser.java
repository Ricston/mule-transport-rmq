/*
 * $Id: RmqEndpointParser.java 1486 2011-09-30 09:57:05Z claude.mamo $
 * --------------------------------------------------------------------------------------
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.rmq.parsers;

public class RmqEndpointParser
{

    /*
     * ======================================
     * POSSIBLE FORMATS - ENDPOINT URI FORMAT
     * ======================================
     * rmq://[exchange]:[queue]{[value],[value],[...]}/[key]
     * rmq://[exchange]:[queue]{[value],[value],[...]}/
     * rmq://[exchange]:[queue]/[key]
     * rmq://[exchange]:[queue]/
     * rmq://[exchange]/[key]
     * rmq://[exchange]/
     */

    /* Endpoint URI in String format as retrieved. */
    private String uri;

    /* Variables to be parsed from Endpoint URI. */
    private String exchangeName;
    private boolean durable = false;
    private boolean exclusive = false;
    private boolean autoDelete = false;
    private String queueName;
    private String routingPattern;

    public RmqEndpointParser(String uri)
    {
        // Setting the URI.
        this.uri = uri;
        // Creating a StringBuffer for optimized parsing.
        StringBuffer uriDummy = new StringBuffer(uri);
        // Remove Mule Schema.
        uriDummy.delete(0, 6);
        // Extract exchange and queue or just exchange.
        if (uriDummy.indexOf(":") != -1)
        {
            exchangeName = uriDummy.substring(0, uriDummy.indexOf(":"));
            uriDummy.delete(0, uriDummy.indexOf(":") + 1);
            queueName = uriDummy.substring(0, uriDummy.indexOf("/"));
            uriDummy.delete(0, uriDummy.indexOf("/") + 1);
            // Parse queue settings.
            if (queueName.contains("{"))
            {
                String settings = queueName.substring(queueName.indexOf("{"), queueName.indexOf("}"));
                if (settings.contains("durable")) durable = true;
                if (settings.contains("exclusive")) exclusive = true;
                if (settings.contains("autoDelete")) autoDelete = true;
                queueName = queueName.substring(0, queueName.indexOf("{"));
            }
        }
        else
        {
            exchangeName = uriDummy.substring(0, uriDummy.indexOf("/"));
            uriDummy.delete(0, uriDummy.indexOf("/") + 1);
        }
        // Finally parse routing pattern / key.
        routingPattern = uriDummy.toString();
    }

    // //////////////////////////////////////////////////////////////////////
    // Getters and Setters
    // //////////////////////////////////////////////////////////////////////

    public void setUri(String uri)
    {
        this.uri = uri;
    }

    public String getUri()
    {
        return uri;
    }

    public void setExchangeName(String exchangeName)
    {
        this.exchangeName = exchangeName;
    }

    public String getExchangeName()
    {
        return exchangeName;
    }

    public void setRoutingPattern(String routingPattern)
    {
        this.routingPattern = routingPattern;
    }

    public String getRoutingPattern()
    {
        return routingPattern;
    }

    public void setQueueName(String queueName)
    {
        this.queueName = queueName;
    }

    public String getQueueName()
    {
        return queueName;
    }

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

}
