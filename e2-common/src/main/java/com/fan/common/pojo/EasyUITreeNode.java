package com.fan.common.pojo;

import java.io.Serializable;

public class EasyUITreeNode implements Serializable{

	private static final long serialVersionUID = 1L;
	/**
	 * {    
	    "id": 2,    
	    "text": "Node 2",    
	    "state": "closed" --有子节点是closed，没有子节点是open
	    }
	 */
	private long id;
	private String text;
	private String state;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
}
