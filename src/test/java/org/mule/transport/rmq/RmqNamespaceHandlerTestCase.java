/*
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.rmq;

import org.mule.tck.FunctionalTestCase;

/**
 * TODO
 */
public class RmqNamespaceHandlerTestCase extends FunctionalTestCase
{
    protected String getConfigResources()
    {
        return "src/test/resources/mule-config.xml";
    }

    public void testRmqConfig() throws Exception
    {
        RmqConnector c = (RmqConnector) muleContext.getRegistry().lookupConnector("connector");
        assertNotNull(c);
        assertTrue(c.isConnected());
        assertTrue(c.isStarted());
        assertEquals(c.getHost(), "localhost");
        assertEquals(c.getPort(), 5672);
        assertEquals(c.getUsername(), "guest");
        assertEquals(c.getPassword(), "guest");
        
    }
}