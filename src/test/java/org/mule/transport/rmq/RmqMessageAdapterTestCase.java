/*
 * $Id: RmqMessageAdapterTestCase.java 1486 2011-09-30 09:57:05Z claude.mamo $
 * --------------------------------------------------------------------------------------
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.rmq;

import org.mule.transport.AbstractMessageAdapterTestCase;
import org.mule.api.transport.MessageAdapter;
import org.mule.api.MessagingException;

import com.rabbitmq.client.QueueingConsumer.Delivery;

public class RmqMessageAdapterTestCase extends AbstractMessageAdapterTestCase
{

    /*
     * For general guidelines on writing transports see
     * http://mule.mulesource.org/display/MULE/Writing+Transports
     */

    public Object getValidMessage() throws Exception
    {
        // TODO Create a valid message for your transport
        Delivery delivery = new Delivery(null, null, "Hello".getBytes());
        return delivery;
    }

    public MessageAdapter createAdapter(Object payload) throws MessagingException
    {
        return new RmqMessageAdapter(payload);
    }

}
