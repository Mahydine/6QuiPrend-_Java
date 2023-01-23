package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import appli.Joueur;

public class TestJoueur {
	@Test
	public void test() {
		testMethods(testConstructor());
	}

	private void testMethods(Joueur J) {
		//test modification du nb_tetes_de_boeufs
		int prec = 0;
		int score = 8;
		J.score_manche(score);
		assertTrue(J.getScore()==prec+score);
		prec+=score;
		score = 21;
		J.score_manche(score);
		assertTrue(J.getScore()==prec+score);
	}

	private Joueur testConstructor() {
		String str = "Mr. Poitrenaud";
		int ent = 1;
		Joueur J = new Joueur(str,ent);
		assertTrue(J.getNom()==str&&J.getOrdreABtique()==ent&&J.getScore()==0);
		str = "Ziak";
		ent = 10;
		J = new Joueur(str,ent);
		assertTrue(J.getNom()==str&&J.getOrdreABtique()==ent&&J.getScore()==0);
		str = "Shia";
		ent = 28;
		J = new Joueur(str,ent);
		assertTrue(J.getNom()==str&&J.getOrdreABtique()==ent&&J.getScore()==0);
	
		return J;
	}
}
