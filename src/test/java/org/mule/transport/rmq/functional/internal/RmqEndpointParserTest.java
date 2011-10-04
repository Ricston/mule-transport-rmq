/*
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.rmq.functional.internal;

import org.mule.transport.rmq.parsers.RmqEndpointParser;

import junit.framework.TestCase;

public class RmqEndpointParserTest extends TestCase {
	
	/* 
	 * Possible Formats - ENDPOINT URIS
	 * 
	 * schema://exchange:queue/key
	 * schema://exchange/key
	 * 
	 */

	RmqEndpointParser rep1 = new RmqEndpointParser("rmq://myExchange:myQueue/#");
	RmqEndpointParser rep2 = new RmqEndpointParser("rmq://myExchange/#");
	RmqEndpointParser rep3 = new RmqEndpointParser("rmq://my.exchange:my.queue/org.com.*");
	RmqEndpointParser rep4 = new RmqEndpointParser("rmq://exchange/*");
	
	RmqEndpointParser rep5 = new RmqEndpointParser("rmq://exchange:queue{durable}/org.com.*");
	RmqEndpointParser rep6 = new RmqEndpointParser("rmq://exchange/");
	RmqEndpointParser rep7 = new RmqEndpointParser("rmq://exchange:queue{durable, exclusive, autoDelete}/org.com.*");
	
	public void testRmqEndpointParser() {
		assertNotNull(rep1);
		assertNotNull(rep2);
		assertNotNull(rep3);
		assertNotNull(rep4);
		assertNotNull(rep5);
		assertNotNull(rep6);
		assertNotNull(rep7);
	}

	public void testGetUri() {
		assertNotNull(rep1.getUri());
		assertNotNull(rep2.getUri());
		assertNotNull(rep3.getUri());
		assertNotNull(rep4.getUri());
		assertNotNull(rep5.getUri());
		assertNotNull(rep6.getUri());
		assertNotNull(rep7.getUri());
		assertEquals("rmq://myExchange:myQueue/#", rep1.getUri());
		assertEquals("rmq://myExchange/#", rep2.getUri());
		assertEquals("rmq://my.exchange:my.queue/org.com.*", rep3.getUri());
		assertEquals("rmq://exchange/*", rep4.getUri());
		assertEquals("rmq://exchange:queue{durable}/org.com.*", rep5.getUri());
		assertEquals("rmq://exchange/", rep6.getUri());
		assertEquals("rmq://exchange:queue{durable, exclusive, autoDelete}/org.com.*", rep7.getUri());
	}

	public void testGetExchangeName() {
		assertNotNull(rep1.getExchangeName());
		assertNotNull(rep2.getExchangeName());
		assertNotNull(rep3.getExchangeName());
		assertNotNull(rep4.getExchangeName());
		assertNotNull(rep5.getExchangeName());
		assertNotNull(rep6.getExchangeName());
		assertNotNull(rep7.getExchangeName());		
		assertEquals("myExchange", rep1.getExchangeName());
		assertEquals("myExchange", rep2.getExchangeName());
		assertEquals("my.exchange", rep3.getExchangeName());
		assertEquals("exchange", rep4.getExchangeName());
		assertEquals("exchange", rep5.getExchangeName());
		assertEquals("exchange", rep6.getExchangeName());
		assertEquals("exchange", rep7.getExchangeName());
	}

	public void testGetRoutingPattern() {
		assertNotNull(rep1.getRoutingPattern());
		assertNotNull(rep2.getRoutingPattern());
		assertNotNull(rep3.getRoutingPattern());
		assertNotNull(rep4.getRoutingPattern());
		assertNotNull(rep5.getRoutingPattern());
		assertNotNull(rep6.getRoutingPattern());
		assertNotNull(rep7.getRoutingPattern());	
		assertEquals("#", rep1.getRoutingPattern());
		assertEquals("#", rep2.getRoutingPattern());
		assertEquals("org.com.*", rep3.getRoutingPattern());
		assertEquals("*", rep4.getRoutingPattern());
		assertEquals("org.com.*", rep5.getRoutingPattern());
		assertEquals("", rep6.getRoutingPattern());
		assertEquals("org.com.*", rep7.getRoutingPattern());
	}

	public void testGetQueueName() {
		assertNotNull(rep1.getQueueName());
		assertNull(rep2.getQueueName());
		assertNotNull(rep3.getQueueName());
		assertNull(rep4.getQueueName());
		assertNotNull(rep5.getQueueName());
		assertNull(rep6.getQueueName());
		assertNotNull(rep7.getQueueName());	
		assertEquals("myQueue", rep1.getQueueName());
		assertEquals(null, rep2.getQueueName());
		assertEquals("my.queue", rep3.getQueueName());
		assertEquals(null, rep4.getQueueName());
		assertEquals("queue", rep5.getQueueName());
		assertEquals(null, rep6.getQueueName());
		assertEquals("queue", rep7.getQueueName());
	}
	
	public void testIsDurable(){
		assertNotNull(rep1.isDurable());
		assertNotNull(rep2.isDurable());
		assertNotNull(rep3.isDurable());
		assertNotNull(rep4.isDurable());
		assertNotNull(rep5.isDurable());
		assertNotNull(rep6.isDurable());
		assertNotNull(rep7.isDurable());
		assertFalse(rep1.isDurable());
		assertFalse(rep2.isDurable());
		assertFalse(rep3.isDurable());
		assertFalse(rep4.isDurable());
		assertTrue(rep5.isDurable());
		assertFalse(rep6.isDurable());
		assertTrue(rep7.isDurable());
	}
	
	public void testIsExclusive(){
		assertNotNull(rep1.isExclusive());
		assertNotNull(rep2.isExclusive());
		assertNotNull(rep3.isExclusive());
		assertNotNull(rep4.isExclusive());
		assertNotNull(rep5.isExclusive());
		assertNotNull(rep6.isExclusive());
		assertNotNull(rep7.isExclusive());
		assertFalse(rep1.isExclusive());
		assertFalse(rep2.isExclusive());
		assertFalse(rep3.isExclusive());
		assertFalse(rep4.isExclusive());
		assertFalse(rep5.isExclusive());
		assertFalse(rep6.isExclusive());
		assertTrue(rep7.isExclusive());
	}
	
	public void testIsAutoDelete(){
		assertNotNull(rep1.isAutoDelete());
		assertNotNull(rep2.isAutoDelete());
		assertNotNull(rep3.isAutoDelete());
		assertNotNull(rep4.isAutoDelete());
		assertNotNull(rep5.isAutoDelete());
		assertNotNull(rep6.isAutoDelete());
		assertNotNull(rep7.isAutoDelete());
		assertFalse(rep1.isAutoDelete());
		assertFalse(rep2.isAutoDelete());
		assertFalse(rep3.isAutoDelete());
		assertFalse(rep4.isAutoDelete());
		assertFalse(rep5.isAutoDelete());
		assertFalse(rep6.isAutoDelete());
		assertTrue(rep7.isAutoDelete());
	}
	
}