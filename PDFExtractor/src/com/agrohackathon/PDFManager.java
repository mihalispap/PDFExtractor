package com.agrohackathon;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStream;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;



 
public class PDFManager {
    
   private PDFParser parser;
   private PDFTextStripper pdfStripper;
   private PDDocument pdDoc ;
   private COSDocument cosDoc ;
   
   private String Text ;
   private String filePath;
   private File file;
 
    public PDFManager() {
        
    }
    
    public void saveImagesiText(String output_dir) throws IOException
    {
    	PdfReader reader;

        File file = new File(filePath);
        reader = new PdfReader(file.getAbsolutePath());
        for (int i = 0; i < reader.getXrefSize(); i++) {
            PdfObject pdfobj = reader.getPdfObject(i);
            if (pdfobj == null || !pdfobj.isStream()) {
                continue;
            }
            
            System.out.println(output_dir + System.nanoTime() + ".jpg");
            
            PdfStream stream = (PdfStream) pdfobj;
            PdfObject pdfsubtype = stream.get(PdfName.SUBTYPE);
            if (pdfsubtype != null && pdfsubtype.toString().equals(PdfName.IMAGE.toString())) {
                byte[] img = PdfReader.getStreamBytesRaw((PRStream) stream);
                FileOutputStream out = new FileOutputStream(new File(output_dir + System.nanoTime() + ".jpg"));
                out.write(img);
                out.flush();
                out.close();
            }
        }
    }
    
    public String toTextTika() throws IOException, TikaException
    {
    	File file = new File(filePath);
        
        //Instantiating Tika facade class
        Tika tika = new Tika();
        String filecontent = tika.parseToString(file);
        
        System.out.println("Extracted Content: " + filecontent);
        
        return filecontent;
    }
    
   public String ToText() throws IOException
   {
       this.pdfStripper = null;
       this.pdDoc = null;
       this.cosDoc = null;
       
       pdDoc = PDDocument.load(new File(filePath));
       pdfStripper = new PDFTextStripper();
       
       pdDoc.getNumberOfPages();
       
       // reading text from page 1 to 10
       // if you want to get text from full pdf file use this code
       // pdfStripper.setEndPage(pdDoc.getNumberOfPages());
       
       Text = pdfStripper.getText(pdDoc);
       
       pdDoc.close();
       return Text;
   }
 
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    /*
     * 
     * TODO:
     * 		exploit sentences that contain Figure XX ? nice idea!
     * 
     * */
    public void testPDFBoxExtractImages(String pdf_file, String output_dir) throws Exception 
    {
        PDDocument document = PDDocument.load(new File(pdf_file));
        
        int i=0;
        
        PDPageTree list = document.getPages();
        for (PDPage page : list) 
        {
        	
            PDResources pdResources = page.getResources();
                        
            //System.out.println(pdResources.toString());
            for (COSName c : pdResources.getXObjectNames()) 
            {
            	System.out.println(c.toString());
                PDXObject o = pdResources.getXObject(c);
                
                
                
                if (o instanceof org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject) 
                {
                    File file = new File(output_dir + System.nanoTime() + ".png");
                    
                    System.out.println(
                    		((org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject) o).getMetadata()
                    				);
                    
                    System.out.println("Output file:"+output_dir + System.nanoTime() + ".png");
                    ImageIO.write(((org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject)o).getImage(), "png", file);
                    
                    /*System.out.println("--PAGE:"+i+"--");
                    System.out.println(this.extractTextFromPage((i+1)));
                    System.out.println("----");*/
                    
                }
                else
                {
                	System.out.println("TTT");
                	
                	File file = new File(output_dir + System.nanoTime() + ".png");
                    
                    System.out.println(
                    		((org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject) o).getMetadata()
                    				);
                    
                    System.out.println("Output file:"+output_dir + System.nanoTime() + ".png");
                    ImageIO.write(((org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject)o).getImage(), "png", file);
                    
                    System.out.println("--PAGE:"+i+"--");
                    System.out.println(this.extractTextFromPage((i+1)));
                    System.out.println("----");
                }
            }
            

        	i++;
        }
    }
    
    public void toTextiText()
    {
    	try { 
    		
    		
    		//Create PdfReader instance. 
    		PdfReader pdfReader = new PdfReader(filePath);	  
    		
    		
    		//Get the number of pages in pdf. 
    		int pages = pdfReader.getNumberOfPages();   
    		//Iterate the pdf through pages. 
    		for(int i=1; i<=pages; i++) 
    		{ 
    			//Extract the page content using PdfTextExtractor. 
    			String pageContent = PdfTextExtractor.getTextFromPage(pdfReader, i);   
    			//Print the page content on console. 
    			System.out.println("Content on Page " + i + ": " + pageContent);
    			
    			
    		}   
    		//Close the PdfReader. 
    		pdfReader.close(); 
    		} 
    	catch (Exception e) 
    	{ 
    		e.printStackTrace(); 
    	}
    	
    }
    
    public String extractTextFromPage(int page) throws IOException
    {
    	this.pdfStripper = null;
        this.pdDoc = null;
        this.cosDoc = null;
        
        pdDoc = PDDocument.load(new File(filePath));
        pdfStripper = new PDFTextStripper();
        
        pdDoc.getNumberOfPages();
        
        // reading text from page 1 to 10
        // if you want to get text from full pdf file use this code
        // pdfStripper.setEndPage(pdDoc.getNumberOfPages());
        
        pdfStripper.setStartPage(page);
        pdfStripper.setEndPage(page);
        
        Text = pdfStripper.getText(pdDoc);
        
        pdDoc.close();
        return Text;
    }
}










