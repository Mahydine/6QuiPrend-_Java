package appli;

public class Joueur {	
	private String nom;//nom du joueur
	private int score = 0;//nombre de tdb cumulés
	private ListeCarte cartes = new ListeCarte();//cartes en main
	private int ordreABtique = 0;//son ordre alphabétique avec les autres joueurs

	public Joueur(String nom, int i) {
		this.nom=nom;
		this.ordreABtique = i;
	}
	
	
	public ListeCarte getCartes() {
		return cartes;
	}
	public void setCartes(ListeCarte cartes) {
		this.cartes = cartes;
	}


	public int getScore() {
		return score;
	}


	public String getNom() {
		return nom;
	}


	public int getOrdreABtique() {
		return ordreABtique;
	}

	//additione un entier poidS au score du joueur
	public void score_manche(int poidS) {
		this.score+=poidS;
		
	}
}
