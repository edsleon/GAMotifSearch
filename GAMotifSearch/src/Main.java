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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


/**
 * @author Eng. (C) Edson David Leon - MSc. Carlos Andrés Sierra
 */
public class Main {
	
	/**
     * TODO
     * @param args
     */
    public static void main(String[] args) 
    {   
    	
    	long startTime = System.nanoTime(); //Start time counter 
        
    	
    	//To print in console information about execution
    	BufferedWriter bf = new BufferedWriter ( new OutputStreamWriter(System.out) );
    	BufferedWriter bw = null;
    	
    	if(args.length != 1)
        {
    		//No valid file
    		try 
    		{
				bf.write("Invalid parameter. Please set a valid configuration file. \n");
				bf.flush();
			} 
    		catch (IOException e) 
    		{
				e.printStackTrace();
			}
    		
    		System.exit(0);
        }
    	
    	System.gc(); // Call Garbage Collector
        
  		String setupFile = args[0]; //File to extract configuration parameters
        String line;
        
        // Read parameters from configuration file 
    	String dataset = ""; //Data set of clusters. Can be in format .txt (from Cluster Detection) or .bed
    	String datasetPath = ""; //Path in computer to access to data set
    	int populationSize = 10; //Population size of genetic algorithm
    	int iterations = 10; //Iterations or generations for genetic algorithm
    	String output = "Default_output.txt"; //File to save motif and his attributes
    	String email = ""; //(Optional) E-mail to send a report of results and end-run of tool
    	String hg = ""; //(Required for BED files) Path to hg (human genome) chromosome files
        
        try 
		{
        	BufferedReader br;
        	br = new BufferedReader(new FileReader(setupFile));
        	String[] field_value;
        	
        	line = br.readLine();
            
            while(line != null)
            {
            	field_value = line.split("=");
            	
            	
            	//Read parameters
            	switch(field_value[0])
            	{
            		case "Dataset":
            		{
            			String[] path = field_value[1].split("/");
            			dataset = path[path.length - 1];		
            			datasetPath = field_value[1];
            		}
            		break;
            		case "PopulationSize":
            		{
            			populationSize = Integer.parseInt(field_value[1]);
            		}
            		break;
            		case "Iterations":
            		{
            			iterations  = Integer.parseInt(field_value[1]);
            		}
            		break;
            		case "Output":
            		{
            			 output = field_value[1];
            		}
            		break;
            		case "E-mail":
            		{
            			email = field_value[1];
            		}
            		break;
            		case "PathHG":
            		{
            			hg = field_value[1];
            			
            			File fastaPathF = new File(hg);
                        if (!fastaPathF.exists())
                        {
                        	System.out.println("Human genome folder not exists.");
                        	System.exit(0);
                        }
                    }
            		break;
            	}
            	
            	line = br.readLine();
            }
            
            br.close();
		} 
		catch (FileNotFoundException e1) 
		{
			e1.printStackTrace();
		} 
        catch (IOException e) 
		{
			e.printStackTrace();
		}
        
    	
    	
        //File dataset
    	File f = new File(datasetPath);
    	
    	//Validate first parameter: Clusters Dataset
    	if(!f.exists() || f.isDirectory())
    	{
    		//No valid file
    		try 
    		{
				bf.write("No valid data set file. Please insert a valid path.\nFile: " + datasetPath);
				bf.flush();
			} 
    		catch (IOException e) 
    		{
				e.printStackTrace();
			}
    		
    		System.exit(0);
    	}
    	
    	
    	//Validate second parameter: Population Size
    	if(populationSize < 10)
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
    	
    	
    	//File output creation
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
    	Population population = new Population(datasetPath, populationSize, iterations, hg);
    	
    	String bestMotif = "\n\n" + population.getBest(); //Save info for best motif at end of all iterations
    	
    	
    	//Define time of execution
    	double elapsedTimeInSec;
        elapsedTimeInSec = (System.nanoTime() - startTime) * 1.0e-9;
        
    	//TODO improve results presentation. Add time, fitness, 
    	try 
    	{
    		fw = new FileWriter(o.getAbsoluteFile(), true);
    		bw = new BufferedWriter(fw);
    		
    		bf.write("\n\nElapsed Time: " + elapsedTimeInSec + " seconds\n\n");
        	
    		bw.write( "Best motif is " + bestMotif );
    		bw.write("\nElapsed Time: " + elapsedTimeInSec + " seconds\n\n");
        	
    		
			bf.flush();
			bw.flush();
		} 
    	catch (IOException e) 
    	{
			e.printStackTrace();
		}
    	
    	
    	
        
    	
    	
    	//Sent mail
    	sentMail(email, "The experiment is complete.\n", "DataSet: " + dataset, bestMotif);
    }
    
    
    /**
     * 
     * @param to
     * @param content
     * @param experimentName 
     */
    public static void sentMail(String to, String content, String experimentName, String motif)
    {
        String from = "carlos.andres.sierra.v@gmail.com"; // Sender E-mail
        String host = "localhost"; //SMTP Host
        Properties properties = System.getProperties(); //System Properties
        properties.setProperty("mail.smtp.host", host); // Setup Mail Server

        // Get Default Session object.
        Session session = Session.getDefaultInstance(properties);

         try
         {
            // Create a default MimeMessage object.
            MimeMessage mail = new MimeMessage(session);

            mail.setFrom(new InternetAddress(from)); //Set "From"
            mail.addRecipient(Message.RecipientType.TO, new InternetAddress(to)); //Set "To"
            mail.setSubject("End Run: " + experimentName); //Set "Subject"

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            
            mail.setContent("<h2>" + experimentName + "</h2><br><br>"  + content + "<br><br>Best mofit info:<br>" + motif + "<br><br>Date: " + timeStamp, "text/html" );

            Transport.send(mail); //Send message
            System.out.println("Sent mail successfully....");
        }
        catch (MessagingException e) 
        {
            System.out.println("Fail Sent mail.\n" + e.getMessage());
        }
    }
}