package eamocanu.autocomplete.driver;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.Scanner;

import eamocanu.autocomplete.CharacterNode;
import eamocanu.autocomplete.DictionaryWithContext;

import ui.JTreeDemo;
import ui.UI;


public class Main {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//convertFileNoSpaceAllowed("world.cities.txt");
		//convertFileWithSpace("world.cities.unprocessed.txt");
		DictionaryWithContext d = new DictionaryWithContext();
//		for (char c=0; c<355; c++){
//			System.out.println((int)c + " CHAR:"+c+":END");
//		}
//		readWordFile(d,"PHUN.txt");
//		readWordFile(d,"processedCitieswspace.txt");
//		readWordFile(d,"Cities.txt");
		
//		readWordFile(d,"mghtmag2.txt");
		readWordFile(d,"dict1.txt");
		//FAILs
//		readWordFile(d,"world.cities.unprocessed.txt");
//		readWordFile(d,"Dictionary");
		
		
//		String [] words = {
//				"aaaaa", "abercrombie", "abot", "aba", "abc", "abca", "afterlife", "apparatus",
//							"anus", "acus", "angus", "atic", "aticus",
//							"brown", "brownie", "brownie_puffs",
//							"New Bruns", "New York", "Old Yorkie", "New Village", "New Lane", "New Devel", 
//							"New Mapoon",
//							
//		};
		String [] words = {
				"New York", "Yorkville", "York", 
				"One day in New York I was going home to my car and then",
				"One day in New York I was going home to",
				"One day in New York was going home",
				
				"home and my homilicious car",
				"home and homer",
				
				"going home to my",
				"going home to",
				"m hol",
				"m ho",
				
				"New Bruns", "New York", "Old Yorkie", "New Village", "New Lane", "New Devel", "Yorkenstein"
				
		};
		
		
		for (int i=0;i<words.length; i++){
			try {
				//System.out.println("** adding word **" + words[i]);
				d.addWordSentence(words[i]);
			} catch (Exception e){
				System.out.println(e);
			}
		}

		
		
//		List<String> retrievedWords= d.getAllWordsWFakes();//d.getAllWords();
//		CharacterNode root= new CharacterNode('_');
//		for (String s:retrievedWords){
//			System.out.println(s);
//		}
//		System.out.println("Total words: "+retrievedWords.size());
		
		String match= "O";
		CharacterNode wn =d.getNextNodeForPrefix(match.charAt(0), null);
		JTreeDemo demo= new JTreeDemo(wn.tn);
		demo.setVisible(true);
		
		//System.out.println("Total m-words: "+d.getAllWordsStartingWith(match.charAt(0)).size());
				
		//simulate typing 1 char at a time
		for (int i=1; i<match.length(); i++){
			wn= d.getNextNodeForPrefix(match.charAt(i), wn);
			printAll(d, match.substring(0,i+1), wn);
//			JTreeDemo demo= new JTreeDemo(wn.tn);
//			demo.setVisible(true);
		}
		
		System.out.println();
//		System.out.println(d.hasWord("abc") +"=true");
//		System.out.println(d.hasWord("a") +"=false");
//		System.out.println(d.hasWord("apparatus") +"=true");
//		System.out.println(d.hasWord("New York") +"=true");
		System.out.println(d.hasWord("and") +"=false");
		System.out.println(d.hasWord("home") +"=false");
		System.out.println(d.hasWord("homer") +"=false");
		System.out.println(d.hasWord("hom") +"=false");
		System.out.println(d.hasWord("home and homer") +"=true");
		
		
		UI ui=new UI(d);
		ui.show();
		
	}

	private static void printAll(DictionaryWithContext d, String strPrefix, CharacterNode wn) {
		List<String> wds= d.getFormattedWordsForPrefix(wn);
		
		System.out.println("Total "+strPrefix+"-words: "+wds.size());
		String pre= strPrefix.substring(0,strPrefix.length() -1 );
		for (String s: wds){
			//System.out.println(pre +"["+ s.charAt(0) +"]"+s.substring(1,s.length()));
//			if (s.contains(Dictionary.PREFIX_DELIMITER)){
//				s= s.replace(Dictionary.PREFIX_DELIMITER, pre);
//			}
			System.out.println(s);
		}
	}
	
	
	private static void convertFileWithSpace(String filename){
		StringBuffer cityNames= new StringBuffer(999);
		Scanner sc=null;
		Writer out=null;
		BufferedReader reader = null;
		
		try {
			reader = new BufferedReader(new FileReader(filename));
			sc = new Scanner(reader);
			sc.useDelimiter("\t");
			out = new OutputStreamWriter(new FileOutputStream("processedwspace.txt"));
			int i=0;
			while (sc.hasNext()){
			//while(true){
			//	i++;
			//	if (i==321434) break;
				String skip=sc.next();//skip 1023234334
	//			System.out.println(skip);
				
				String s= sc.next();
//				System.out.println(s);
				cityNames.append( s + "\n" );
				String line= sc.nextLine();
				
//				System.out.println(line);
//				System.out.println(sc.hasNext());
//				System.out.println();
			}
			
			out.write(cityNames.toString());
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			sc.close();
		}
	}
	
	private static void convertFileNoSpaceAllowed(String filename){
		StringBuffer cityNames= new StringBuffer(999);
		Scanner sc=null;
		Writer out=null;
		BufferedReader reader = null;
		
		try {
			reader = new BufferedReader(new FileReader(filename));
			sc = new Scanner(reader);
			out = new OutputStreamWriter(new FileOutputStream("processed.txt"));
			int i=0;
			while (sc.hasNext()){
			//while(true){
			//	i++;
			//	if (i==321434) break;
				String skip=sc.next();//skip 1023234334
	//			System.out.println(skip);
				
				String s= sc.next();
	//			System.out.println(s);
				cityNames.append( s + "\n" );
				String line= sc.nextLine();
				
//				System.out.println(line);
//				System.out.println(sc.hasNext());
//				System.out.println();
			}
			
			out.write(cityNames.toString());
			out.close();
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		} finally {
			sc.close();
		}
	}
	
	private static void readWordFile(DictionaryWithContext d, String filename){
		File file = new File(filename);
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(file));
			String text = null;
long i=0;
			// repeat until all lines is read
			while ((text = reader.readLine()) != null) {
				try {
					if (text.length() ==0) continue;
					text= text.substring(1,text.length());
					if (text.length() ==0) continue;
//					System.out.println("word is:"+text+":word_end");
					//if (text.toLowerCase().charAt(0) <'a' || text.toLowerCase().charAt(0) >'z') continue;
					if (text.toLowerCase().charAt(0) <'0' || text.toLowerCase().charAt(0) >'9') continue;
		i++;			
		
					//text= text.toLowerCase();
		if (i>= 10000) break; //and look at the tree; TODO
					d.addWordSentence(text);
					if (i % 10000 == 0) {
						System.gc();
						System.out.println(i);
					}
				} catch (Exception e) {
					e.printStackTrace();
					break;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}


	
}
