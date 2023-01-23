package appli;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import static util.Console.clearScreen;
import static util.Console.pause;

public class Application {
	static File f = new File("src/config.txt");//fichier contenant les noms des joueurs
	private static	Partie p = new Partie(f);//Partie de 6 qui prend jouee
	private static int nb_tours = 0;//nombre de tours joués depuis le début de la partie
	private static ArrayList<Integer> POIDS = new ArrayList<>();//liste des nombres de têtes de boeufs des cartes
	
	public static void main(String[] args) {
		affichage_debut();//affichage du message d'entrée
		initialise_poids();	//initialisation de la liste des poids (le poids à l'indice i-1 est le nombre de tete de boeufs de la carte i)
		
		//tant que la partie n'estpas terminée, je continue à jouer une manche
		while(!p.checkEnd()) {
			ArrayList<Integer> jeu = new ArrayList<>();//liste des carte mises en jeu
			for(Joueur j : p.getJoueurs()) {
				System.out.println("A "+j.getNom()+" de jouer.");//annonce du joueur dont c'est le tour
				pause();
				jeu.add(selection_carte(j));//ajout de la carte sélectionnée dans les cartes en jeu
				clearScreen();
			}
			placement_cartes(jeu);//mise en jeu des cartes
			++nb_tours;	//fin du tour
			if(nb_tours%10==0) //si les joueurs n'ont plus de cartes en main donc
				p.new_manche();//j'initialise une nouvelle manche
		}
		affichage_fin();//affichage des scores finaux
	}

	private static void affichage_fin() {
		System.out.println("** Score final");
		
		for(int i = 0;i<p.nb_joueurs();++i) {//boucle permettant d'obtenir l'ordre d'affichage des joueurs
			int idJ = i;
			int score = p.getJoueurs().get(i).getScore();
			for(int j = i+1;j<p.nb_joueurs();++j) {
				Joueur J = p.getJoueurs().get(j);
				if(J.getScore()<score) {
					idJ=j;
					score = J.getScore();
				}
				if(J.getScore()==score)
					if(J.getOrdreABtique()<p.getJoueurs().get(idJ).getOrdreABtique()) {
						idJ=j;
						score = J.getScore();
					}	
			}
			Joueur J = new Joueur(p.getJoueurs().get(idJ).getNom(),p.getJoueurs().get(idJ).getOrdreABtique());
			J.score_manche(p.getJoueurs().get(idJ).getScore());
			p.getJoueurs().set(idJ, p.getJoueurs().get(i));
			p.getJoueurs().set(i, J);
		}
		for(Joueur J : p.getJoueurs()) {//boucle affichant dans l'ordre les joueurs et leur nb de tetes de boeufs
			System.out.print(J.getNom()+" a ramassé "+J.getScore()+" tête");
			if(!(J.getScore()==0||J.getScore()==1))System.out.print("s");
			System.out.println(" de boeufs");
		}
		
	}

	private static void affichage_debut() {
		System.out.print("Les "+p.nb_joueurs()+" joueurs sont");
		int i = 0;
		for(Joueur j :p.getJoueurs()) {
			if(i==p.nb_joueurs()-2)
				System.out.print(" "+j.getNom()+" et");
			else if(i==p.nb_joueurs()-1)
				System.out.print(" "+j.getNom()+".");
			else
				System.out.print(" "+j.getNom()+",");
			++i;

		}
		System.out.println(" Merci de jouer à 6 qui prend !");

	}

	private static void initialise_poids() {
		for(int i = 1; i <=104;++i) {
			int j = 0;
			if(i%10==0)j=3;//regle : si dernier chiffre egal a 0 +2tdb(tetes de boeufs)
			else if(i%10==5)j=2;//regle : une carte dont le dernier chiffre est 5 +2tdb
			if(i%10==i/10)j+=5;//regles : une czrte dont les chiffres sont identiques +5tdb
			if(j==0)++j;//les autres valent 1tdb
			POIDS.add(j);
		}

	}

	private static void placement_cartes(ArrayList<Integer> jeu) {
		ArrayList<Integer> temp=new ArrayList<>();
		ArrayList<Integer>scores_temp = new ArrayList<>();
		for(int i : jeu) {
			temp.add(i);
			scores_temp.add(0);
		}
		temp.sort(null);

		int carte = temp.get(0);
		int idJ = jeu.indexOf(carte);
		int idS;
		if(carte<p.min_serie()) {//si jamais la carte la plus peite du jeu ne peut etre posée
			affichage_jeu(jeu, temp);
			System.out.println(" vont être posées.");
			Joueur J = p.getJoueurs().get(idJ);
			System.out.println("Pour poser la carte "+carte+", "+J.getNom()+" doit choisir la série qu'il va ramasser.");
			idS = selection_serie(J)-1;//je demande au joeur qui l'a posé de sélectionner la série dont il doit ramasser les cartes
			scores_temp.set(idJ, poidserie(p.getSeries()[idS]));//j'enregistre le nombre de têtes de boeufs que le joueur a ramasse
			p.setSerie(idS,carte);//je mets la carte du joueur en tête de série
		}

		for(int i = 0;i<p.nb_joueurs();++i) {
			carte = temp.get(i);
			idJ = jeu.indexOf(carte);
			if (scores_temp.get(idJ)==0) {//si jamais la 1ère carte est déjà posée (le joueur a donc un score non nul)
				int poidS =0;//j'initialise le nb de tdb à 0
				idS=p.idSeriePose(carte);//je récupère l'id de la série où la carte doit être posée
				if (p.getSeries()[idS].size()==5) {//si il y a déjà 5 carte 
					poidS = poidserie(p.getSeries()[idS]);//le joueur recupere le nb de tdb de la série
					p.setSerie(idS, carte);//puis j'initialise ma série avec une seule carte (celle du joueur)
				}
				else {//sinon
					p.getSeries()[idS].addEnd(carte);//je rajoute à la fin de la série la carte
				}
				scores_temp.set(idJ, poidS);//j'enregistre à l'indice du joueur son score
			}
		}

		affichage_jeu(jeu, temp);
		System.out.println(" ont été posées.");


		for(int i = 0; i < p.nb_series();++i) {//affichage des series
			System.out.print("- série n° "+(i+1)+" : ");
			affichage_listecarte(p.getSeries()[i].toArray());
		}

		affichage_scores(scores_temp);


	}


	private static void affichage_scores(ArrayList<Integer> scores) {
		int[] temp = new int[scores.size()];
		int m =0;
		for(int i : scores) {
			temp[m]=i;
			++m;
		}
		Joueur[] J = new Joueur[p.nb_joueurs()];
		for(int i = 0;i<temp.length;++i) {//j'obtiens grâce cette boucle l'ordre dans le quel affiché les joueurs
			int idJ = -1;
			int score = 30;
			for(int j = 0;j<scores.size();++j) {
				if(scores.get(j)!=null) {
					if(scores.get(j)<score) {
						idJ=j;
						score = scores.get(j);
					}
					else if(scores.get(j)==score) {
						ArrayList<Joueur> Js = p.getJoueurs();
						if(Js.get(idJ).getOrdreABtique()>Js.get(j).getOrdreABtique()) {
							idJ=j;
							score = scores.get(j);
						}
					}
				}
			}
			scores.set(idJ, null);
			J[i]=p.getJoueurs().get(idJ);
		}
		int ct = 0;
		for(Joueur j : J) {
			int idJ = p.getJoueurs().indexOf(j);
			if(temp[idJ]!=0) {//si le joueur a ramassé des têtes j'affiche le nombre de tête qu'il a ramasse
				++ct;
				System.out.print(j.getNom()+" a ramassé "+temp[idJ]+" tête");
				if(temp[idJ]>1)System.out.print("s");
				System.out.println(" de boeufs");
				j.score_manche(temp[idJ]);
			}
		}
		if(ct==0)System.out.println("Aucun joueur ne ramasse de tête de boeufs.");
	}
	private static int poidserie(ListeCarte s) {
		ArrayList<Integer> serie = s.toArray();
		int S=0;
		for(int i : serie)S+=POIDS.get(i-1);
		return S;
	}
	private static void affichage_listecarte(ArrayList<Integer> tab) {
		System.out.print(tab.get(0));
		if(POIDS.get(tab.get(0)-1)>1)
			System.out.print(" ("+POIDS.get(tab.get(0)-1)+")");
		for(int i = 1;i<tab.size();++i) {
			System.out.print(", "+tab.get(i));
			if(POIDS.get(tab.get(i)-1)>1)
				System.out.print(" ("+POIDS.get(tab.get(i)-1)+")");

		}
		System.out.println();
	}


	private static void affichage_jeu(ArrayList<Integer> jeu, ArrayList<Integer> temp) {
		System.out.print("Les cartes");
		Joueur J;
		int carte;
		for(int i = 0;i<jeu.size()-2;++i) {
			carte = temp.get(i);
			J = p.getJoueurs().get(jeu.indexOf(carte));
			System.out.print(" "+carte+" ("+J.getNom()+"),");
		}
		carte = temp.get(temp.size()-2);
		J=p.getJoueurs().get(jeu.indexOf(carte));
		System.out.print(" "+carte+" ("+J.getNom()+") et");
		carte = temp.get(temp.size()-1);
		J=p.getJoueurs().get(jeu.indexOf(carte));
		System.out.print(" "+carte+" ("+J.getNom()+")");
	}

	private static int selection_serie(Joueur J) {	
		for(int i = 0; i < p.nb_series();++i) {
			System.out.print("- série n° "+(i+1)+" : ");
			affichage_listecarte(p.getSeries()[i].toArray());
		}
		int j = 0;
		boolean ok = false;
		System.out.print("Saisissez votre choix : ");
		while(!ok) {//boucle pour s'assurer d'une saisie valide
			ok = true;
			@SuppressWarnings("resource")
			Scanner sc = new Scanner(System.in);
			try {
				j = sc.nextInt();
			}catch(Exception e){ok = false;}//si jamais saisie de charactères alors saisie non valide

			ok = (ok&&j>0&&j<=4);//si ne correspond pas au numéro du série alors saisie non valide
			if(!ok)System.out.print("Ceci n'est pas une série valide, saisissez votre choix : ");
		}
		return j;
	}

	private static int selection_carte(Joueur J) {	
		for(int i = 0; i < p.nb_series();++i) {
			System.out.print("- série n° "+(i+1)+" : ");
			affichage_listecarte(p.getSeries()[i].toArray());
		}
		System.out.print("- Vos cartes : ");
		affichage_listecarte(J.getCartes().toArray());

		boolean ok = false;
		int j = 0;
		
		System.out.print("Saisissez votre choix : ");
		
		
		//Boucle pour s'assurer d'une entrée valide
		while(!ok) {
			ok = true;
			@SuppressWarnings("resource")
			Scanner sc = new Scanner(System.in);
			try{
				j = sc .nextInt();
			}
			catch (Exception e) {ok = false;sc = new Scanner(System.in);}//si jamais saisie de charactères alors saisie non valide

			ok = ok && J.getCartes().contains(j);//si le joueur n'a pasla carte en main alorssaisie non valide
			if(!ok)System.out.print("Vous n'avez pas cette carte, saisissez votre choix : ");
		}
		J.getCartes().delete(j);
		return j;

	}
}
