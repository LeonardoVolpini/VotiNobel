package it.polito.tdp.nobel.model;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.nobel.db.EsameDAO;

public class Model {

	private List<Esame> partenza;
	private Set<Esame> soluzMigliore;
	private double mediaSoluzMigl;
	private int castiTestati;
	
	public Model() {
		EsameDAO dao= new EsameDAO();
		this.partenza= dao.getTuttiEsami();
		this.castiTestati=0;
	}
	
	public Set<Esame> calcolaSottoinsiemeEsami(int numeroCrediti) {
		Set<Esame> parziale = new HashSet<Esame>();
		this.soluzMigliore = new HashSet<Esame>();
		this.mediaSoluzMigl=0;
		//cerca1(parziale,0,numeroCrediti);
		cerca2(parziale,0,numeroCrediti);
		System.out.println("TODO!");
		return this.soluzMigliore;	
	}

	//complessita' 2^N
	private void cerca2(Set<Esame> parziale, int L, int m) {
		this.castiTestati++;
		//casi terminali:
		int crediti= sommaCrediti(parziale);
		if (crediti>m) {
			return;
		}
		if (crediti==m) {
			double media= calcolaMedia(parziale);
			if (media >this.mediaSoluzMigl ) {
			this.soluzMigliore= new LinkedHashSet<Esame>(parziale);
			this.mediaSoluzMigl=media;
			}
			return;
		}
		//sicuramente crediti < m se arrivo a questo punto
		//L==N -> non ci sono piu' esami da aggiungere
		if (L==partenza.size()) {
			return; //non ho piu' esami, ma non ho raggiunto i crediti sufficenti
		}
		
		//generazioni sotto-problemi
		//partenza[L] e' da aggiunggere o no?
		parziale.add(partenza.get(L));
		cerca2(parziale,L+1,m);
		parziale.remove(partenza.get(L));
		cerca2(parziale,L+1,m);
		
	}

	//complessita' N!
	private void cerca1(Set<Esame> parziale, int L, int m) {
		this.castiTestati++;
		// System.out.println("L = " + L + "\t" + parziale);  per stampare via via le soluzioni parziali
		//casi terminali:
		int crediti= sommaCrediti(parziale);
		if (crediti>m) {
			return;
		}
		if (crediti==m) {
			double media= calcolaMedia(parziale);
			if (media >this.mediaSoluzMigl ) {
				this.soluzMigliore= new HashSet<Esame>(parziale);
				this.mediaSoluzMigl=media;
			}
			return;
		}
		//sicuramente crediti < m se arrivo a questo punto
		//L==N -> non ci sono piu' esami da aggiungere
		if (L==partenza.size()) {
			return; //non ho piu' esami, ma non ho raggiunto i crediti sufficenti
		}
		
		/* //genero i sotto-problemi: //poco efficente
		for (Esame e : partenza) {
			if (!parziale.contains(e)) {
				parziale.add(e);
				cerca1(parziale,L+1,m);
				parziale.remove(e);
			}
		}*/
		for (int i=0; i<partenza.size(); i++) {  //leggermente piu efficente
			if (!parziale.contains(partenza.get(i)) && i>=L) {
				parziale.add(partenza.get(i));
				cerca1(parziale,L+1,m);
				parziale.remove(partenza.get(i));
			}
		}
		
	}
	
	public int getCasiTestati() {
		return this.castiTestati;
	}

	public double calcolaMedia(Set<Esame> esami) {
		
		int crediti = 0;
		int somma = 0;
		
		for(Esame e : esami){
			crediti += e.getCrediti();
			somma += (e.getVoto() * e.getCrediti());
		}
		
		return somma/crediti;
	}
	
	public int sommaCrediti(Set<Esame> esami) {
		int somma = 0;
		
		for(Esame e : esami)
			somma += e.getCrediti();
		
		return somma;
	}

}
