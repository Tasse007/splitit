package io.javabrains.splitit.controllers;

public class Wrapper {
	
	private int temporaryInteger;
	private String temporaryString;
	private double temporaryDouble;
	private double temporaryAmount;
	private String temporaryDescription;
	private String temporaryName;
	
	
	
	public int getTemporaryInteger() {
		return temporaryInteger;
	}
	public void setTemporaryInteger(int temporaryInteger) {
		this.temporaryInteger = temporaryInteger;
	}
	public String getTemporaryString() {
		return temporaryString;
	}
	public void setTemporaryString(String temporaryString) {
		this.temporaryString = temporaryString;
	}
	public double getTemporaryDouble() {
		return temporaryDouble;
	}
	public void setTemporaryDouble(double temporaryFloat) {
		this.temporaryDouble = temporaryFloat;
	}
	public double getTemporaryAmount() {
		return temporaryAmount;
	}
	public void setTemporaryAmount(double temporaryAmount) {
		this.temporaryAmount = temporaryAmount;
	}
	public String getTemporaryDescription() {
		return temporaryDescription;
	}
	public void setTemporaryDescription(String temporaryDescription) {
		this.temporaryDescription = temporaryDescription;
	}
	public String getTemporaryName() {
		return temporaryName;
	}
	public void setTemporaryName(String temporaryName) {
		this.temporaryName = temporaryName;
	}
	@Override
	public String toString() {
		return "Wrapper [temporaryInteger=" + temporaryInteger + ", temporaryString=" + temporaryString
				+ ", temporaryFloat=" + temporaryDouble + ", temporaryAmount=" + temporaryAmount
				+ ", temporaryDescription=" + temporaryDescription + ", temporaryName=" + temporaryName + "]";
	}
	
	
	

}
