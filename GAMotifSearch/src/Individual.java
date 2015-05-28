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


/**
 *
 * @author Eng. (C) Edson David Leon - MSc. Carlos Andrés Sierra
 */
public class Individual 
{
	private String fenotipe; //Representation of individual
	private String genotipe; //Genetic representation of individual
    private double fitness;
    
    
    /**
     * Zero-parameters constructor
     */
    public Individual() 
    {
    	this.fenotipe = "";
    	this.genotipe = "";
    	this.fitness = 0.0;
    }
    
    
    /**
     * Constructor of the class that requires the genetic representation of new individual
     * @param genotipe
     */
    public Individual(String genotipe, double fitness) 
    {
    	this.genotipe = genotipe;
        this.obtainFenotipe();
        this.fitness = fitness;
    }


	/**
	 * @return the fenotipe
	 */
	public String getFenotipe() 
	{
		return fenotipe;
	}


	/**
	 * @param fenotipe the fenotipe to set
	 */
	public void setFenotipe(String fenotipe) 
	{
		this.fenotipe = fenotipe;
	}


	/**
	 * @return the genotipe
	 */
	public String getGenotipe() 
	{
		return genotipe;
	}


	/**
	 * @param genotipe the genotipe to set
	 */
	public void setGenotipe(String genotipe) 
	{
		this.genotipe = genotipe;
	}


	/**
	 * @return the fitness
	 */
	public double getFitness() 
	{
		return fitness;
	}


	/**
	 * @param fitness the fitness to set
	 */
	public void setFitness(double fitness)
	{
		this.fitness = fitness;
	}
  
	
	/**
	 * 
	 */
	public void obtainFenotipe()
	{
		this.fenotipe = this.genotipe; //Representation of individual is the same in both genotipe and fenotipe
	}
   
}