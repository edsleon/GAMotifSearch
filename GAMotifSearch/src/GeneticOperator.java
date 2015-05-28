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



import java.util.Random;

/**
 *
 * @author Eng. (C) Edson David Leon - MSc. Carlos Andrés Sierra
 */

public class GeneticOperator {

	Random rd = new Random();
	
	/**
	 * Constructor of the class. 
	 */
	public GeneticOperator() {}
	
	
	/**
	 * This method implements traditional Mutation strategy.
	 * @param father1
	 * @param father2
	 * @return sons in an array
	 */
	public String[] mutation(String father1, String father2)
	{
		//Array of sons's chromosomes
		String[] sonsChromosomes = new String[2];
		
		int change; //Save point in chromosome to change base
		
		//Point to change base from first father
		change = rd.nextInt( father1.length() );
		//Obtain first	 son changing base (randomly) on defined point
		sonsChromosomes[0] = father1.substring(0,change) + newBase( father1.charAt(change) ) + father1.substring(change + 1);
		
		//Point to change base from second father
		change = rd.nextInt( father2.length() );
		//Obtain second son changing base (randomly) on defined point
		sonsChromosomes[1] = father2.substring(0,change) + newBase( father2.charAt(change) ) + father2.substring(change + 1);
		
		return sonsChromosomes;
	}

	
	/**
	 * This method is used to obtain a new nucleotide base different to an original base.
	 * @param oldBase
	 * @return newBase
	 */
	private String newBase(char oldBase)
	{
		char base = ' ';
		
		//If base is A, must to be change for another base (C, G, T)
		if(base == 'A')
		{
			base = randomBase('C', 'G', 'T');
		}
		else
		{
			if(base == 'C') //Similar to previous case
			{
				base = randomBase('A', 'G', 'T');
			}
			else
			{
				if(base == 'G') //Similar to previous case
				{
					base = randomBase('A', 'C', 'T');
				}
				else
				{
					base = randomBase('A', 'C', 'G');
				}
			}
		}
		
		return base + "";
	}
	
	
	/**
	 * This method is used to define one base of a set of bases passed like parameters.
	 * @param base1
	 * @param base2
	 * @param base3
	 * @return nucleotide base
	 */
	private char randomBase(char base1, char base2, char base3)
	{
		char base = ' ';
		
		int position = rd.nextInt(3);
		
		//Define randomly which one of three bases is selected
		
		if(position == 0)
		{
			base = base1;
		}
		else
		{
			if(position == 1)
			{
				base = base2;
			}
			else
			{
				base = base3;
			}
		}
		
		return base;
	}
	
	
	
	/**
	 * 
	 * @param father1
	 * @param father2
	 * @return sons in an array
	 */
	public String[] crossover(String father1, String father2)
	{
		String[] sonsChromosomes = new String[2];
		
		int length = father1.length() < father2.length() ? father1.length() : father2.length();
		int point = rd.nextInt(length);
		
		//Just make cross in point of rupture
		sonsChromosomes[0] = father1.substring(0, point) + father2.substring(point); //Father1.part1 + Father2.part2
		sonsChromosomes[1] = father2.substring(0, point) + father1.substring(point); //Father2.part1 + Father1.part2
		
		return sonsChromosomes;
	}
}
