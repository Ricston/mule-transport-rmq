/*
 * $Id: PollMultiFunctionalTestCase.java 1486 2011-09-30 09:57:05Z claude.mamo $
 * --------------------------------------------------------------------------------------
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.rmq.functional;

import java.util.HashMap;
import java.util.Map;

import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;

public class PollMultiFunctionalTestCase extends org.mule.tck.FunctionalTestCase
{

    protected String getConfigResources()
    {
        return "src/test/resources/poll-multi-mule-config.xml";
    }

    public void testSingleMessage() throws Exception
    {

        MuleClient client = null;
        MuleMessage reply1 = null;
        MuleMessage reply2 = null;
        String message = "This is a very little message to be sent over from VM through Mule aboard RabbitMQ and then returned back to VM through mule.";

        try
        {
            client = new MuleClient();
            client.dispatch("vm://inbound", message, null);
            reply1 = client.request("vm://outbound1", 5000);
            reply2 = client.request("vm://outbound2", 5000);
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
    }

    public void testTwoMessages() throws Exception
    {

        MuleClient client = null;
        MuleMessage reply11 = null;
        MuleMessage reply12 = null;
        MuleMessage reply21 = null;
        MuleMessage reply22 = null;
        String message1 = "1. This is a very little message to be sent over from VM through Mule aboard RabbitMQ and then returned back to VM through mule.";
        String message2 = "2. This is a very little message to be sent over from VM through Mule aboard RabbitMQ and then returned back to VM through mule.";

        try
        {
            client = new MuleClient();
            client.dispatch("vm://inbound", message1, null);
            reply11 = client.request("vm://outbound1", 10000);
            reply21 = client.request("vm://outbound2", 10000);
            client.dispatch("vm://inbound", message2, null);
            reply12 = client.request("vm://outbound1", 10000);
            reply22 = client.request("vm://outbound2", 10000);
        }
        catch (MuleException e)
        {
            fail("Exception raised: " + e.getDetailedMessage());
        }

        assertNotNull(reply11);
        assertNotNull(reply11.getPayload());
        assertEquals(message1, reply11.getPayload());

        assertNotNull(reply21);
        assertNotNull(reply21.getPayload());
        assertEquals(message1, reply21.getPayload());

        assertNotNull(reply12);
        assertNotNull(reply12.getPayload());
        assertEquals(message2, reply12.getPayload());

        assertNotNull(reply22);
        assertNotNull(reply22.getPayload());
        assertEquals(message2, reply22.getPayload());
    }

    public void testSpamMessages() throws Exception
    {

        MuleClient client = null;
        Map<String, String> messg_list = new HashMap<String, String>();
        int limit = 5;

        try
        {

            client = new MuleClient();

            for (int i = 0; i < limit; i++)
            {
                messg_list.put("This is " + i, "This is " + i);
                client.dispatch("vm://inbound", messg_list.get("This is " + i), null);
            }

            for (int i = 0; i < limit; i++)
            {
                MuleMessage reply = client.request("vm://outbound1", 5000);
                assertNotNull(reply);
                assertNotNull(reply.getPayload());
                assertNotNull(messg_list.get(reply.getPayloadAsString()));
                assertEquals(messg_list.get(reply.getPayloadAsString()), reply.getPayloadAsString());
            }

            for (int i = 0; i < limit; i++)
            {
                MuleMessage reply = client.request("vm://outbound2", 5000);
                assertNotNull(reply);
                assertNotNull(reply.getPayload());
                assertNotNull(messg_list.get(reply.getPayloadAsString()));
                assertEquals(messg_list.get(reply.getPayloadAsString()), reply.getPayloadAsString());
            }

        }
        catch (MuleException e)
        {
            fail("Exception raised: " + e.getDetailedMessage());
        }

    }

}
