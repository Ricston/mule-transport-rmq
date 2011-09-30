/*
 * $Id: RmqMessageRequesterFactory.java 1486 2011-09-30 09:57:05Z claude.mamo $
 * --------------------------------------------------------------------------------------
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.rmq;

import org.mule.api.MuleException;
import org.mule.api.endpoint.InboundEndpoint;
import org.mule.api.transport.MessageRequester;
import org.mule.transport.AbstractMessageRequesterFactory;

/**
 * <code>FullMessageRequester</code> TODO document
 */
public class RmqMessageRequesterFactory extends AbstractMessageRequesterFactory
{

    public MessageRequester create(InboundEndpoint endpoint) throws MuleException
    {
        return new RmqMessageRequester(endpoint);
    }

}
