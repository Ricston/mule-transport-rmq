/*
 * $Id: RmqMessageAdapter.java 1486 2011-09-30 09:57:05Z claude.mamo $
 * --------------------------------------------------------------------------------------
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.rmq;

import org.mule.api.MessagingException;
import org.mule.transport.AbstractMessageAdapter;

import com.rabbitmq.client.QueueingConsumer.Delivery;

/**
 * <code>RmqMessageAdapter</code> TODO document
 */
public class RmqMessageAdapter extends AbstractMessageAdapter
{

    private static final long serialVersionUID = -8868259497257751159L;

    private Delivery rmqMessage;

    public RmqMessageAdapter(Object message) throws MessagingException
    {
        super();
        rmqMessage = (Delivery) message;
    }

    public String getPayloadAsString(String encoding) throws Exception
    {
        return new String(rmqMessage.getBody());
    }

    public byte[] getPayloadAsBytes() throws Exception
    {
        return rmqMessage.getBody();
    }

    public Object getPayload()
    {
        return rmqMessage;
    }

}
