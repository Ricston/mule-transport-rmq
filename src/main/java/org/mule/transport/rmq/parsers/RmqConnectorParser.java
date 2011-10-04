/*
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.rmq.parsers;

public class RmqConnectorParser {
	
	/*
	 * =======================================
	 * POSSIBLE FORMATS - CONNECTOR URI FORMAT
	 * =======================================
	 * [user]:[password]@[host]:[port]/[virtualhost]
	 * [user]:[password]@[host]:[port]
	 * [host]:[port]/[virtualhost]
	 * [host]:[port]
	 */
	
	/*Connector URI in String format as retrieved.*/
	private String uri;
	
	/*Variables to be parsed from Connector URI.*/
	private String host;
	private int port;
	private String username;
	private String password;
	private String vhost;
	
	public RmqConnectorParser(String uri){
		//Setting the URI.
		this.uri = uri;
		//Creating a StringBuffer for optimized parsing.
		StringBuffer buf = new StringBuffer(uri);
		//Extract user details if found.
		if(buf.indexOf("@") != -1){
			username = buf.substring(0, buf.indexOf(":"));
			buf.delete(0, buf.indexOf(":") + 1);
			password = buf.substring(0, buf.indexOf("@"));
			buf.delete(0, buf.indexOf("@") + 1);
		}
		//Extract host.
		host = buf.substring(0, buf.indexOf(":"));
		buf.delete(0, buf.indexOf(":") + 1);
		//Extract port, and/or virtualhost.
		if(buf.indexOf("/") == -1){
			port = Integer.parseInt(buf.toString());
		} else {
			port = Integer.parseInt(buf.substring(0, buf.indexOf("/")));
			buf.delete(0, buf.indexOf("/") + 1);
			vhost = buf.toString();
		}
	}
	
	// //////////////////////////////////////////////////////////////////////
	// Getters and Setters
	// //////////////////////////////////////////////////////////////////////

	public void setHost(String host) {
		this.host = host;
	}

	public String getHost() {
		return host;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getPort() {
		return port;
	}

	public void setVhost(String vhost) {
		this.vhost = vhost;
	}

	public String getVhost() {
		return vhost;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getUri() {
		return uri;
	}

}