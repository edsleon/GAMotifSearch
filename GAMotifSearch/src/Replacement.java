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

import java.util.Random;


/**
 * TODO
 * @author Eng. (C) Edson David Leon - MSc. Carlos Andrés Sierra
 */
public class Replacement {

	
	private Random rd = new Random(); //TODO
	
	
	/**
	 * Constructor of the class
	 */
	public Replacement() {}
	
	
	/**
	 * TODO
	 * @param father1
	 * @param father2
	 * @param son1
	 * @param son2
	 * @return TODO
	 */
	public Individual[] steadyState(Individual father1, Individual father2, Individual son1, Individual son2)
	{
		Individual[] winners = new Individual[2];
		
		//TODO
		if(father1.getFitness() < father2.getFitness()) //TODO
		{
			if(son1.getFitness() < son2.getFitness()) //TODO
			{
				winners[0] = this.roulette(father1, son1); //Worst father - Worst son
				winners[1] = this.roulette(father2, son2); //Best father - Best son
			}
			else //TODO
			{
				winners[0] = this.roulette(father1, son2); //Worst father - Worst son
				winners[1] = this.roulette(father2, son1); //Best father - Best son
			}
		}
		else //TODO
		{
			if(son1.getFitness() < son2.getFitness()) //TODO
			{
				winners[0] = this.roulette(father2, son1); //Worst father - Worst son
				winners[1] = this.roulette(father1, son2); //Best father - Best son
			}
			else //TODO
			{
				winners[0] = this.roulette(father2, son2); //Worst father - Worst son
				winners[1] = this.roulette(father1, son1); //Best father - Best son
			}
		}
		
		return winners;
	}
	

	
	/**
	 * TODO
	 * @param player1
	 * @param player2
	 * @return TODO
	 */
	private Individual roulette(Individual player1, Individual player2)
	{
		// TODO
		double totalFitness = player1.getFitness() + player2.getFitness();
		
		//TODO
		double point = player1.getFitness() / totalFitness;
		
		return rd.nextDouble() < point ? player1 : player2; //TODO
	}

}
