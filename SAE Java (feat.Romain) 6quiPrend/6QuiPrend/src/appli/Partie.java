package appli;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;



public class Partie {
	private static final int NB_SERIES = 4;//nombre de series
	private static final int NB_CARTES = 10;//nombre de cartes par personnes en début de tour
	private static final int MAX_SCORE = 60;//limite de score
	private static final int MAX_CARTES = 104;//nombre de carte en tout dans un jeu

	private ArrayList<Joueur> liste_joueurs = new ArrayList<>();//liste des joueurs
	private ListeCarte[] series = new ListeCarte[NB_SERIES];//liste des series
	
	public Partie(File f) {
		for(int i = 0;i<NB_SERIES;++i)series[i] = new ListeCarte();
		initialisationJoueur(f);
	}
	public int nb_joueurs() {
		return liste_joueurs.size();
	}
	
	//initialisation des series avec une carte quelles piochent
	private void initialisationSeries(ListeCarte pioche) {
		ArrayList<Integer> temp = new ArrayList<>();
		for(int i = 0;i<NB_SERIES;++i)
			temp.add(pioche.peakDebut());
		temp.sort(null);
		for(int i = 0;i<NB_SERIES;++i)
			series[i] = new ListeCarte(temp.get(i));
		
	}
	//initialise la liste des joueurs de la partie à partir dun fichier "fichier"
	private void initialisationJoueur(File fichier) {
		//File fichier = new File("src/config.txt");	
		Scanner lecteur =null;
		ArrayList<String>liste_noms = new ArrayList<>();
		try { lecteur = new Scanner(fichier); } // Lecture du fichier config
		catch (FileNotFoundException e) { e.printStackTrace(); } // en cas d'erreur, arrêt du programme + message d'erreur 'fichier introuvable'
		while (lecteur.hasNextLine()) { liste_noms.add((lecteur.nextLine())); } // tant qu'il y a des lignes, crée un nouveau joueur              	
		@SuppressWarnings("unchecked")
		ArrayList<String> temp = (ArrayList<String>) liste_noms.clone();
		liste_noms.sort(null);
		for(String nom : temp) 
			liste_joueurs.add(new Joueur(nom,liste_noms.indexOf(nom)));

		distribution_cartes();
		
	}
	//distribution des cartes entres tous les joueurs et les séries
	private void distribution_cartes() {
		ListeCarte pioche = new ListeCarte();
		pioche.initialise_pioche(MAX_CARTES);
		
		ArrayList<ArrayList<Integer>> mains_tmp = new ArrayList<>();
		for(int j = 0;j<liste_joueurs.size();++j) {
			mains_tmp.add(new ArrayList<Integer>());
			liste_joueurs.get(j).getCartes().clear();
		}
		
		for(int i = 0;i<NB_CARTES;++i) 
			for(int j = 0;j<liste_joueurs.size();++j) 
				mains_tmp.get(j).add(pioche.peakDebut());
		for(int i = 0;i<liste_joueurs.size();++i)
			mains_tmp.get(i).sort(null);
		for(int i = 0;i<NB_CARTES;++i) 
			for(int j = 0;j<liste_joueurs.size();++j) 
				liste_joueurs.get(j).getCartes().addEnd(mains_tmp.get(j).get(i));
		
		initialisationSeries(pioche);
	}
	//renvoie siun joueur a atteint un score depassant 60tdb
	public boolean checkEnd() {
		boolean s = false;
		for(int i = 0; i < nb_joueurs() && !s;++i)
			if(liste_joueurs.get(i).getScore()>=MAX_SCORE)
				s = true;
		return s;
	}
	//renvoie la liste des joueurs
	public ArrayList<Joueur>getJoueurs() {
		return liste_joueurs;
	}
	
	public int nb_series() {
		return NB_SERIES;
	}

	public ListeCarte[] getSeries() {
		return series;
	}
	

	//renvoie la plus petite carte parmie les dernières des séries
	public int min_serie() {
		int s = 105;
		for(ListeCarte cs : series) {
			if(cs.getEnd()<s)s=cs.getEnd();
		}
		return s;
	}
	//initialise une serie à partie d'une carte
	public void setSerie(int ids, int carte) {
		series[ids]=new ListeCarte();
		series[ids].addEnd(carte);
		
	}
	

	
	//renvoie l'indice de la série à laquelle une carte donnée doit etre posée
	public int idSeriePose(int carte) {
		int ecart = 104;
		int idS=-1;
		for(int i = 0;i<NB_SERIES;++i) {
			int end = series[i].getEnd();
			if(end-carte<0) 
				if(ecart>carte-end) {
					idS = i;
					ecart = carte - end;
				}
		}
		if(idS==-1)throw new RuntimeException("Pas possible de poser");
		return idS;
	}
	//initialise unt nouvelle manche
	public void new_manche() {
		distribution_cartes();
	}
}
