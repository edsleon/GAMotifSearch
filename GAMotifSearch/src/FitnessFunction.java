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


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import Structures.Cluster;


/**
 *
 * @author Eng. (C) Edson David Leon - MSc. Carlos Andrés Sierra
 */
public class FitnessFunction 
{
	//Clusters of data sets 
	Vector<Cluster> clusters = new Vector<Cluster>();
	
	
	/**
	 * Constructor with data set path
	 * @param file
	 */
	public FitnessFunction(String file)
	{
		BufferedReader brBackground;
		String[] lineBackground;
		String line = "";
		
		String chromosome; 
		int start; 
		int end;
		String sequence;
		String strand; 
		int reads;
		int mutations;
		int[] mutationsT2C;
		String[] tempMutationsT2C;
		
		
		try 
		{
			brBackground = new BufferedReader(new FileReader(file));
			line = brBackground.readLine();
			
			while(line != null)
			{
				lineBackground = line.split("\t");
				
				if(lineBackground.length > 9)
				{
					chromosome = lineBackground[2]; 
					start = Integer.parseInt( lineBackground[3] );
					end = Integer.parseInt( lineBackground[4] );
					sequence = lineBackground[8];
					strand = lineBackground[7];
					reads = Integer.parseInt( lineBackground[6] );
					mutations = Integer.parseInt( lineBackground[10] );
					
					//Profile of T2C mutations
					tempMutationsT2C = lineBackground[20].split(" ");
					mutationsT2C = new int[tempMutationsT2C.length];
					
					for(int i = 0; i < tempMutationsT2C.length; i++)
					{
						mutationsT2C[i] = Integer.parseInt(tempMutationsT2C[i]);
					}
					
					this.clusters.add( new Cluster( chromosome, start, end, sequence, strand, reads, mutations, mutationsT2C ) );
				}	
					
				line = brBackground.readLine();
			}
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	
	/**
	 * This method calculate fitness taken into account the occurrences of individual inside clusters
	 * @param individual
	 * @return fitness
	 */
	public double calculateFitness_OccurrencesInCluster (String individual)
	{
        int occurrences = 0;
        String tempSequence;
		
		int lastIndex = 0;
		
		//Search in all clusters
		for(int i = 0; i < this.clusters.size(); i++)
		{
			tempSequence = this.clusters.get(i).getSequence();
			lastIndex = 0;
			
			//Move across the sequence
			for(int j = 0; j < (tempSequence.length() - individual.length() + 1); j++)
			{
				lastIndex = tempSequence.indexOf( individual, lastIndex );
				
				if(lastIndex == -1)
				{
					break; //No matches
				}
				else
				{
					occurrences++;
					lastIndex += (individual.length() - 1);
				}
			}
		}
		
		return occurrences * 1.0;
	}
	
	
	/**
	 * This method calculate fitness taken into account the occurrences of individual inside clusters but counting reads amount
	 * @param individual
	 * @return fitness
	 */
	public double calculateFitness_OccurrencesInReads (String individual)
	{
        int occurrences = 0;
        String tempSequence;
		
		int lastIndex = 0;
		
		//Search in all clusters
		for(int i = 0; i < this.clusters.size(); i++)
		{
			tempSequence = this.clusters.get(i).getSequence();
			lastIndex = 0;
			
			//Move across the sequence
			for(int j = 0; j < (tempSequence.length() - individual.length() + 1); j++)
			{
				lastIndex = tempSequence.indexOf( individual, lastIndex );
				
				if(lastIndex == -1)
				{
					break; //No matches
				}
				else
				{
					occurrences += this.clusters.get(i).getReads();
					lastIndex += (individual.length() - 1);
				}
			}
		}
		
		return occurrences * 1.0;
	}
	
	
	/**
	 * This method calculate fitness taken into account mutations by cluster
	 * @param individual
	 * @return fitness
	 */
	public double calculateFitness_Mutations (String individual)
	{
        int mutations = 0;
		String tempSequence;
		
		int lastIndex = 0;
		
		//Search in all clusters
		for(int i = 0; i < this.clusters.size(); i++)
		{
			tempSequence = this.clusters.get(i).getSequence();
			lastIndex = 0;
			
			//Move across the sequence
			for(int j = 0; j < (tempSequence.length() - individual.length() + 1); j++)
			{
				lastIndex = tempSequence.indexOf( individual, lastIndex ); //TODO change for right function
				
				if(lastIndex == -1)
				{
					break; //No matches
				}
				else
				{
					mutations += this.clusters.get(i).getMutations();
					break;
				}
			}
			
		}
		
		//Convex combination
		return mutations * 1.0;
	}
	
	
	
	//TODO
	/**
	 * This method calculate fitness taken into account mutations by cluster
	 * @param individual
	 * @return fitness
	 */
	public double calculateFitness_MutationsT2C (String individual)
	{
        int mutations = 0;
		String tempSequence;
		
		int lastIndex = 0;
		
		//Search in all clusters
		for(int i = 0; i < this.clusters.size(); i++)
		{
			tempSequence = this.clusters.get(i).getSequence();
			lastIndex = 0;
			
			//Move across the sequence
			for(int j = 0; j < (tempSequence.length() - individual.length() + 1); j++)
			{
				lastIndex = tempSequence.indexOf( individual, lastIndex ); //TODO change for right function
				
				if(lastIndex == -1)
				{
					break; //No matches
				}
				else
				{
					mutations += this.clusters.get(i).getMutations();
					break;
				}
			}
			
		}
		
		//Convex combination
		return mutations * 1.0;
	}
	
	
	/**
	 * This method calculate fitness in a convex combination of two-parameters: occurrences and mutations
	 * @param individual
	 * @param weigthOcurrences
	 * @param weigthMutations
	 * @return fitness
	 */
	public double calculateFitness_OccurrencesandMutations (String individual, double weigthOccurrences, double weigthMutations)
	{
        int occurrences = 0;
        int mutations = 0;
		int lengths = 0;
		String tempSequence;
		
		int lastIndex = 0;
		boolean find;
		
		//Search in all clusters
		for(int i = 0; i < this.clusters.size(); i++)
		{
			tempSequence = this.clusters.get(i).getSequence();
			lastIndex = 0;
			find = false;
			
			//Move across the sequence
			for(int j = 0; j < (tempSequence.length() - individual.length() + 1); j++)
			{
				lastIndex = tempSequence.indexOf( individual, lastIndex );
				
				if(lastIndex == -1)
				{
					break; //No matches
				}
				else
				{
					occurrences++;
					lastIndex += (individual.length() - 1);
					find = true;
				}
			}
			
			if(find)
			{
				mutations += this.clusters.get(i).getMutations();
				lengths += tempSequence.length();
			}
		}
		
		//Convex combination
		if(occurrences == 0)
			return 0.0;
		else
			return (( occurrences / this.clusters.size() ) * 0.5) + ( ( mutations / lengths ) * 0.5) * 100;
	}
}