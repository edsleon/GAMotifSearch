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
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author Eng. (C) Edson David Leon - MSc. Carlos Andrés Sierra
 */
public class Main {
	
	/**
     * 
     * @param args
     */
    public static void main(String[] args) 
    {   
    	BufferedWriter bf = new BufferedWriter ( new OutputStreamWriter(System.out) );
    	BufferedWriter bw = null;
    	
    	if(args.length < 4)
        {
    		//No valid file
    		try 
    		{
				bf.write("Incomplete parameters.\n");
				bf.flush();
			} 
    		catch (IOException e) 
    		{
				e.printStackTrace();
			}
    		
    		System.exit(0);
        }
    	
    	//TODO Read parameters from configuration file 
    	
    	String file = args[0];
    	int sizePopulation = Integer.parseInt( args[1] );
    	int iterations = Integer.parseInt( args[2] );
    	String output = args[3];
    	
        //File dataset
    	File f = new File(file);
    	
    	//Validate first parameter: Clusters Dataset
    	if(!f.exists() || f.isDirectory())
    	{
    		//No valid file
    		try 
    		{
				bf.write("No valid file. Please insert a valid path.\n");
				bf.flush();
			} 
    		catch (IOException e) 
    		{
				e.printStackTrace();
			}
    		
    		System.exit(0);
    	}
    	
    	//Validate second parameter: Population Size
    	if(sizePopulation < 10)
    	{
    		//No valid size
    		try 
    		{
				bf.write("No valid population size. Please insert a valid size (minimum 10).\n");
				bf.flush();
			} 
    		catch (IOException e) 
    		{
				e.printStackTrace();
			}
    		
    		System.exit(0);
    	}
    	
    	
    	//Validate third parameter: Iterations
    	if(iterations < 10)
    	{
    		//No valid iterations
    		try 
    		{
				bf.write("No valid interation. Please insert a valid amount of iterations (minimum 10).\n");
				bf.flush();
			} 
    		catch (IOException e) 
    		{
				e.printStackTrace();
			}
    		System.exit(0);
    	}
    	
    	
    	//File dataset
    	File o = new File(output);
    	
    	//Create output file
    	FileWriter fw;
        if (!o.exists()) 
        {
            try 
            {
                o.createNewFile();
            } 
            catch (IOException ex) 
            {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("\n\nFile " + o);
                System.exit(0);
            }
        }
    	
        //Start population and run GA
    	Population population = new Population(file, sizePopulation, iterations);
    	
    	
    	//TODO improve results presentation. Add time, fitness, 
    	try 
    	{
    		fw = new FileWriter(o.getAbsoluteFile(), true);
    		bw = new BufferedWriter(fw);
    		
    		bf.write( population.getBest() + "\n\n" );
    		bw.write( "Best motif is " + population.getBest() );
			bf.flush();
			bw.flush();
		} 
    	catch (IOException e) 
    	{
			e.printStackTrace();
		}
    }
}