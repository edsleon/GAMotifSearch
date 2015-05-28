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
					
					mutationsT2C = new int[1];
					
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
	 * This method is used to obtain fitness of an individual compared with data set information
	 * @param individual
	 * @return fitness
	 */
	public double calculateFitness (String individual)
	{
        int ocurrences = 0;
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
					ocurrences++;
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
		if(ocurrences == 0)
			return 0.0;
		else
			return (( ocurrences / this.clusters.size() ) * 0.5) + ( ( mutations / lengths ) * 0.5) * 100;
	}
}