package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

import appli.Joueur;
import appli.ListeCarte;
import appli.Partie;
public class TestPartie {
	private final File f = new File("src/test/config.txt");
	private String[] noms = new  String[10];
	private int[] ordres = new int[10]; 
	@Test
	public void test() {
		init();
		testConstructeur();
		testCheckEnd();
		testMinserie();
	}
	
	private void testMinserie() {
		for(int i = 0;i<10;++i) {
			Partie p = new Partie(f);
			ArrayList<Integer>temp=new ArrayList<>();
			for(int j = 0;j<4;++j) {
				temp.add(TestListeCarte.rand(-100,100));
				p.getSeries()[j].addEnd(temp.get(j));
			}
			temp.sort(null);
			assertTrue(temp.get(0)==p.min_serie());
		}
	}

	private void testCheckEnd() {
		Partie p = new Partie(f);
		boolean ok = true;
		for(int idP = 0; idP <= p.nb_joueurs()&&ok;++idP) {
			if(idP!=p.nb_joueurs()) {
				p.getJoueurs().get(idP).score_manche(60);
				if(p.checkEnd())System.out.println(idP);
				ok = ok &&( p.checkEnd()==true);
				p.getJoueurs().get(idP).score_manche(-60);
			}
			else {
				ok = ok &&( p.checkEnd()==false);
			}
		}
		assertTrue(ok);
		
	}

	private void init() {
		for(int i = 0;i<10;++i)noms[i]=new String();
		noms[0]="Romain";
		noms[1]="Mahydine";
		noms[2]="Alexis";
		noms[3]="Louis";
		noms[4]="Paul";
		noms[5]="Xavier";
		noms[6]="Dominique";
		noms[7]="Sofia";
		noms[8]="Flore";
		noms[9]="Sugeetha";
		ordres[2]=0;
		ordres[6]=1;
		ordres[8]=2;
		ordres[3]=3;
		ordres[1]=4;
		ordres[4]=5;
		ordres[0]=6;
		ordres[7]=7;
		ordres[9]=8;
		ordres[5]=9;
	}

	private void testConstructeur() {
		Partie p = new Partie(f);
		assertTrue(p.getJoueurs().size()==10);
		boolean ok = true;
		int i = 0;
		for(Joueur J : p.getJoueurs()) {
			ok=J.getNom().equals(noms[i])&&ok;
			++i;
		}
		i=0;
		assertTrue(ok);
		for(Joueur J : p.getJoueurs()) {
			ok=J.getOrdreABtique()==ordres[i]&&ok;
			++i;
		}
		assertTrue(ok);
		
		
		for(Joueur J : p.getJoueurs())
			ok = ok&&J.getCartes().size()==10;
		for(ListeCarte s : p.getSeries())
			ok = ok&&s.size()==1;
		assertTrue(ok);
		

		boolean[] bools = new boolean[104];
		for(int i1 = 0;i1<bools.length;++i1)
			bools[i1]=false;
		
		for(Joueur J : p.getJoueurs())
			for(int n : J.getCartes().toArray()) {
				if(bools[n-1]==false)bools[n-1]=true;
				else ok = false;
			}
				
		for(ListeCarte s : p.getSeries())
			for(int n : s.toArray()) {
				if(bools[n-1]==false)bools[n-1]=true;
				else ok = false;
		}
		for(boolean b : bools)ok=ok&&b;
		assertTrue(ok);
		
	}
	
}
