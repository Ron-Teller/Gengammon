package io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import algorithm.Gene;
import algorithm.MoveTest;


public class DataSerializer {

	public List<Gene> loadPopulation(String file) {
		List<Gene> population = new ArrayList<Gene>();
		Gene gene;
		try {
		    FileInputStream in = new FileInputStream(file);
		    ObjectInputStream ois = new ObjectInputStream(in);
		    while (in.available() > 0) {
		    	gene = (Gene) ois.readObject();
		    	population.add(gene);
		    }
		    ois.close();
		  } catch (Exception e) {
		    System.out.println("Problem loading:" + e);
		    e.printStackTrace();
		  }			
	     return population;
	}
	
	public void savePopulation(List<Gene> population, String file) {
		try {
		      FileOutputStream out = new FileOutputStream(file);
		      ObjectOutputStream oos = new ObjectOutputStream(out);
		      for (Gene gene : population) {
			      oos.writeObject(gene);
			      oos.flush();  
		      }
		      oos.close();
		    } catch (Exception e) {
		    	e.printStackTrace();
		    }
	}
	
	public List<MoveTest> loadTests(String file, int maxTests)
	{
		List<MoveTest> tests = new ArrayList<MoveTest>();
		MoveTest test;
		int testCount = 0;
		try {
		    FileInputStream in = new FileInputStream(file);
		    ObjectInputStream ois = new ObjectInputStream(in);
		    while (in.available() > 0 && testCount < maxTests) {
		    	test = (MoveTest) ois.readObject();
		    	tests.add(test);
		    	testCount ++;
		    }
		    ois.close();
		  } catch (Exception e) {
			  e.printStackTrace();
		  }			
	     return tests;
	}
	
	public void saveTests(String file, List<MoveTest> tests)
	{
		try {
		      FileOutputStream out = new FileOutputStream(file);
		      ObjectOutputStream oos = new ObjectOutputStream(out);
		      for (MoveTest test : tests) {
			      oos.writeObject(test);
			      oos.flush();  
		      }
		      oos.close();
		    } catch (Exception e) {
		    	e.printStackTrace();
		    }
	}	
}
