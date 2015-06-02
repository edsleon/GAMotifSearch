/*
# GAMotifSearch - Tool to find motifs in clusters's sets of an experimental data set of RBP obtained by CLIP-seq protocol.
#
# Created by Eng. Edson Leon Araujo and Msc. Carlos Andres Sierra on April 2015.
# Copyright (c) 2015. Eng. Edson Leon Araujo and Msc. Carlos Andres Sierra. Research Group LACSER. Bios-UAN division. Universidad Antonio Narino. All rights reserved.
#
# This file is part of GAMotifSearch.
#
# GAMotifSearch is free software: you can redistribute it and/or modify it under the terms of the 
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
	//Attributes
	private int populationSize; //TODO
	private int iterations; //TODO
	private FitnessFunction fitnessFunction = null; //TODO
	private Replacement replacement = null; //TODO
	private GeneticOperator geneticOperator = null; //TODO
	private Vector<Individual> population = null; //TODO
	private Random rd = null; //TODO
	
	BufferedWriter bf = null; //TODO
	
	
	/**
	 * Constructor of the class
	 * @param file
	 * @param populationSize
	 * @param iterations
	 */
	public Population(String file, int populationSize, int iterations)
	{
		//TODO
		bf = new BufferedWriter ( new OutputStreamWriter(System.out) );
		
		//TODO
		try 
		{
			bf.write("||---------- Welcome to GA-MotifDetection ----------|| \n\n Initializing algorithm...\n");
			bf.flush();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		//TODO
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
			
			this.initialPopulation(); //TODO
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		try 
		{
			bf.write("Start Genetic Algorithm...\n\n");
			bf.flush();
			
			this.generations(); //TODO
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
    
	
    /**
     * TODO
     */
    private void initialPopulation() 
    {
    	//TODO
    	String individual = "";
        double fitness;
        
    	//TODO
    	for(int i = 0; i < this.populationSize; i++) 
        {
	        //Sequence length: 4 - 35
	        int length = 4 + rd.nextInt(32); //TODO
	        int base; //TODO
	        individual = ""; //TODO
	        
	        //TODO
	        for(int j = 0; j < length; j++) 
	        {
	            base = rd.nextInt(4); //TODO
	            
	            switch(base) 
	            {
	                case 0:		individual += "A";	break;
	                case 1:		individual += "C";	break;
	                case 2:		individual += "G";	break;
	                case 3:		individual += "T";	break;
	                default:	individual += "N";	break; //TODO
	            }
	        }
	        
	        //TOO
	        fitness = fitnessFunction.calculateFitness_OccurrencesandMutations(individual, 0.5, 0.5);
	        
	        //TODO
	        this.population.add( new Individual(individual, fitness) ); 
	    }
    }
    
    
    /**
     * TODO
     */
    private void generations()
    {
    	//TODO
    	Individual father1 = null; //TODO
    	Individual father2 = null; //TODO
    	String[] sonsGenotipes = new String[2]; //TODO
    	Individual[] sons = new Individual[2]; //TODO
    	Individual[] newGeneration = new Individual[2]; //TODO
    	Vector<Individual> nextGeneration = new Vector<Individual>(); //TODO
    	double tempFitness; //TODO
    	
    	
    	//Iterations or generations
    	for(int i = 0; i < this.iterations; i++)
    	{
    		System.gc();
    		
    		try 
    		{
    			bf.write("Iteration " + (i + 1) + "\n"); //TODO
    			bf.flush();
    		} 
    		catch (IOException e) 
    		{
    			e.printStackTrace();
    		}
    		
    		//Repeat operation to build new generation
    		for(int j = 0; j < (populationSize/ 2); j++)
    		{
    			father1 = selectFather(); //TODO
    			father2 = selectFather(); //TODO
    			
    			//Apply genetic operator to obtain sons
    			if(rd.nextDouble() < 0.5) //TODO
    			{
    				//TODO
    				sonsGenotipes = this.geneticOperator.mutation(father1.getGenotipe(), father2.getGenotipe()); 
    			}
    			else
    			{
    				//TODO
    				sonsGenotipes = this.geneticOperator.crossover(father1.getGenotipe(), father2.getGenotipe());
    			}
    			
    			//TODO
    			tempFitness = fitnessFunction.calculateFitness_OccurrencesandMutations( sonsGenotipes[0], 0.5, 0.5 );
    			sons[0] =  new Individual(sonsGenotipes[0], tempFitness) ; 
    			
    			//TODO
    			tempFitness = fitnessFunction.calculateFitness_OccurrencesandMutations( sonsGenotipes[1], 0.5, 0.5 );
    			sons[1] =  new Individual(sonsGenotipes[1], tempFitness); 
    			
    			//TODO
    			newGeneration = this.replacement.steadyState(father1, father2, sons[0], sons[1]);
    			
    			//TODO
    			nextGeneration.add( newGeneration[0] );
    			nextGeneration.add( newGeneration[1] );
    		}
    		
    		this.population = nextGeneration;
    	}
    }
    
    
    /**
     * TODO
     * @return TODO
     */
    private Individual selectFather()
    {
    	Individual father = null;
    	
    	int candidate1, candidate2, candidate3, candidate4; //TODO
    	
    	//TODO
    	candidate1 = rd.nextInt(populationSize); 
    	candidate2 = rd.nextInt(populationSize);
    	candidate3 = rd.nextInt(populationSize);
    	candidate4 = rd.nextInt(populationSize);
    	
    	//TODO
    	father = tournament( this.population.get(candidate1), this.population.get(candidate2), 
    			this.population.get(candidate3), this.population.get(candidate4) );
    	
    	//TODO
    	return father;
    }
    
    
    /**
     * TODO
     * @param player1
     * @param player2
     * @param player3
     * @param player4
     * @return TODO
     */
    private Individual tournament(Individual player1, Individual player2, Individual player3, Individual player4)
    {
    	Individual winner = null; //TODO
    	
    	//TODO
    	winner = roulette( roulette(player1, player2), roulette(player3, player4) ); 
    	
    	return winner;
    }
    
    
    /**
	 * TODO
	 * @param player1
	 * @param player2
	 * @return TODO
	 */
	private Individual roulette(Individual player1, Individual player2)
	{
		//TODO
		double totalFitness = player1.getFitness() + player2.getFitness(); 
		
		//TODO
		double point = player1.getFitness() / totalFitness;
		
		//TODO
		return rd.nextDouble() < point ? player1 : player2;
	}
    
    
	/**
	 * TODO
	 * @return TODO
	 */
    public String getBest()
    {
    	int index = 0;
    	
    	//TODO
    	double fitness = this.population.get(0).getFitness();
    	
    	//TODO
    	for(int i = 1; i < this.populationSize; i++)
    	{
    		//TODO
    		if(this.population.get(i).getFitness() > fitness)
    		{
    			index = i; //TODO
    			fitness = this.population.get(i).getFitness(); //TODO
    		}
    	}
    	
    	return this.population.get(index).getGenotipe(); //TODO improve to best results presentation
    }
    
    
    /**
     * 
     */
    public void toPrint()
    {
    	String sentence = "\n\nPopulation\n";
    	
    	//TODO
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