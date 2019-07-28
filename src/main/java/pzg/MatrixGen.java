package pzg;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Random;

public class MatrixGen 
{
	private char[][] mat;
	private int[][] available;
	private int height, width, numReq;
	private List<String> words;
	private Random rand = new Random();
	
	public MatrixGen(int n, int m)
	{	
		height = n;
		width = m;
		mat = new char[height][width];
		available = new int[height][width];
						
		for(int i = 0;i<n;i++)
			for(int j = 0;j<m;j++)
				mat[i][j] = (char)(65 + rand.nextInt(26));
	
	}
	
	public void setWords(int num, int maxLetters)
	{
		numReq = num;
		Words wr = new Words(numReq, maxLetters);
		words = wr.generateWords();
		wr.printWords();
	}
	
	public char[][] getMat(){
		return mat;
	}
	
	private void printMat()
	{
		int i,j;
		for(i = 0;i<mat.length;i++)
		{
			System.out.printf("%-2d  ",i);			
			for(j = 0;j<mat[i].length;j++)
				System.out.printf("%-4c",mat[i][j]);
			System.out.println();
		}		
		System.out.printf("%-4s","");
		
		for(j = 0;j<mat[i-1].length;j++)
			System.out.printf("%-4s",j);
		
		System.out.println();		
		System.out.println();
		
		for(i = 0;i<mat.length;i++)
		{
			System.out.printf("%-2d  ",i);			
			for(j = 0;j<mat[i].length;j++)
				System.out.printf("%-4c",available[i][j] != 0?available[i][j]:'-');
			System.out.println();
		}	
		
		System.out.printf("%-4s","");
		for(j = 0;j<mat[i-1].length;j++)
			System.out.printf("%-4s",j);		
		System.out.println();		
	}
	
	public interface Chooser{
		boolean writeType(int h, int w, String word);
	}
	
	Chooser[] choice = new Chooser[] {
			  new Chooser() { public boolean writeType(int h, int w, String word) { return putHorizontal(h,w,word); } },
			  new Chooser() { public boolean writeType(int h, int w, String word) { return putHorizontalReversed(h,w,word); } },
			  new Chooser() { public boolean writeType(int h, int w, String word) { return putVertical(h,w,word); } },
			  new Chooser() { public boolean writeType(int h, int w, String word) { return putVerticalReversed(h,w,word); } },
			  new Chooser() { public boolean writeType(int h, int w, String word) { return putDiagonally(h,w,word); } },
			  new Chooser() { public boolean writeType(int h, int w, String word) { return putDiagonallyReversed(h,w,word); } },
			  new Chooser() { public boolean writeType(int h, int w, String word) { return putSecondDiag(h,w,word); } },
			  new Chooser() { public boolean writeType(int h, int w, String word) { return putSecondDiagReversed(h,w,word); } }
	};
	
	private void initWords(int init)
	{
		int h, w, selWord = 0, selWrite, last = -1,i,j;
		for(i = 0;i<init;i++)
		{	
			if(last != i){
				selWord = rand.nextInt(numReq);
				last = i;
			}
			selWrite = rand.nextInt(choice.length);
			h = rand.nextInt(height);
			w = rand.nextInt(width);
			if(choice[selWrite].writeType(h, w, words.get(selWord).toUpperCase()) == false)
				i--;			
			else{
				words.remove(selWord);
				numReq--;
			}
		}
		
		for(i = 0;i<available.length;i++)
			for(j = 0;j<available[i].length;j++)
				if(available[i][j] != 0)
					mat[i][j] = (char)available[i][j];			
	}
	
	private boolean putHorizontal(int hgt, int wid, String word)
	{
		int i, len = word.length();
		
		if(len + wid > width) 
			return false;
		for(i = wid;i<wid + len;i++)
		{
			if(available[hgt][i] == 0 || available[hgt][i] == word.charAt(i-wid))
				continue; 
			return false;
		}
		for(i = wid;i<wid+len;i++)
			available[hgt][i] = word.charAt(i-wid);
		return true;
	}
	
	private boolean putHorizontalReversed(int hgt, int wid, String word)
	{
		StringBuilder rev = new StringBuilder(word);		
		return putHorizontal(hgt, wid, new String(rev.reverse()));
	}
	
	private boolean putVertical(int hgt, int wid, String word)
	{
		int i, len = word.length();
		
		if(len + hgt > height) 
			return false;
		for(i = hgt;i<hgt + len;i++)
		{
			if(available[i][wid] == 0 || available[i][wid] == word.charAt(i-hgt))
				continue;
			return false;
		}
		
		for(i = hgt;i<hgt+len;i++)
			available[i][wid] = word.charAt(i-hgt);
		return true;
	}
	
	private boolean putVerticalReversed(int hgt, int wid, String word)
	{
		StringBuilder rev = new StringBuilder(word);		
		return putVertical(hgt, wid, new String(rev.reverse()));
	}
	
	private boolean putDiagonally(int hgt, int wid, String word)	// --- like this -> \ 
	{
		int i,len = word.length();

		if(len + wid > width || len + hgt > height) 
			return false;
		
		for(i = 0;i<len;i++)
		{
			if(available[hgt+i][wid+i] == 0 || available[hgt+i][wid+i] == word.charAt(i))
				continue;
			return false;		
		}
		for(i = 0;i<len;i++)
			available[hgt+i][wid+i] = word.charAt(i);
		
		return true;
	}
	
	private boolean putDiagonallyReversed(int hgt, int wid, String word)
	{		
		StringBuilder rev = new StringBuilder(word);		
		return putDiagonally(hgt, wid, new String(rev.reverse()));
	}
	
	private boolean putSecondDiag(int hgt, int wid, String word) // --- like this -> /
	{	
		int i,len = word.length();
		
		if(wid + len >= width || hgt - len < 0) 
			return false;
		
		for(i = 0;i<len;i++)
		{
			if(available[hgt-i][wid+i] == 0 || available[hgt-i][wid+i] == word.charAt(i))
				continue;
			return false;		
		}
		for(i = 0;i<len;i++)
			available[hgt-i][wid+i] = word.charAt(i);
		System.out.println("DA"+word);
		return true;
	}
	
	private boolean putSecondDiagReversed(int hgt, int wid, String word)
	{	
		StringBuilder rev = new StringBuilder(word);		
		return putDiagonally(hgt, wid, new String(rev.reverse()));
	}
	
	public static void main(String args[]){
		
		Instant start = Instant.now(); 
		MatrixGen matr = new MatrixGen(20,15);
		matr.setWords(35,6);
		matr.initWords(35);
		
		Instant finish = Instant.now();
		long timeElapsed = Duration.between(start, finish).toMillis();
		System.out.println(timeElapsed);
		matr.printMat();
	}
}
