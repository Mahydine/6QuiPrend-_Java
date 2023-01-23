package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Random;

import org.junit.jupiter.api.Test;

import appli.ListeCarte;

public class TestListeCarte {
	@Test
	public void test() {
		
		testAdd();
		testPeak();
		testToArray();
		testPioche();
		testContains();
		testDelete();
		
		
	}

	private void testDelete() {
		ListeCarte lc = new ListeCarte();
		int size = rand(100,200);
		lc.initialise_pioche(size);
		ArrayList<Integer> temp = lc.toArray();
		boolean ok = true;
		int iter = rand(100,size);
		for(int i = 0;i<iter;++i) {
			int id = rand(0,temp.size()-1);
			lc.delete(temp.get(id));
			temp.remove(id);
			ok=ok&&(lc.toArray().equals(temp));
		}
		assertTrue(true);
	}

	private void testContains() {
		ArrayList<Integer> temp=new ArrayList<>();
		ListeCarte lc= constructeur(temp);
		int iter = rand(200,300);
		boolean ok = true;
		for(int j = 0;j<iter&&ok;++j) {
			int i = rand(-100,100);
			ok=ok&&(lc.contains(i)==temp.contains(i));
		}
		assertTrue(ok);
	}

	private void testPioche() {
		int iter = rand(10,15);
		for(int k = 0;k<iter;++k) {
			int n = rand(104,300);
			ListeCarte lc = new ListeCarte();
			lc.initialise_pioche(n);
			boolean unique = true;
			boolean[] bools = new boolean[n];
			for(int i = 0;i<n&&unique;++i) {
				int m = lc.toArray().get(i)-1;
				if(bools[m]==false)bools[m]=true;
				else unique = false;
			}
			assertTrue(unique);
			

			boolean melange = false;
			int prec = lc.toArray().get(0);
			for(int i = 1;i<n&&!melange ;++i) {
				if(prec!=lc.toArray().get(i))melange=true;
				else prec = lc.toArray().get(i);
			}
			assertTrue(melange);
		}
		
	}
	private void testPeak() {
		ArrayList<Integer> temp = new ArrayList<>();
		ListeCarte lc = new ListeCarte();
		int taille_prec = temp.size();
		int n = rand(100,200);
		for(int i = 0;i<n;++i) {
			int choix = 0;
			if(lc.size()!=0)
				choix = rand(1,3);
			else 
				choix = 2;
			
			if(choix!=1) {
				int add = rand(-100,100);
				lc.addEnd(add);
				temp.add(add);
			}
			else {
				int peak = lc.peakDebut();
				assertTrue(peak==temp.get(0));
				temp.remove(0);
				assertTrue(taille_prec-1==lc.size());
				System.out.println(lc);	
			}
			taille_prec=lc.size();
		}
		
	}
	private void testAdd(){
		for(int nb = 0;nb<10;++nb) {
			ListeCarte lc = new ListeCarte();
			ArrayList<Integer> temp = new ArrayList<>();
			int i = rand(10,30);
			for(int j = 0;j<i;++j) {
				int n = rand(-100,100);
				lc.addEnd(n);
				temp.add(n);
			}
			StringBuilder Sol = new StringBuilder();
			int c = 0;
			for(int m : temp) {
				if(!(c==0)) 
					Sol.append(" ");
				++c;
				Sol.append(m);
			}
			System.out.println(Sol);
			System.out.println(lc);
			assertTrue(lc.toString().equals(Sol.toString()));
			assertTrue(lc.size()==i);
		}
	}
	private void testToArray() {
		int iter = rand(100,200);
		boolean ok= true;
		for(int i = 0;i<iter;++i) {
			ArrayList<Integer>temp = new ArrayList<>();
			ListeCarte lc = constructeur(temp);
			ok=ok&&lc.toArray().equals(temp);
		}
		assertTrue(ok);
	}
	
	
	
	private ListeCarte constructeur(ArrayList<Integer> temp) {
		ListeCarte lc = new ListeCarte();
		int i = rand(10,30);
		for(int j = 0;j<i;++j) {
			int n = rand(-100,100);
			lc.addEnd(n);
			temp.add(n);
		}
		return lc;
	}
	static int rand(int min, int max) {
		if (min >= max) {
			throw new IllegalArgumentException("max doit être plus grand que min");
		}

		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}

}
