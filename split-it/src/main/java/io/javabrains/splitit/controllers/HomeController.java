package io.javabrains.splitit.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Controller // No RestController because we use a HTML instead of a REST API
public class HomeController implements WebMvcConfigurer {
	
	public static List<Transaction> transaktionen = new ArrayList<>();
	public int id=0;
	public static List<Person> personList = new ArrayList<>();
	public static List<DirektZahlung> direktZahlungen = new ArrayList<DirektZahlung>();
	public double total_cost = 0;
	public double durchschnittlicheAusgaben=0;
	public double offeneZahlungen =0;

	@GetMapping("/")
	public String home(Model model) {

		if (personList.size() > 0) {
			
			updateNumbers();
			model.addAttribute("total_cost", abrunden(total_cost));
			model.addAttribute("average_cost", abrunden(durchschnittlicheAusgaben));
			
			int counter =0;
			if(transaktionen.size()>2) {
			for(int i=transaktionen.size()-1; i> (transaktionen.size()-4); i--) {
				model.addAttribute("transaction"+(counter)+"a", transaktionen.get(i).getId());
				model.addAttribute("transaction"+(counter)+"b", transaktionen.get(i).getSurname());
				model.addAttribute("transaction"+(counter)+"c", transaktionen.get(i).getDescription());
				model.addAttribute("transaction"+(counter)+"d", abrunden(transaktionen.get(i).getAmount())+"€");
				counter++;
			}}
		
		} else {
			model.addAttribute("total_cost", 0);
			model.addAttribute("average_cost", 0);
		}
		
		
		// return name of home.html file in src/main/resources/templates
		return "home";
	}

	@GetMapping("/about")
	public String about(Model model) {
		model.addAttribute("author",
				"Tassilo Habig - Hindenburgstraße 13 - 78467 Konstanz - GERMANY - Tel.: +49-(0)-178-61-87-657 - E-Mail: tassilohabig@web.de");
		return "about";
	}

	@GetMapping("/impressum")
	public String impressum(Model model) {
		model.addAttribute("author",
				"Tassilo Habig - Hindenburgstraße 13 - 78467 Konstanz - GERMANY - Tel.: +49-(0)-178-61-87-657 - E-Mail: tassilohabig@web.de");
		return "about";
	}

	// Eingeben der Daten
	@GetMapping("/costURL")
	public String costForm(Model model) {
		model.addAttribute("costObject", new CostClass());
		// shows cost.html
		return "costhtml";
	}
	
	@GetMapping("/useful")
	public String callUseful(){
		return "useful";
		
	}
	
	

	
	
	// Ausgeben der Daten
	@PostMapping("/costURL")
	public String costSubmission(@ModelAttribute CostClass cost, Model model) {
		
		
		
		boolean personInSystem = false;
		int index=0;
		
		for (int i = 0; i < personList.size(); i++) {
			if (cost.getName().equalsIgnoreCase(personList.get(i).getName())) {
				personInSystem = true;
				index=i;}
		}
		
		if (personList.size() <= 9) {
			
			transaktionen.add(new Transaction(id, cost.getName(), cost.getCost(), cost.getBeschreibung()));
			model.addAttribute("transactionId", "Transaktions-Id: " +id);
			id++;

			model.addAttribute("successfully_saved", cost.getName() +", folgende Ausgabe wurde erfolgreich gespeichert:");			

			System.out.println("if (personList.size() <= 9) {");

			if(personInSystem==false) {
			System.out.println("personInSystem==false");
			personList.add(new Person(cost.getName(), cost.getCost()));

			}else if (personInSystem==true) {
			System.out.println("else if (personInSystem==true");

			personList.get(index).setGetrageneKosten(personList.get(index).getGetrageneKosten() + cost.getCost());
			} else {
			System.out.println("else SONDERFALL");
			}
			
			}else {
			model.addAttribute("unsuccessfully_saved", "Die maximale Anzahl an Personen wurde überschritten! "+ cost.getName() +", folgende Ausgabe wurde nicht gespeichert!");			

			model.addAttribute("maxPeople", "Die maximale Anzahl an Personen ist bereits erreicht! " + cost.getName()
					+ " wurde nicht hinzugefügt!");
		}
		

		
		updateNumbers();
		System.out.println("Hier sind die vorhandenen personen:");
		for (int i = 0; i < personList.size(); i++) {
			String changingString = "name";
			changingString = changingString + i;
			model.addAttribute(changingString, personList.get(i).getName() + " hat "
						+ abrunden(personList.get(i).getGetrageneKosten()) + " Euro ausgegeben. Deine Differenz zu den Durchschnittskosten beträgt " + abrunden(personList.get(i).getDifferenzZumDurchschnitt())+"€.");
			
			System.out.println(personList.get(i).toString());
		}
		
		
		return "result";
	}

	

	public void updateNumbers() {
		total_cost=0;
		for (Person pers : personList) {
			total_cost = total_cost + pers.getGetrageneKosten();
			}
		durchschnittlicheAusgaben = total_cost / (personList.size());
		
		for(Person pers : personList) {
				pers.setDifferenzZumDurchschnitt(pers.getGetrageneKosten() - durchschnittlicheAusgaben);
				}
	}
	
	@GetMapping("/clear")
	public String clearAllData(Model model) {
		
		transaktionen.removeAll(transaktionen);
		personList.removeAll(personList);
		direktZahlungen.removeAll(direktZahlungen);
		
		id=0;
		total_cost = 0;
		durchschnittlicheAusgaben=0;
		offeneZahlungen =0;
		
		model.addAttribute("data_cleared", "Alle Daten wurden erfolgreich gelöscht!");
		model.addAttribute("new_project", "Du kannst ein neues Projekt anlegen indem du einfach neue Ausgaben einträgst.");
		System.out.println("Alle Daten gelöscht." + transaktionen.size() +" "+personList.size());
	return "debtcalculation";
	}
	
	
	
	
	
	@GetMapping("/debtcalculation")
	public void calculateDebt(Model model) {
		
		offeneZahlungen =0;
		
		if (personList.size() > 0) {
			
			updateNumbers();
			model.addAttribute("total_cost", abrunden(total_cost));
			model.addAttribute("average_cost", abrunden(durchschnittlicheAusgaben));
		
		} else {
			model.addAttribute("total_cost", 0);
			model.addAttribute("average_cost", 0);
		}

	String info = "info";
	int counter =0;
	int bcounter =1;
	//Dazu schauen wir uns nacheinander alle Personen an...
	model.addAttribute(info+counter++,"Verrechnung der Gruppenkosten:");
    for (Person pers : personList) {

        //Hat die Person mehr als der Durchschnitt gezahlt fließt die Differenz daraus in die offenen Zahlungen, die noch umverteilt werden müssen.
        if (pers.getDifferenzZumDurchschnitt() > 0) {
            offeneZahlungen += pers.getDifferenzZumDurchschnitt();
           model.addAttribute(info+counter++, ("● " +pers.getName() + " hat insgesamt " + abrunden(pers.getrageneKosten) + "€ bezahlt und bekommt noch "+ abrunden(pers.getDifferenzZumDurchschnitt())   +    "€."));
        } else if (pers.getDifferenzZumDurchschnitt() < 0) {
        	model.addAttribute(info+counter++, ("● " +pers.getName() + " hat insgesamt " + abrunden(pers.getrageneKosten) + "€ bezahlt und muss noch "+abrunden(pers.getDifferenzZumDurchschnitt()*(-1))+"€ bezahlen."));
        } else {
        	model.addAttribute(info+counter++,("● " +pers.getName() + " hat insgesamt " + abrunden(pers.getrageneKosten) + "€ bezahlt und muss niemandem mehr etwas bezahlen"));// Kosten der
        	// Pers=Durchschnittskosten
        }

    }
    
    model.addAttribute("verrechnen","Noch zu verrechnende Kosten der Gruppe: " + abrunden(offeneZahlungen) + "€.");
    

    //Schauen ob Direktzahlungen vorhanden; Bekommt Person noch Geld bzw. muss Person noch Geld zurückzahlen?
    for (Person pers : personList) {
        for (DirektZahlung dz : direktZahlungen) {
            if (dz.von.equals(pers)) {
                pers.bekommtDirektzahlung = true;
            }

            if(dz.an.equals(pers)){
                pers.zahltDirektzahlung=true;
            }

        }
    }

    model.addAttribute("verrechnen2","Verrechnung der Gesamtkosten:");
    //Es wird für alle Personen geschaut, ob sie eine P- Person ist die noch Geld bezahlen muss
    for (Person persImMinus : personList) {
        //P- wird man wenn man weniger als der Durchschnitt gezahlt hat oder Direktzahlungen gewährt wurden
        if (persImMinus.getDifferenzZumDurchschnitt() < 0 || persImMinus.zahltDirektzahlung==true) {
            //Es wird für alle Personen geschaut, ob sie eine P+ Person ist die noch Geld bekommt.
            for (Person persImPlus : personList) {
                //P+ Person wird man indem man mehr als der Durchschnitt gezahlt hat oder Direktzahlungen für jemanden vorgestreckt hat. Kosten können nicht mit sich selber verrechnet werden.
                if ((persImPlus.getDifferenzZumDurchschnitt() > 0 || persImPlus.bekommtDirektzahlung == true) && !persImMinus.equals(persImPlus)) {

                    double betrag =0;

                    //Wenn P+ Gruppengeld bekommt und P- Geld an Gruppe zahlen muss, werden die offenen Ausgaben anteilig verrechnet
                    if(persImPlus.getDifferenzZumDurchschnitt() > 0 && persImMinus.getDifferenzZumDurchschnitt()<0){
                        betrag=  (persImMinus.getDifferenzZumDurchschnitt()
                                * (persImPlus.getDifferenzZumDurchschnitt() / offeneZahlungen)) * (-1);
                    }

                    //Wenn die P+ DZ erhält oder P- auch DZ erhält, kann geschaut werden, ob man Kosten verrechnen kann
                    if (persImPlus.bekommtDirektzahlung == true || persImMinus.bekommtDirektzahlung == true) {
                        for (DirektZahlung dz : direktZahlungen) {
                            //Sind DZ von P+ an P- vorhanden
                            if (dz.getBereitsVerrechnet()==false && dz.von.equals(persImPlus) && dz.an.equals(persImMinus)) {
                                for (DirektZahlung dz2 : direktZahlungen) {
                                    //Sind gleichzeitig DZ von P- an P+ vorhanden
                                    if (dz2.getBereitsVerrechnet()==false && dz2.von.equals(persImMinus) && dz2.an.equals(persImPlus)) {
                                        //Schauen ob der von P+ zu zahlende Betrag kleiner als der von P- zu zahlende Betrag ist
                                        double direktZahlungsVerrechnung = dz.getBetrag() - dz2.getBetrag();
                                        //Wenn ja, Betrag verrechnen, auf den Gruppenbetrag addieren und die beiden DZ als erledigt markieren.
                                        if (direktZahlungsVerrechnung > 0) {
                                            betrag += direktZahlungsVerrechnung;
                                            dz.setBereitsVerrechnet(true);
                                            dz2.setBereitsVerrechnet(true);
                                            //Wenn Verrechnung ins negative gehen würde, da P+ mehr zahlen muss als P-, dann nur DZ an P- auf Gruppenbetrag draufschlagen und als erledigt markieren.
                                        } else if (direktZahlungsVerrechnung<0){
                                            betrag += dz.getBetrag();
                                            dz.setBereitsVerrechnet(true);
                                        }
                                    //Falls nur DZ von P+ an P- vorhanden, die Kosten bei P- draufschlagen und die DZ als erledigt markieren
                                    }else if(dz.getBereitsVerrechnet()==false){
                                        betrag += dz.getBetrag();
                                        dz.setBereitsVerrechnet(true);

                                    }
                                }
                            }
                        }
                    }

                    if(betrag!=0) {
                    	model.addAttribute("b"+info+bcounter++,"● " +persImMinus.getName() + " muss an " + persImPlus.getName() + " " + abrunden(betrag) + "€ zahlen.");
                    }

                     /*Wenn die P+ DZ erhält und die P- nur DZ zahlen muss, wird geschaut ob zwischen P+ und P- eine Direktzahlung offen ist. Falls ja wird diese direkt dem Betrag hinzugefügt.
                    if (persImPlus.bekommtDirektzahlung == true && persImMinus.zahltDirektzahlung == true
                            && persImMinus.bekommtDirektzahlung == false) {
                        for (DirektZahlung dz : direktZahlungen) {
                            if (dz.getBereitsVerrechnet()==false && dz.von.equals(persImPlus) && dz.an.equals(persImMinus)) {
                                betrag += dz.getBetrag();
                                dz.setBereitsVerrechnet(true);
                            }
                        }
                    }*/

                }
            }
        }
    }
}



public static double abrunden(double zahl){
    return Math.round( zahl * 100 ) / 100.0;
}

class DirektZahlung {

    Person von;
    Person an;
    double betrag=0;
    boolean bereitsVerrechnet=false;

    public Person getVon() {
        return von;
    }
    public void setVon(Person von) {
        this.von = von;
    }
    public Person getAn() {
        return an;
    }
    public void setAn(Person an) {
        this.an = an;
    }
    public double getBetrag() {
        return betrag;
    }
    public void setBetrag(double betrag) {
        this.betrag = betrag;
    }

    public boolean getBereitsVerrechnet() {
        return bereitsVerrechnet;
    }

    public void setBereitsVerrechnet(boolean bereitsVerrechnet) {
        this.bereitsVerrechnet = bereitsVerrechnet;
    }

    public DirektZahlung(Person von, Person an, double betrag) {
        super();
        this.von = von;
        this.an = an;
        this.betrag = betrag;
        this.bereitsVerrechnet=false;
    }
    @Override
    public String toString() {
        return "DirektZahlung [von=" + von.toString() + ", an=" + an.toString() + ", betrag=" + betrag + "]";
    }


}
}	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	



