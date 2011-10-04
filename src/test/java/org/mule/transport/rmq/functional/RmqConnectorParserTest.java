/*
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.rmq.functional;

import org.mule.transport.rmq.parsers.RmqConnectorParser;

import junit.framework.TestCase;

public class RmqConnectorParserTest extends TestCase {
	
	/* 
	 * Possible Formats - CONNECTOR URIS
	 * 
	 * host:port/virtualHost
	 * host:port
	 * username:password@host:port/virtualHost
	 * username:password@host:port
	 * 
	 */

	RmqConnectorParser rcp1 = new RmqConnectorParser("127.0.0.1:5672/virtualHost");
	RmqConnectorParser rcp2 = new RmqConnectorParser("localhost:5671");
	RmqConnectorParser rcp3 = new RmqConnectorParser("rabbit:carrot@134.256.178.290:5671/rabbitVHost");
	RmqConnectorParser rcp4 = new RmqConnectorParser("joseph:secret@localhost:5672");
	
	public void testRmqConnectorParser() {
		assertNotNull(rcp1);
		assertNotNull(rcp2);
		assertNotNull(rcp3);
		assertNotNull(rcp4);
	}

	public void testGetHost() {
		assertNotNull(rcp1.getHost());
		assertNotNull(rcp2.getHost());
		assertNotNull(rcp3.getHost());
		assertNotNull(rcp4.getHost());
		assertEquals("127.0.0.1", rcp1.getHost());
		assertEquals("localhost", rcp2.getHost());
		assertEquals("134.256.178.290", rcp3.getHost());
		assertEquals("localhost", rcp4.getHost());
	}

	public void testGetPort() {
		assertNotNull(rcp1.getPort());
		assertNotNull(rcp2.getPort());
		assertNotNull(rcp3.getPort());
		assertNotNull(rcp4.getPort());
		assertEquals(5672, rcp1.getPort());
		assertEquals(5671, rcp2.getPort());
		assertEquals(5671, rcp3.getPort());
		assertEquals(5672, rcp4.getPort());
	}

	public void testGetVhost() {
		assertNotNull(rcp1.getVhost());
		assertNull(rcp2.getVhost());
		assertNotNull(rcp3.getVhost());
		assertNull(rcp4.getVhost());
		assertEquals("virtualHost", rcp1.getVhost());
		assertEquals(null, rcp2.getVhost());
		assertEquals("rabbitVHost", rcp3.getVhost());
		assertEquals(null, rcp4.getVhost());
	}

	public void testGetUsername() {
		assertNull(rcp1.getUsername());
		assertNull(rcp2.getUsername());
		assertNotNull(rcp3.getUsername());
		assertNotNull(rcp4.getUsername());
		assertEquals(null, rcp1.getUsername());
		assertEquals(null, rcp2.getUsername());
		assertEquals("rabbit", rcp3.getUsername());
		assertEquals("joseph", rcp4.getUsername());
	}

	public void testGetPassword() {
		assertNull(rcp1.getPassword());
		assertNull(rcp2.getPassword());
		assertNotNull(rcp3.getPassword());
		assertNotNull(rcp4.getPassword());
		assertEquals(null, rcp1.getPassword());
		assertEquals(null, rcp2.getPassword());
		assertEquals("carrot", rcp3.getPassword());
		assertEquals("secret", rcp4.getPassword());
	}
}