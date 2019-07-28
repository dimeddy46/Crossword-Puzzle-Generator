package pzg;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Words {
	private int numReq, numTotal;
	private List<String> selectedWords, totalWords;
	private final File dictionary = new File("Italian.txt");
	
	public Words(int req, int maxLetters)
	{
		selectedWords = new ArrayList<String>();
		totalWords = new ArrayList<String>();
		
		BufferedReader br = null;
		String st;
	
		try {
			br = new BufferedReader(new FileReader(dictionary));		
		} 
		catch (FileNotFoundException e) { 
			System.out.println("Dictionary is missing.");
			System.exit(0);
		} 
		
		try{
			while((st = br.readLine()) != null)
			{
				if(maxLetters >= st.length())
					totalWords.add(st);
			}
		}
		catch(IOException ex){
			System.out.println("Failed to read this dictionary.");
			System.exit(0);
		}
		numReq = req;		
		numTotal = totalWords.size();
		System.out.println("Total words extracted from dictionary: "+numTotal);
	}
	
	private void generateIndexes()
	{
		Random x = new Random();
		int[] index = new int[numReq]; 
		int i;
		
		selectedWords.clear();
		for(i = 0;i<numReq;i++)
			index[i] = x.nextInt(numTotal);	
		Arrays.sort(index);
		
		for(i = 0;i<numReq;i++)
			selectedWords.add(totalWords.get(index[i]));
	}
	
	public void setRequiredWords(int req)
	{
		numReq = req;
	}
	
	public void printWords()
	{		
		int divz = numReq / 5 ,rest = numReq % 5, i,j;
		try{
			for(i = 0;i<divz;i++)
			{
				for(j = 0;j<5;j++)
					System.out.printf("%-15s",selectedWords.get(i+divz*j));
				if(rest-- > 0)
					System.out.printf("%-15s",selectedWords.get(i+divz*j));
				System.out.println();
			}
		}
		catch(IndexOutOfBoundsException ex){ } 
	}
	
	public List<String> generateWords()
	{		
		generateIndexes();
		return selectedWords;
	}
		
	private boolean isDerived(String a, String b)
	{
		int len1 = a.length(), len2 = b.length(), c = 0;
		if(len1 == len2)
		{
			for(int i = 0;i<len1;i++)
			{
				if(a.charAt(i) == b.charAt(i))
					c++;
				else break;
			}
			if(c >= len1-2)
				return true;
		}
		return false;			
	}
	
	public void writeDict()
	{
		File f = new File("Italian2.txt");
		BufferedWriter bw = null;
		//int len, fp;		
		String st = totalWords.get(0);
		try{
			bw = new BufferedWriter(new FileWriter(f));
			for(String p : totalWords)
			{
				if(isDerived(st,p)){
					continue;
				}
				bw.write(st+"\n");
				st = p;
			}
			bw.close();
		}
		catch(IOException ex){
			System.out.println("Failed to write");
		}
	}
	
}
