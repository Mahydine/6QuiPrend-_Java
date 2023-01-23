package appli;

import java.util.ArrayList;
import java.util.Collections;

class Maillon{
	int carte;//valeur du maillon
	Maillon suivant = null;//r�f�rence au prochain maillon dans la chaine
	
	Maillon(int i) {
		carte = i;
	}
	
}

public class ListeCarte {
	private Maillon debut;//r�f�rence au 1er maillon de la chaine
	private Maillon fin;//r�f�rence au derniermaillon de la cha�ne
	private int size = 0;//nombre de maillon dans la chaine
	
	
	public ListeCarte(int i) {
		debut = new Maillon(i);
		fin = debut;
		size = 1;
	}
	public ListeCarte() {
	}
	//d�croche le 1er entier de lachaine en le renvoyant
	public int peakDebut() {
		int temp = debut.carte;
		if(size==1) {
			temp = debut.carte;
			debut = null;fin = null;
		}
		else debut = debut.suivant;
		--size;
		return temp;
	}
	//ajoute un entier � la fin de la chaine
	public void addEnd(int i) {
		if(size == 0) {
			debut = new Maillon(i);
			fin = debut;
		}
		else {
			fin.suivant = new Maillon(i);
			fin = fin.suivant;
		}
		++size;
	}
	//renvoie la valeur du dernier entier accroch� � la cha�ne
	public int getEnd() {
		return fin.carte;
	}
	//renvoie la liste des entiers de la chaine sous forme de chaine de char (espac�s d'un ' ')
	public String toString() {
		StringBuilder S = new StringBuilder(size*2);
		if(debut!=null) {
			Maillon c = debut;
			S.append(c.carte);
			while(c.suivant!=null) {
				c=c.suivant;
				S.append(" "+c.carte);
			}
		}
		return  S.toString();
		
	}
	//initialise une pioche de n cartes (cha�ne de n entiers diff�rents de 1 � n attach�s dans le d�sordres)
	public void initialise_pioche(int n) {
		this.clear();
		ArrayList<Integer>temp = new ArrayList<>();
		for(int i = 1;i<=n;++i) {
			temp.add(i);
		}
		Collections.shuffle(temp);
		for(int i : temp)this.addEnd(i);
		
	}
	//Reinitialise la chaine � 0 entier
	void clear() {
		this.debut = null;
		this.fin = null;
		size=0;
	}
	//renvoie si l'entier i est attach� dans la cha�ne
	public boolean contains(int i) {
		Maillon current = debut;
		while(current.carte!=i && current.suivant!=null)
			current = current.suivant;
		return current.carte==i;
	}
	//renvoie le 1er entier de la chaine
	public int getStart() {return debut.carte;}
	//d�tache le 1er entier i de la chaine
	public void delete(int i) {
		boolean ok =false;
		
		if(debut.carte==i) {
			debut=debut.suivant;
		}
		else {
			Maillon current = debut.suivant;
			Maillon prec = debut;
			while(current.suivant!=null&&!ok) {
				prec = current;
				current=current.suivant;
				if(current.carte==i) {
					prec.suivant = current.suivant;
					ok=true;
				}
			}
		}
		--size;
	}
	//renvoie tous les entiers de la chaines dans une ArrayList<Integer>
	public ArrayList<Integer> toArray() {
		ArrayList<Integer> s = new ArrayList<>();
		s.add(debut.carte);
		Maillon current = debut;
		while(current.suivant!=null) {
			s.add(current.suivant.carte);
			current = current.suivant;
		}
		return s;
	}
	
	public int size() {
		return size;
	}
	
}
