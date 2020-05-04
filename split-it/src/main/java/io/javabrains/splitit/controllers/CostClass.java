package io.javabrains.splitit.controllers;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CostClass {
	
	private int transactionId;
	
	//@NotNull
	//@Size(min=2, max=15)
	private String name;
	//Muss float werden:
	//@NotNull
	//@Min(0)
	private double cost;
	private String beschreibung;

	  public int getTransactionId() {
	    return transactionId;
	  }

	  public void setTransactionId(int id) {
	    this.transactionId = id;
	  }

	  public double getCost() {
	    return cost;
	  }

	  public void setCost(double cost) {
	    this.cost = cost;
	  }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBeschreibung() {
		return beschreibung;
	}

	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}
	  
	
	  

}
