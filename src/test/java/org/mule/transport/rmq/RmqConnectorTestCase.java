/*
 * $Id: RmqConnectorTestCase.java 1486 2011-09-30 09:57:05Z claude.mamo $
 * --------------------------------------------------------------------------------------
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.rmq;

import org.mule.api.transport.Connector;
import org.mule.transport.AbstractConnectorTestCase;

import com.rabbitmq.client.QueueingConsumer;

public class RmqConnectorTestCase extends AbstractConnectorTestCase
{

    /*
     * For general guidelines on writing transports see
     * http://mule.mulesource.org/display/MULE/Writing+Transports
     */

    public Connector createConnector() throws Exception
    {
        /*
         * IMPLEMENTATION NOTE: Create and initialise an instance of your connector
         * here. Do not actually call the connect method.
         */

        RmqConnector c = new RmqConnector();
        c.setName("Test");
        c.setConnectorURI("guest:guest@127.0.0.1:5672");
        return c;
    }

    public String getTestEndpointURI()
    {
        // TODO Return a valid endpoint for you transport here
        return "rmq://amq.topic/#";
    }

    public Object getValidMessage() throws Exception
    {
        // TODO Return an valid message for your transport
        return new QueueingConsumer.Delivery(null, null, "This is a test message.".getBytes());
    }    
    
    public void testProperties() throws Exception
    {
        // TODO test setting and retrieving any custom properties on the
        // Connector as necessary
    }
    
    public void testConnectorMessageRequesterFactory()
    {
    	 // TODO test connector MessageRequesterFactory    	
    }

}
