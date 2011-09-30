/*
 * $Id: FanoutFunctionalTestCase.java 1486 2011-09-30 09:57:05Z claude.mamo $
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

public class FanoutFunctionalTestCase extends org.mule.tck.FunctionalTestCase
{

    protected String getConfigResources()
    {
        return "src/test/resources/fanout-mule-config.xml";
    }

    public void testSingleMessage() throws Exception
    {

        MuleClient client = null;
        MuleMessage reply1 = null;
        MuleMessage reply2 = null;
        MuleMessage reply3 = null;
        MuleMessage reply4 = null;
        MuleMessage reply5 = null;
        String message = "This is a very little message to be sent over from VM through Mule aboard RabbitMQ and then returned back to VM through mule.";

        try
        {
            client = new MuleClient();
            client.dispatch("vm://inbound", message, null);
            reply1 = client.request("vm://outbound1", 5000);
            reply2 = client.request("vm://outbound2", 5000);
            reply3 = client.request("vm://outbound3", 5000);
            reply4 = client.request("vm://outbound4", 5000);
            reply5 = client.request("vm://outbound5", 5000);
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
        assertNotNull(reply5);
        assertNotNull(reply5.getPayload());
        assertEquals(message, reply5.getPayload());
    }

    // public void testTwoMessages() throws Exception{
    //
    // MuleClient client = null;
    // MuleMessage reply1 = null;
    // MuleMessage reply2 = null;
    // String message1 =
    // "1. This is a very little message to be sent over from VM through Mule aboard RabbitMQ and then returned back to VM through mule.";
    // String message2 =
    // "2. This is a very little message to be sent over from VM through Mule aboard RabbitMQ and then returned back to VM through mule.";
    //
    // try {
    // client = new MuleClient();
    // client.dispatch("vm://inbound", message1, null);
    // reply1 = client.request("vm://outbound", 10000);
    // client.dispatch("vm://inbound", message2, null);
    // reply2 = client.request("vm://outbound", 10000);
    // } catch (MuleException e) {
    // fail("Exception raised: " + e.getDetailedMessage());
    // }
    //
    // assertNotNull(reply1);
    // assertNotNull(reply1.getPayload());
    // assertEquals(message1, reply1.getPayload());
    //
    // assertNotNull(reply2);
    // assertNotNull(reply2.getPayload());
    // assertEquals(message2, reply2.getPayload());
    // }
    //
    // public void testSpamMessages() throws Exception{
    //
    // MuleClient client = null;
    // Map<String,String> messg_list = new HashMap<String,String>();
    // int limit = 50;
    //
    // try {
    //
    // client = new MuleClient();
    //
    // for(int i = 0; i < limit; i++){
    // messg_list.put( "This is " + i, "This is " + i);
    // client.dispatch("vm://inbound", messg_list.get("This is " + i), null);
    // }
    //
    // for(int i = 0; i < limit; i++){
    // MuleMessage reply = client.request("vm://outbound", 5000);
    // assertNotNull(reply);
    // assertNotNull(reply.getPayload());
    // assertNotNull(messg_list.get(reply.getPayloadAsString()));
    // assertEquals(messg_list.get(reply.getPayloadAsString()),
    // reply.getPayloadAsString());
    // }
    //
    // } catch (MuleException e) {
    //
    // fail("Exception raised: " + e.getDetailedMessage());
    //
    // }
    //
    // }

}
