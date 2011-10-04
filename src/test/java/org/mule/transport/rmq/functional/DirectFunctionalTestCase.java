/*
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

public class DirectFunctionalTestCase extends org.mule.tck.FunctionalTestCase {
	
	protected String getConfigResources() {
		return "src/test/resources/direct-mule-config.xml";
	}
	
	public void testSingleMessage() throws Exception{
		
		MuleClient client = null;
		MuleMessage reply = null;
		String message = "This is a very little message to be sent over from VM through Mule aboard RabbitMQ and then returned back to VM through mule.";
		
		try {
			client = new MuleClient();
			client.dispatch("vm://inbound", message, null);
			reply = client.request("vm://outbound",	5000);
		} catch (MuleException e) {
			fail("Exception raised: " + e.getDetailedMessage());
		}
		
		assertNotNull(reply);
		assertNotNull(reply.getPayload());
		assertEquals(message, reply.getPayload());
	}

}
