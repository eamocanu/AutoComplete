/**
 * 
 */
package eamocanu.autocomplete;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Adrian
 *
 */
public class SimpleAutoCompleteDictionary implements Dictionary {

	private Map<Character, CharacterNode> wordsMap;
	
	
	
	/**
	 * 
	 */
	public SimpleAutoCompleteDictionary() {
		wordsMap= new HashMap<Character, CharacterNode>();
	}
	

	/* (non-Javadoc)
	 * @see eamocanu.autocomplete.Dictionary#addWordSentence(java.lang.String)
	 */
	@Override
	public void addWordSentence(String sentence) throws Exception {
		// TODO Auto-generated method stub
		//find how many parts it would split on space pr white space.
		//if it can fit into mem do it else error or do a few words only
	}


	@Override
	public void addWord(String word) throws Exception {
		CharacterNode startNode;
		
		if (word==null) return;
		if (word.length()==0) return;
		
		if (!wordsMap.containsKey(word.charAt(0))){
			startNode= new SimpleCharacterNode(word.charAt(0), 0);
			wordsMap.put(word.charAt(0), startNode);
		} else {
			startNode= wordsMap.get(word.charAt(0));
		}
		
		CharacterNode crtNode= startNode;
		for (int i =1; i<word.length(); i++){
			char crtChar= word.charAt(i);
			
			if (crtNode.hasChild(crtChar)){
				crtNode= crtNode.getChild(crtChar);
			} else {
				CharacterNode childNode= new SimpleCharacterNode(crtChar, i); 
				crtNode.addChild(childNode);
				crtNode= childNode;
			}
		}
		
		crtNode.addChild(SimpleCharacterNode.END_NODE);
	}

	
	
	/* (non-Javadoc)
	 * @see eamocanu.autocomplete.Dictionary#getAllFormattedWordsStartingWith(char)
	 */
	@Override
	public List<String> getAllFormattedWordsStartingWith(char prefChar) {
		// TODO Auto-generated method stub
		return null;
	}

	
	/* (non-Javadoc)
	 * @see eamocanu.autocomplete.Dictionary#getAllWords()
	 */
	@Override
	public List<String> getAllWords() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see eamocanu.autocomplete.Dictionary#getAllWordsStartingWith(char)
	 */
	@Override
	public List<String> getAllWordsStartingWith(char prefChar) {
		if (wordsMap.containsKey(prefChar)){
			return getWordsForPrefix( wordsMap.get(prefChar) );
		}
		
		return Collections.emptyList(); 
	}

	
	/* (non-Javadoc)
	 * @see eamocanu.autocomplete.Dictionary#getNextNodeForPrefix(char, eamocanu.autocomplete.CharacterNode)
	 */
	@Override
	public CharacterNode getNextNodeForPrefix(char nextChar, CharacterNode wordNode) {
		
		if (wordNode==null){
			if (wordsMap.containsKey(nextChar)){
				return wordsMap.get(nextChar);
			} else {
				return SimpleCharacterNode.EMPTY;
			}	
		}
		
		if (wordNode.hasChild(nextChar)){
			return wordNode.getChild(nextChar);
		}
		
		return SimpleCharacterNode.EMPTY;
	}
	

	/* (non-Javadoc)
	 * @see eamocanu.autocomplete.Dictionary#getWordsForPrefix(eamocanu.autocomplete.CharacterNode)
	 */
	@Override
	public List<String> getWordsForPrefix(CharacterNode wordNode) {//TODO add a size param for list 
		List<String> words= new LinkedList<String>();
		getAllWords(words, wordNode, new StringBuffer());
		return words;
	}

	
	//TODO get all words HIGH RANKED ones first, until a specified length of list
	//TODO add length of list param
	private void getAllWords(List<String> generatedWords, 
			CharacterNode wordNode, StringBuffer crtWord) {
		
		if (wordNode == SimpleCharacterNode.END_NODE){
			generatedWords.add(crtWord.toString().intern());
			return;
		}
		
		crtWord.append(wordNode.getChar());
		
		List<CharacterNode> children= wordNode.getChildren();
		for (CharacterNode crtChild: children){
			getAllWords(generatedWords, crtChild, crtWord);
		}
		
		crtWord.setLength(crtWord.length()-1);//drop last added (this wordNode)
	}


	/* (non-Javadoc)
	 * @see eamocanu.autocomplete.Dictionary#getWordsForPrefix(java.lang.String)
	 */
	@Override
	public List<String> getWordsForPrefix(String pref) {//TODO test next :)
		if (pref==null) return Collections.emptyList();
		if (pref.length()==0) return Collections.emptyList();
		
		CharacterNode crtNode= getNextNodeForPrefix(pref.charAt(0), null);
		
		if (crtNode==SimpleCharacterNode.EMPTY){
			return Collections.emptyList();
		}
		
		if (pref.length()==1) {
			List<String> words = new LinkedList<String>();
			words.add(""+crtNode.getChar());
			return words;
		}
		
		//traverse words tree to find end of prefix location in tree
		for (int i=1; i<pref.length(); i++){
			crtNode= getNextNodeForPrefix(pref.charAt(i), crtNode);
			if (crtNode==SimpleCharacterNode.EMPTY) return Collections.emptyList();
		}
		
		return getWordsForPrefix(crtNode);
	}

	
	/* (non-Javadoc)
	 * @see eamocanu.autocomplete.Dictionary#hasWord(java.lang.String)
	 */
	@Override
	public boolean hasWord(String word) {
		if (word==null) return false;
		if (word.length()==0) return false;
		
		CharacterNode crtNode= getNextNodeForPrefix(word.charAt(0), null);
		
		if (crtNode==SimpleCharacterNode.EMPTY){
			return false;
		}

		for (int i=1; i<word.length(); i++){
			crtNode= getNextNodeForPrefix(word.charAt(i), crtNode);
			if (crtNode==SimpleCharacterNode.EMPTY) return false;
		}
		
		return false;
	}

}
