/*
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.rmq;

import org.mule.api.endpoint.InboundEndpoint;
import org.mule.tck.AbstractMuleTestCase;


public class RmqConnectorFactoryTestCase extends AbstractMuleTestCase
{

    /* For general guidelines on writing transports see
       http://mule.mulesource.org/display/MULE/Writing+Transports */

    public void testCreateFromFactory() throws Exception
    {
        InboundEndpoint endpoint = muleContext.getRegistry().lookupEndpointFactory().getInboundEndpoint(getEndpointURI());
        assertNotNull(endpoint);
        assertNotNull(endpoint.getConnector());
        assertTrue(endpoint.getConnector() instanceof RmqConnector);
        assertEquals(getEndpointURI().toString(), endpoint.getEndpointURI().toString());
    }

    public String getEndpointURI() 
    {
        // TODO return a valid endpoint URI string for your transport
        // i.e. tcp://localhost:1234
        return new String("rmq://amq.topic/#");
    }

}
