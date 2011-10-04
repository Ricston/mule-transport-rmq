/*
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.rmq.essentials;

import java.util.Map;

public class Exchange {
	
	/*Variables to be set.*/
	private String name;
	private String type;
	private boolean durable = false;
	private boolean autoDelete = false;
	private Map<String,Object> arguments = null;
	
	public Exchange(){
	}
	
	public Exchange(String name){
		this.name = name;
		if (name.equals("amq.topic")) type = "topic"; 
		if (name.equals("amq.fanout")) type = "fanout";
		if (name.equals("amq.direct")) type = "direct";
	}
	
	public Exchange(String name, String type){
		this.name = name;
		this.type = type;
	}
	
	public Exchange(String name, String type, boolean durable, boolean autoDelete){
		this.name = name;
		this.type = type;
		this.durable = durable;
		this.autoDelete = autoDelete;
	}
	
	public Exchange(String name, String type, boolean durable, boolean autoDelete, Map<String,Object> arguments){
		this.name = name;
		this.type = type;
		this.durable = durable;
		this.autoDelete = autoDelete;
		this.arguments = arguments;
	}
	
	// //////////////////////////////////////////////////////////////////////
	// Getters and Setters
	// //////////////////////////////////////////////////////////////////////
	
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getType() {
		return type;
	}
	public void setDurable(boolean durable) {
		this.durable = durable;
	}
	public boolean isDurable() {
		return durable;
	}
	public void setAutoDelete(boolean autoDelete) {
		this.autoDelete = autoDelete;
	}
	public boolean isAutoDelete() {
		return autoDelete;
	}
	public void setArguments(Map<String,Object> arguments) {
		this.arguments = arguments;
	}
	public Map<String,Object> getArguments() {
		return arguments;
	}
	
}