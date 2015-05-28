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

package Structures;


/**
 *
 * @author Eng. (C) Edson David Leon - MSc. Carlos Andrés Sierra
 */
public class Cluster {

	//Attributes
	private String chromosome;
	private int start;
	private int end;
	private String sequence;
	private String strand;
	private int reads;
	private int mutations;
	private int[] mutationT2C;
	
	
	/**
	 * Zero-parameters constructor
	 */
	public Cluster() {}

	
	/**
	 * Constructor with parameters for a complete definition of a cluster
	 * @param chromosome
	 * @param start
	 * @param end
	 * @param sequence
	 * @param strand
	 * @param reads
	 * @param mutations
	 * @param mutationsT2C
	 */
	public Cluster(String chromosome, int start, int end, String sequence, String strand, int reads, int mutations, int[] mutationsT2C)
	{
		this.chromosome = chromosome;
		this.start = start;
		this.end = end;
		this.sequence = sequence;
		this.strand = strand;
		this.reads = reads;
		this.mutations = mutations;
		this.mutationT2C = mutationsT2C;
	}
	
	
	
	/**
	 * @return the chromosome
	 */
	public String getChromosome() 
	{
		return chromosome;
	}


	/**
	 * @param chromosome the chromosome to set
	 */
	public void setChromosome(String chromosome) 
	{
		this.chromosome = chromosome;
	}


	/**
	 * @return the start
	 */
	public int getStart() 
	{
		return start;
	}

	
	/**
	 * @param start the start to set
	 */
	public void setStart(int start) 
	{
		this.start = start;
	}

	
	/**
	 * @return the end
	 */
	public int getEnd() 
	{
		return end;
	}

	
	/**
	 * @param end the end to set
	 */
	public void setEnd(int end)
	{
		this.end = end;
	}

	
	/**
	 * @return the sequence
	 */
	public String getSequence() 
	{
		return sequence;
	}

	
	/**
	 * @param sequence the sequence to set
	 */
	public void setSequence(String sequence) 
	{
		this.sequence = sequence;
	}

	/**
	 * @return the strand
	 */
	public String getStrand() 
	{
		return strand;
	}

	
	/**
	 * @param strand the strand to set
	 */
	public void setStrand(String strand) 
	{
		this.strand = strand;
	}

	
	/**
	 * @return the reads
	 */
	public int getReads() 
	{
		return reads;
	}

	
	/**
	 * @param reads the reads to set
	 */
	public void setReads(int reads) 
	{
		this.reads = reads;
	}

	
	/**
	 * @return the mutations
	 */
	public int getMutations() 
	{
		return mutations;
	}

	
	/**
	 * @param mutations the mutations to set
	 */
	public void setMutations(int mutations) 
	{
		this.mutations = mutations;
	}

	
	/**
	 * @return the mutationT2C
	 */
	public int[] getMutationT2C() 
	{
		return mutationT2C;
	}

	
	/**
	 * @param mutationT2C the mutationT2C to set
	 */
	public void setMutationT2C(int[] mutationT2C) 
	{
		this.mutationT2C = mutationT2C;
	}

}
