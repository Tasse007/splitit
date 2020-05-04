package io.javabrains.splitit.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Controller // No RestController because we use a HTML instead of a REST API
public class TransactionController implements WebMvcConfigurer {

	
	private HomeController homecontroller;

	@GetMapping("/transactions")
	private String getTransactionHistory(Model model) {
	
		for(int i=0; i< HomeController.transaktionen.size(); i++) {
			
			model.addAttribute("transaction"+(i)+"a", HomeController.transaktionen.get(i).getId());
			model.addAttribute("transaction"+(i)+"b", HomeController.transaktionen.get(i).getSurname());
			model.addAttribute("transaction"+(i)+"c", HomeController.transaktionen.get(i).getDescription());
			model.addAttribute("transaction"+(i)+"d", abrunden(HomeController.transaktionen.get(i).getAmount())+"€");
			
			System.out.println("GETM transaction size:" +HomeController.transaktionen.size());

			System.out.println(HomeController.transaktionen.get(i).toString());
		}
		model.addAttribute("wrapperObject", new Wrapper());
		
		return "transactionhistory";
		
	}
	
	

	@PostMapping("/transactions")
	private String changeTransactionHistory(@ModelAttribute Wrapper w, Model model) {
		System.out.println("w.getTemporaryInteger: " + w.getTemporaryInteger());
		
		int index=0;
		int counter=0;
		double old_amount=0;
		double new_amount =w.getTemporaryAmount();
		String old_person="";
		String new_person= w.getTemporaryName();
		Transaction transaktion =null;
		Person oldPerson = null;
		Person newPerson = null;
		
		//Transaktion finden 
		for(int i=0; i< HomeController.transaktionen.size(); i++) {
			if(HomeController.transaktionen.get(i).getId() == w.getTemporaryInteger()) {
			
			transaktion= HomeController.transaktionen.get(i);
			old_amount = transaktion.getAmount();
			old_person = transaktion.getSurname();
			index=i;
		
			}
		}
		
		
		
		//ggf. Person ändern
		//Finde alte Person und zähle ihre Transaktionen --> B ist nicht mehr in Transaktionsliste, deshalb geht compiler nie in if-statement
		for(int b=0; b< HomeController.personList.size();b++) {
			if(HomeController.personList.get(b).getName().equalsIgnoreCase(old_person)) {
				oldPerson= HomeController.personList.get(b);
			}
		}
		
		for(int y=0; y< HomeController.transaktionen.size(); y++) {
			if(HomeController.transaktionen.get(y).getSurname().equalsIgnoreCase(old_person)){
				counter++;
			}
		}
		
		//Prüfen ob neue Person schon im System erfasst ist
		
		boolean newPersonInSystem=false;
		for(int z=0; z<HomeController.personList.size(); z++) {
			if(HomeController.personList.get(z).getName().equalsIgnoreCase(new_person)) {
				newPerson = HomeController.personList.get(z);
				newPersonInSystem=true;
			}
		}
		
		
		
		
			
			//Wenn sich die Person ändert
		if(!(old_person.equalsIgnoreCase(new_person))) {
			//wie viele Transaktionen hat die alte Person getätigt? Bei einer Transaktion muss die alte Person entfernt/überschrieben werden
			//Bei mehr als einer Transaktion muss die alte Peroson beibehalten werden.
			
			//Alte Person hat nur eine Transaktion --> Person muss gelöscht werden und Daten auf neue Person hinzufügen
			if(counter==1) {
				if(newPersonInSystem==true) {
					HomeController.personList.remove(oldPerson);
					newPerson.setGetrageneKosten(newPerson.getGetrageneKosten() +new_amount);
				}else if(newPersonInSystem==false) {
					HomeController.personList.remove(oldPerson);
					Person myNewPerson = new Person(new_person, new_amount);
					HomeController.personList.add(myNewPerson);
				}
			}else {	
			
			//Alte Person hat mehrere Transaktionen --> Neue Person hat noch keine Transaktionen, dann neue Person erstellen und neue und alte Daten anpassen
			if(newPersonInSystem==false) {	
			
			Person myNewPerson = new Person(new_person, new_amount);
			HomeController.personList.add(myNewPerson);
			oldPerson.setGetrageneKosten(oldPerson.getGetrageneKosten() - old_amount);
			
			//Alte Person hat mehrere Transaktionen --> Neue Person ist schon im System, dann neue und alte Daten anpassen
			}else if(newPersonInSystem==true) {
			
			newPerson.setGetrageneKosten(newPerson.getGetrageneKosten() + new_amount);	
			oldPerson.setGetrageneKosten(oldPerson.getGetrageneKosten() - old_amount);
			}
			}
			}else {
			
			oldPerson.setGetrageneKosten(oldPerson.getGetrageneKosten() - old_amount +new_amount);
			}
		
		//Transaktion wird geupdated
			
			transaktion.setAmount(w.getTemporaryAmount());
			transaktion.setDescription(w.getTemporaryDescription());
			transaktion.setSurname(w.getTemporaryName());
			
		
	for(int a=0; a< HomeController.transaktionen.size(); a++) {
			
			model.addAttribute("transaction"+(a)+"a", HomeController.transaktionen.get(a).getId());
			model.addAttribute("transaction"+(a)+"b", HomeController.transaktionen.get(a).getSurname());
			model.addAttribute("transaction"+(a)+"c", HomeController.transaktionen.get(a).getDescription());
			model.addAttribute("transaction"+(a)+"d", abrunden(HomeController.transaktionen.get(a).getAmount())+"€");
			
			System.out.println("POSTM transaction size:" +HomeController.transaktionen.size());

			System.out.println(HomeController.transaktionen.get(a).toString());
		}
		model.addAttribute("transaction_updated", "Die Transaktion mit der Id "+transaktion.getId()+ " wurde erfolgreich aktualisiert!");
		return "changetransaction";

	}
	
	
	public static double abrunden(double zahl){
	    return Math.round( zahl * 100 ) / 100.0;
	}

}

