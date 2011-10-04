/*
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.rmq.transformers;

import org.mule.transformer.AbstractMessageAwareTransformer;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;

import com.rabbitmq.client.QueueingConsumer.Delivery;

/**
 * <code>RmqMessageToObject</code> TODO Document
 */
public class RmqMessageToDelivery extends AbstractMessageAwareTransformer
{
    public RmqMessageToDelivery()
    {
		registerSourceType(Delivery.class);
		setReturnClass(Delivery.class);
    }

    public Object transform(MuleMessage message, String outputEncoding) throws TransformerException
    {
    	return (Delivery) message.getPayload();
    }
}
