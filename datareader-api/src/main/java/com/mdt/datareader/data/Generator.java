package com.mdt.datareader.data;

public class Generator {

	private String name;
	private int interval;
	private String operation;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	@Override
	public String toString() {
		return "Generator [name=" + name + ", interval=" + interval + ", operation=" + operation + "]";
	}

}
