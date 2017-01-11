package com.agrohackathon;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessRead;
//import org.apache.pdfbox.io.RandomAccessRead;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.tika.exception.TikaException;


public class Extract {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		 if (args.length < 1) {
	            System.err.println("Usage: input url of the resource as arg input");                
	            System.exit(1);
	        } 

		 	File file = new File(System.getProperty("user.dir")+System.getProperty("file.separator")+"temp");
		    String identifier = "";
			file.mkdirs();
			
			String filename="pdf"+(System.currentTimeMillis()/1000)+".pdf";
			
			download_save(args[0], filename);
			
			
		 
		 PDFManager pm = new PDFManager();
		 pm.setFilePath(System.getProperty("user.dir")+System.getProperty("file.separator")
			+"temp"+System.getProperty("file.separator")+filename);
		 
		 pm.toTextiText();
		 try {
			pm.saveImagesiText(args[1]);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		 
		System.exit(1);

		 try {
			pm.testPDFBoxExtractImages(System.getProperty("user.dir")+System.getProperty("file.separator")
				+"temp"+System.getProperty("file.separator")+filename, args[1]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 try {
			System.out.println("OUT"+pm.ToText());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 
	       //String s = extract("test");        
	              
	       //System.out.println(s);
	
		 try {
			pm.toTextTika();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TikaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	}

	public static void download_save(String url, String filename)
	{
		try
		{
			URL website = new URL(url);
			ReadableByteChannel rbc = Channels.newChannel(website.openStream());
			FileOutputStream fos = new FileOutputStream(System.getProperty("user.dir")+System.getProperty("file.separator")
				+"temp"+System.getProperty("file.separator")+filename);
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		}
		catch(java.lang.Exception e)
		{
			e.printStackTrace();
		}
	}
	
}









