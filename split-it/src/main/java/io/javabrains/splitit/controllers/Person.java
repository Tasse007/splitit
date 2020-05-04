package io.javabrains.splitit.controllers;

import java.util.ArrayList;
import java.util.List;

public class Person {
	
	
	    public String name;
	    public double getrageneKosten;
	    public double differenzZumDurchschnitt;
	    public boolean bekommtDirektzahlung=false;
	    public boolean zahltDirektzahlung=false;

	    public static List<Person> personen = new ArrayList<Person>();

	    public Person(String name, double kosten) {
	        this.name = name;
	        this.getrageneKosten = kosten;
	        personen.add(this);
	    }

	    public double getGetrageneKosten() {
	        return getrageneKosten;
	    }

	    public void setGetrageneKosten(double kosten) {
	        this.getrageneKosten = kosten;
	    }

	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	    public double getDifferenzZumDurchschnitt() {
	        return differenzZumDurchschnitt;
	    }

	    public void setDifferenzZumDurchschnitt(double diffAvg) {
	        this.differenzZumDurchschnitt = diffAvg;
	    }

		@Override
		public String toString() {
			return "Person [name=" + name + ", getrageneKosten=" + getrageneKosten + ", differenzZumDurchschnitt="
					+ differenzZumDurchschnitt + ", bekommtDirektzahlung=" + bekommtDirektzahlung
					+ ", zahltDirektzahlung=" + zahltDirektzahlung + "]";
		}
	    
	    

}
