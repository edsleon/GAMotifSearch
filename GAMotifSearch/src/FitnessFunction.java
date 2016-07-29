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
 * This class represents fitness function to genetic algorithm
 * @author Eng. (C) Edson David Leon - MSc. Carlos Andrés Sierra
 */
public class FitnessFunction 
{
	//Clusters of data sets 
	private Vector<Cluster> clusters = new Vector<Cluster>();
	private int number_reads = 0;
	
	
	/**
	 * Constructor with data set path
	 * @param file
	 */
	public FitnessFunction(String file)
	{
		BufferedReader brBackground;
		String[] lineBackground;
		String line = "";
		
		//Read attributes
		String chromosome = ""; 
		int start = -1; 
		int end = -1;
		String sequence = "";
		String strand = ""; 
		int reads = -1;
		int mutations = -1;
		int[] mutationsT2C = null;
		String[] tempMutationsT2C = null;
		
		
		try 
		{
			brBackground = new BufferedReader(new FileReader(file));
			line = brBackground.readLine();
			
			while(line != null) //Read all lines of file
			{
				//Separate all fields of row
				lineBackground = line.split("\t");
				
				//Validate if read is valid
				if(lineBackground.length > 20)
				{
					//Read's Information
					chromosome = lineBackground[2]; 
					start = Integer.parseInt( lineBackground[3] );
					end = Integer.parseInt( lineBackground[4] );
					sequence = lineBackground[8];
					strand = lineBackground[7];
					reads = Integer.parseInt( lineBackground[6] );
					mutations = Integer.parseInt( lineBackground[10] );
					
					this.number_reads += reads;
					
					//Profile of T2C mutations
					tempMutationsT2C = lineBackground[20].split(" ");
					mutationsT2C = new int[tempMutationsT2C.length];
					
					//Get mutations per position
					for(int i = 1; i < tempMutationsT2C.length; i++)
					{
						mutationsT2C[i] = Integer.parseInt(tempMutationsT2C[i]);
					}
					
					//Add cluster to collection
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
		
		System.out.println("Size:" + this.clusters.size());
		System.out.println("Reads:" + this.number_reads + "\n");
	}
	
	
	/**
	 * This method calculate fitness taken into account the occurrences of individual inside clusters
	 * @param individual
	 * @return fitness
	 */
	@SuppressWarnings("unused")
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
				//Get occurrences inside sequence
				lastIndex = tempSequence.indexOf( individual, lastIndex );
				
				if(lastIndex == -1)
				{
					break; //No matches
				}
				else
				{
					//Add occurrences and move inside sequence
					occurrences++;
					lastIndex += (individual.length() - 1);
					
					break;
				}
			}
		}
		
		return occurrences / (double)this.clusters.size();
	}
	
	
	/**
	 * This method calculate fitness taken into account the occurrences of individual inside clusters but counting reads amount
	 * @param individual
	 * @return fitness
	 */
	public double calculateFitness_OccurrencesInReads (String individual)
	{
        int occurrences = 0; //Occurrences in read
        String tempSequence; //Read's sequence
		
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
					//Increase occurrences
					occurrences += this.clusters.get(i).getReads(); //TODO review with coverage
					lastIndex += (individual.length() - 1);
				}
			}
		}
		
		return (occurrences) * individual.length();
	}
	
	
	/**
	 * This method calculate fitness taken into account mutations by cluster
	 * @param individual
	 * @return fitness
	 */
	@SuppressWarnings("unused")
	public double calculateFitness_Mutations (String individual)
	{
        int mutations = 0; //Amount of mutations
		String tempSequence; //Read's sequence
		
		int lastIndex = 0;
		
		//Search in all clusters
		for(int i = 0; i < this.clusters.size(); i++)
		{
			tempSequence = this.clusters.get(i).getSequence();
			lastIndex = 0;
		
			
			//Move across the sequence
			for(int j = 0; j < (tempSequence.length() - individual.length() + 1); j++)
			{
				lastIndex = tempSequence.indexOf( individual, lastIndex ); // change for right function
				
				if(lastIndex == -1)
				{
					break; //No matches
				}
				else
				{
					//Increase mutations
					mutations += this.clusters.get(i).getMutations();
					break;
				}
			}
			
		}
		
		return mutations / (double) this.number_reads;
	}
	
	
	
	
	/**
	 * This method calculate fitness taken into account just mutations T2C
	 * @param individual
	 * @return fitness
	 */
	public double calculateFitness_MutationsT2C (String individual)
	{
        int mutations = 0; //Amount of mutations T2C in motif occurrences
		String tempSequence; //Read to compare motif
		
		int lastIndex = 0;
		
		//Search in all clusters
		for(int i = 0; i < this.clusters.size(); i++)
		{
			tempSequence = this.clusters.get(i).getSequence();
			lastIndex = 0;
			
			//Move across the sequence
			for(int j = 0; j < (tempSequence.length() - individual.length() + 1); j++)
			{
				lastIndex = tempSequence.indexOf( individual, lastIndex + 1); // change for right function
				
				if(lastIndex == -1)
				{
					break; //No matches
				}
				else
				{
					//Motif presence, then add amount of mutations in occurrence
					
					//for(int k = 0; k < this.clusters.get(i).getMutationT2C().length; k++)
					for(int k = 0; k < individual.length(); k++)
					{
						mutations += this.clusters.get(i).getMutationT2C()[k + lastIndex];
					}
				}
			}
			
		}
		
		return (mutations / (double)this.number_reads) / (double)individual.length();
	}
	
	
	/**
	 * This method calculate fitness in a convex combination of two-parameters: occurrences and mutations
	 * @param individual
	 * @param weigthOcurrences
	 * @param weigthMutations
	 * @return fitness
	 */
	@SuppressWarnings("unused")
	public double calculateFitness_OccurrencesandMutations (String individual, double weigthOccurrences, double weigthMutations)
	{
        int occurrences = 0; //Occurrences of motif inside data set reads
        int mutations = 0; //Amount of mutations where motif mapped
		int lengths = 0; //Sum of lengths of reads where motif mapped
		String tempSequence; //Current read to review motif presence
		
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
					//Increase amount of occurrences
					occurrences += this.clusters.get(i).getReads();
					lastIndex += (individual.length() - 1);
					find = true;
					
					break;
				}
			}
			
			//If motif has occurrence in read
			if(find)
			{
				//Increase amount of mutations	
				mutations += this.clusters.get(i).getMutations();
				lengths += tempSequence.length() + this.clusters.get(i).getReads();
			}
		}
		
		if(occurrences == 0)
			return 0.0;
		else
		{
			//Convex combination
			return (( occurrences / this.clusters.size() ) * individual.length() * weigthOccurrences) + ( ( mutations / individual.length() ) * weigthMutations);
		}
	}
	
	
	public static void main(String args[])
	{
		String file = args[0];
		String motif = args[1];
		
		FitnessFunction ff = new FitnessFunction(file);
		System.out.println("Mutations\t" + ff.calculateFitness_Mutations(motif));
		System.out.println("Mutations T2C\t" + ff.calculateFitness_MutationsT2C(motif));
		System.out.println("OccurrencesMutations\t" + ff.calculateFitness_OccurrencesandMutations(motif, 0.5, 0.5));
		System.out.println("OccurrencesCluster\t" + ff.calculateFitness_OccurrencesInCluster(motif));
		System.out.println("OccurrencesReads\t" + ff.calculateFitness_OccurrencesInReads(motif));
		
	}
}