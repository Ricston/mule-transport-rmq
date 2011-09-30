/*
 * $Id: TopicFunctionalTestCase.java 1486 2011-09-30 09:57:05Z claude.mamo $
 * --------------------------------------------------------------------------------------
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.rmq.functional;

import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;

public class TopicFunctionalTestCase extends org.mule.tck.FunctionalTestCase
{

    protected String getConfigResources()
    {
        return "src/test/resources/topic-mule-config.xml";
    }

    public void testSingleMessage() throws Exception
    {

        MuleClient client = null;
        MuleMessage reply1 = null;
        MuleMessage reply2 = null;
        MuleMessage reply3 = null;
        MuleMessage reply4 = null;
        MuleMessage reply5 = null;
        MuleMessage reply6 = null;
        MuleMessage reply7 = null;

        String message = "Message this is a test message. The quick brown fox jumps over the lazy dog.";

        try
        {
            client = new MuleClient();
            client.dispatch("vm://inbound", message, null);
            reply1 = client.request("vm://catcher1", 5000);
            reply2 = client.request("vm://catcher2", 5000);
            reply3 = client.request("vm://catcher3", 5000);
            reply4 = client.request("vm://catcher4", 5000);
            reply5 = client.request("vm://misser1", 5000);
            reply6 = client.request("vm://misser2", 5000);
            reply7 = client.request("vm://misser3", 5000);
        }
        catch (MuleException e)
        {
            fail("Exception raised: " + e.getDetailedMessage());
        }

        assertNotNull(reply1);
        assertNotNull(reply1.getPayload());
        assertEquals(message, reply1.getPayload());
        assertNotNull(reply2);
        assertNotNull(reply2.getPayload());
        assertEquals(message, reply2.getPayload());
        assertNotNull(reply3);
        assertNotNull(reply3.getPayload());
        assertEquals(message, reply3.getPayload());
        assertNotNull(reply4);
        assertNotNull(reply4.getPayload());
        assertEquals(message, reply4.getPayload());
        assertNull(reply5);
        assertNull(reply6);
        assertNull(reply7);
    }

}
