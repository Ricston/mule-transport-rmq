/*
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.rmq;

import java.net.URI;
import java.util.Properties;

import org.mule.api.endpoint.MalformedEndpointException;
import org.mule.endpoint.AbstractEndpointURIBuilder;

public class RmqEndpointURIBuilder extends AbstractEndpointURIBuilder {
	
	@Override
	protected void setEndpoint(URI uri, Properties props) throws MalformedEndpointException {
		//Removes the schema thus resulting in the actual URI.
		address = uri.toString().substring(6);
	}

}