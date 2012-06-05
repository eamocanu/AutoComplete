/**
 * 
 */
package eamocanu.autocomplete.driver;

import java.util.List;

import eamocanu.autocomplete.SimpleAutoCompleteDictionary;

/**
 * @author Adrian
 *
 */
public class SimpleDictionaryDriver {
	SimpleAutoCompleteDictionary dictionary;
	
	
	/**
	 * 
	 */
	public SimpleDictionaryDriver() {
		dictionary = new SimpleAutoCompleteDictionary();
	}

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SimpleDictionaryDriver driver = new SimpleDictionaryDriver();
		driver.addWords();
		driver.testDictionary();
	}
	
	
	private void addWords() {
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
				dictionary.addWord(words[i]);
			} catch (Exception e){
				System.out.println(e);
			}
		}
		
	}
	

	private void testDictionary() {
		 List<String> words= dictionary.getAllWordsStartingWith('N');
		 printList(words);
	}
	
	
	public void printList(List<String> list){
		for(String crtString: list){
			System.out.println(crtString);
		}
	}

}
