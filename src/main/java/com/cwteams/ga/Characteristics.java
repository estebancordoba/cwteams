package com.cwteams.ga;
public class Characteristics {
	private double value;
	private String name;	
	private double min;
	private double max;
	
	public Characteristics(double value, String name, double min, double max){
		this.value=value;
		this.name=name;
		this.min=min;
		this.max=max;
	}
	
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public double getMin() {
		return min;
	}
	public void setMin(double min) {
		this.min = min;
	}
	public double getMax() {
		return max;
	}
	public void setMax(double max) {
		this.max = max;
	}	
}