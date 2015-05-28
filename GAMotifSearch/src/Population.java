
/*
# LengthMotifSearch - Tool to find motifs in clusters's sets of an experimental data set of RBP obtained by CLIP-seq protocol.
#
# Created by Edson Leon Araujo and Msc. Carlos Andres Sierra on April 2015.
# Copyright (c) 2015 Edson Leon Araujo and Msc. Carlos Andres Sierra. Research Group LACSER. Bios-UAN division. Universidad Antonio Narino. All rights reserved.
#
# This file is part of LengthMotifSearch.
#
# CLengthMotifSearch is free software: you can redistribute it and/or modify it under the terms of the 
# GNU General Public License as published by the Free Software Foundation, version 2.
*/

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Random;
import java.util.Vector;

/**
 *
 * @author Eng. (C) Edson David Leon - MSc. Carlos Andrés Sierra
 */
public class Population 
{
	private int populationSize;
	private int iterations;
	private FitnessFunction fitnessFunction = null;
	private Replacement replacement = null;
	private GeneticOperator geneticOperator = null;
	private Vector<Individual> population = null;
	private Random rd = null;
	
	BufferedWriter bf = null;
	
	/**
	 * Constructor of the class
	 * @param file
	 * @param populationSize
	 * @param iterations
	 */
	public Population(String file, int populationSize, int iterations)
	{
		bf = new BufferedWriter ( new OutputStreamWriter(System.out) );
		
		try 
		{
			bf.write("||---------- Welcome to GA-MotifDetection ----------|| \n\n Initializing algorithm...\n");
			bf.flush();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		this.populationSize = populationSize;
		this.iterations = iterations;
		
		this.fitnessFunction = new FitnessFunction(file);
		this.geneticOperator = new GeneticOperator();
		this.replacement = new Replacement();
		this.population = new Vector<Individual>();
		this.rd = new Random();
	
		
		try 
		{
			bf.write("Building initial population...\n");
			bf.flush();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		this.initialPopulation();
		
		try 
		{
			bf.write("Start Genetic Algorithm...\n\n");
			bf.flush();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		this.generations();
	}
    
	
    /**
     * 
     */
    private void initialPopulation() 
    {
    	String individual = "";
        double fitness;
        
    	
    	for(int i = 0; i < this.populationSize; i++) 
        {
	        //Sequence length: 4 - 35
	        int length = 4 + rd.nextInt(32);
	        int base;
	        individual = "";
	        
	        for(int j = 0; j < length; j++) 
	        {
	            base = rd.nextInt(4); 
	            
	            switch(base) 
	            {
	                case 0:		individual += "A";	break;
	                case 1:		individual += "C";	break;
	                case 2:		individual += "G";	break;
	                case 3:		individual += "T";	break;
	                default:	individual += "N";	break;
	            }
	        }
	        
	        fitness = fitnessFunction.calculateFitness( individual );
	        this.population.add( new Individual(individual, fitness) ); 
	    }
    }
    
    
    /**
     * 
     */
    private void generations()
    {
    	Individual father1 = null;
    	Individual father2 = null;
    	String[] sonsGenotipes = new String[2];
    	Individual[] sons = new Individual[2];
    	Individual[] newGeneration = new Individual[2];
    	Vector<Individual> nextGeneration = new Vector<Individual>();
    	double tempFitness;
    	
    	
    	
    	//Iterations
    	for(int i = 0; i < this.iterations; i++)
    	{
    		//this.toPrint();
    		//System.gc();
    		
    		try 
    		{
    			bf.write("Iteration " + (i + 1) + "\n");
    			bf.flush();
    		} 
    		catch (IOException e) 
    		{
    			e.printStackTrace();
    		}
    		
    		//Repeat operation to build new generation
    		for(int j = 0; j < (populationSize/ 2); j++)
    		{
    			father1 = selectFather();
    			father2 = selectFather();
    			
    			//Get sons
    			if(rd.nextDouble() < 0.5)
    			{
    				sonsGenotipes = this.geneticOperator.mutation(father1.getGenotipe(), father2.getGenotipe());
    			}
    			else
    			{
    				sonsGenotipes = this.geneticOperator.crossover(father1.getGenotipe(), father2.getGenotipe());
    			}
    			
    			
    			tempFitness = fitnessFunction.calculateFitness( sonsGenotipes[0] );
    			sons[0] =  new Individual(sonsGenotipes[0], tempFitness) ; 
    			
    			tempFitness = fitnessFunction.calculateFitness( sonsGenotipes[1] );
    			sons[1] =  new Individual(sonsGenotipes[1], tempFitness); 
    			
    			newGeneration = this.replacement.steadyState(father1, father2, sons[0], sons[1]);
    			nextGeneration.add( newGeneration[0] );
    			nextGeneration.add( newGeneration[1] );
    		}
    		
    		this.population = nextGeneration;
    	}
    }
    
    
    /**
     * 
     * @return
     */
    private Individual selectFather()
    {
    	Individual father = null;
    	int candidate1, candidate2, candidate3, candidate4;
    	
    	candidate1 = rd.nextInt(populationSize);
    	candidate2 = rd.nextInt(populationSize);
    	candidate3 = rd.nextInt(populationSize);
    	candidate4 = rd.nextInt(populationSize);
    	
    	father = tournament( this.population.get(candidate1), this.population.get(candidate2), 
    			this.population.get(candidate3), this.population.get(candidate4) );
    	
    	return father;
    }
    
    
    /**
     * 
     * @param player1
     * @param player2
     * @param player3
     * @param player4
     * @return
     */
    private Individual tournament(Individual player1, Individual player2, Individual player3, Individual player4)
    {
    	Individual winner = null;
    	
    	winner = roulette( roulette(player1, player2), roulette(player3, player4) );
    	
    	return winner;
    }
    
    
    /**
	 * 
	 * @param player1
	 * @param player2
	 * @return
	 */
	private Individual roulette(Individual player1, Individual player2)
	{
		double totalFitness = player1.getFitness() + player2.getFitness();
		double point = player1.getFitness() / totalFitness;
		
		return rd.nextDouble() < point ? player1 : player2;
	}
    
    
	/**
	 * 
	 * @return
	 */
    public String getBest()
    {
    	int index = 0;
    	double fitness = this.population.get(0).getFitness();
    	
    	for(int i = 1; i < this.populationSize; i++)
    	{
    		if(this.population.get(i).getFitness() > fitness)
    		{
    			index = i;
    			fitness = this.population.get(i).getFitness();
    		}
    	}
    	
    	return this.population.get(index).getGenotipe();
    }
    
    
    public void toPrint()
    {
    	String sentence = "\n\nPopulation\n";
    	
    	for(int i = 0; i < this.populationSize; i++)
    	{
    		sentence += this.population.get(i).getGenotipe() + "\t" + this.population.get(i).getFitness() + "\n";
    	}
    	
    	try 
		{
			bf.write(sentence + "\n");
			bf.flush();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
    }
}