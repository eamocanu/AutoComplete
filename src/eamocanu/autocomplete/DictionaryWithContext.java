package eamocanu.autocomplete;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import eamocanu.autocomplete.exception.WordException;

/**
 * @author Adrian
 * 
 * This dictionary provides word auto complete features along with some context.
 * 		The context is words which surround the possible auto suggested words
 * 		based on user input (taken as prefix). Thus each suggested word, has
 * 		context surrounding it.
 * 
 * TODO
 * Improve by having a var in node to keep track of its depth in its local tree.
 * This way, it can decide how big to make the map of children. It should decrease
 * as depth increases (give 0 depth at root)
 * 
 * TODO: use internal() for string concat (for pointers instead of char copies)
 * 
 * TODO: this dict takes O(n^2) mem to store sentences and O(n) to store words.
 * gotta bring the quadratic down - String internal() should take care of it
 * Otherwise, put words in a list, then in the trees I'll have 
 * ptrs to list words and char index in that word so that. Oh wtd I can have all
 * letter stored in a list then have pointers to them in the tree. Does this make
 * sense? A char takes less mem than a pointer. Gotta look deeper into it. 
 */
public class DictionaryWithContext {

	/** map to hold tree of chars to form words */
	private Map<Character, CharacterNodeWithContext> wordsMap;
//	private List<Character> startChars;
	
	/** maximum context prefix length in characters */
	private int prefixLen=20;
	
	/** maximum context suffix length in characters */
	private int suffixLen=20;
	
	
	
	
	public DictionaryWithContext(){
//		startChars= new ArrayList<Character>();
		wordsMap= new HashMap<Character, CharacterNodeWithContext>();
		for (char c='a'; c<='z'; c++){
//			startChars.add(c);
			CharacterNodeWithContext wn= new CharacterNodeWithContext(c);
//			wn.setOriginalWord(true);
			wordsMap.put(c, wn);
		}
	}
	
	

	/* (non-Javadoc)
	 * @see Dictionary#addWord(java.lang.String)
	 */
	public synchronized void addWordSentence(String w) throws Exception{
		add(w,"","", true);
	}
	
	
	public synchronized void addWord(String w) throws Exception{
		if (w.contains(" ")) throw new Exception("Words cannot contain spaces");
		//^ it is better to throw the exception when adding each char to tree
		//bc that way this is O(n) instead of O(2*n); then check if crtChar==' '
		add(w,"","", true);
	}
	
	
	/** Adds a word or sentence to the dictionary. Sentences are made up of multiple
	 * words separated by 1 or multiple spaces.
	 * 
	 * A suffix and prefix is used to show the word in the context in which it was found
	 * if the word is part of a sentence. There are optional.
	 * 
	 * @param word		The word to add
	 * @param suffix	Used when w is part of a sentence to provide content after w
	 * @param prefix	Used when w is part of a sentence to provide before after w
	 * @param realWord	if true, it means the word was added to the dictionary
	 * 						if false, the word comes from another dictionary word-sentence
	 * 						Used when all words from dictionary are retrieved, it 
	 * 						denotes only real words, not words from original sentences
	 * 
	 * @throws Exception	If the word to add has length 0
	 */
	private synchronized void add(String word, String suffix, String prefix, boolean realWord) throws WordException{
		if (word.length() == 0) throw new WordException("Words cannot have no characters");
		CharacterNodeWithContext wn= wordsMap.get(word.charAt(0));
		
		if (wn == null){
			//this words starts w a character which has not been mapped/encountered yet
			wn= new CharacterNodeWithContext(word.charAt(0));
			wordsMap.put(word.charAt(0), wn );
//			startChars.add(wd.charAt(0));
			wn.setOriginalWord(realWord);
		}

		if (!wn.isOriginal() && realWord){
			wn.setOriginalWord(true);
		}
		
		for (int i=1; i<word.length() ; i++){
			char crtChar= word.charAt(i);
			if (wn.getChild(crtChar) == null){
				wn.addChild(crtChar);
			}
			//special case for indexing words in the middle of the sentence
			if (crtChar == ' '){
				createNewWordIndex(i, word);
			}

//			System.out.println(wn);
			wn= wn.getChild(crtChar);
			if (!wn.isOriginal() && realWord){
				wn.setOriginalWord(true);
			}
		}
		
//		//add ending
//		//wn.addChild(CharacterNode.END_NODE);
//		CharacterNodeI end= new CharacterNode(CharacterNode.END);
//		wn.addChild(CharacterNode.END_NODE);

		//to save memory for real words
		if (realWord)
			wn.addChild(CharacterNodeWithContext.END_NODE);
		else{
			CharacterNodeWithContext end= new CharacterNodeWithContext(CharacterNode.END);
			end.setOriginalWord(realWord);
			wn.addChild(end);
			end.setPrefixWord(prefix);
			end.setSuffixWord(suffix);
		}
	}
	
	
	/** Starts indexing word which starts at space character in sentence.
	 * 
	 * It also adds context to this word ie: a prefix and suffix 
	 * surrounding the word in given sentence so when this word is 
	 * called up, it shows in what context it was found (what 
	 * other words are around it)
	 * 
	 * @param i		index of current space character in given sentence
	 * @param sentence		the sentence
	 * @throws Exception
	 */
	private void createNewWordIndex(int i, String sentence) throws WordException {
		String crtWord, suffix, prefix;
		
		int nextSpaceIndex= sentence.substring(i+1,sentence.length()).indexOf(' ');
		if (nextSpaceIndex<0){
			nextSpaceIndex= sentence.length();
		} else {
			//compute correct index
			nextSpaceIndex += i +1;
		}
		
		if (i-prefixLen < 0){
			prefix= sentence.substring(0, i);
		} else {
			prefix= ".." +sentence.substring(i-prefixLen, i);
		}
		
		if (nextSpaceIndex == sentence.length()){//nothing after this nextSpaceIndex position
			suffix = "";
		} else {
			if (nextSpaceIndex+suffixLen+1 <= sentence.length()){
				suffix= sentence.substring(nextSpaceIndex+1, nextSpaceIndex+suffixLen+1) +"..";
			} else {
				suffix= sentence.substring(nextSpaceIndex+1, sentence.length());
			}
		}
		crtWord= sentence.substring(i+1, nextSpaceIndex);
		
		//this means we have multiple white spaces at consecutive locations so skip them 
		if (crtWord.length() < 1) return;
		
		add(crtWord, suffix, prefix, false);
	}
	

	/* (non-Javadoc)
	 * @see Dictionary#getNextNodeForPrefix(char, CharacterNode)
	 */
	public synchronized CharacterNodeWithContext getNextNodeForPrefix(char nextChar, CharacterNodeWithContext wn) {
		CharacterNodeWithContext returnNode=null;
		
		if (wn == null){
			returnNode= wordsMap.get(nextChar);
		} else {
			returnNode= wn.getChild(nextChar); 
		}
		
		//("Word Does not exist");
		if (returnNode==null) return  CharacterNodeWithContext.EMPTY;
		
		return returnNode; 
	}

	
	//@Override TESTING move this to test dict
	/** Same as @see Dictionary#getAllWordsStartingWith(char)
	 * but returns the results formatted. Thus this method is slower.
	 * 
	 * @param prefChar
	 * @return
	 */
	public synchronized List<String> getAllFormattedWordsStartingWith(char prefChar){
		List<String> allWords= new ArrayList<String>();
		getFormattedWords(""+prefChar, allWords, "", wordsMap.get(prefChar), false);
		return allWords;
	}
	

	/* (non-Javadoc)
	 * @see Dictionary#getAllWordsStartingWith(char)
	 */
	public synchronized List<String> getAllWordsStartingWith(char prefChar){
		List<String> allWords= new ArrayList<String>();
		getWords(allWords, "", wordsMap.get(prefChar), false);
		return allWords;
	}
	
	
	/**
	 * This method is slow and not recommended. It is here for convenience.
	 * The proper way is to send in one character at a time and use
	 * <I>getNextNodeForPrefix(..)</I>
	 * 
	 * Runtime: O(prefix.length) If this is called everytime a new char
	 * 			is added to prefix it will be O(n^2) 
	 * 
	 * @param pref	The prefix that matches all words returned
	 * @param size	The size of the list to return. Algorithm will stop
	 * 				and return when it found this many words
	 * @return 	Returns list of words which start with prefix pref
	 */
	public List<String> getWordsForPrefix(String pref, int size){
		CharacterNodeWithContext wn =getNextNodeForPrefix(pref.charAt(0), null);
		for (int i=1; i<pref.length(); i++){
			wn= getNextNodeForPrefix(pref.charAt(i), wn);
		}
		List<String> words = getWordsForPrefix(wn);
		if (words.size()>size){
			return words.subList(0, size);
		}
		return words;
	}
	
	
	/**
	 * Slower bec O(n) through the whole prefix
	 * 
	 * @param wn
	 * @return
	 */
	public List<String> getFormattedWordsForPrefix(String pref, int size){
		CharacterNodeWithContext wn =getNextNodeForPrefix(pref.charAt(0), null);
		
		for (int i=1; i<pref.length(); i++){
			wn= getNextNodeForPrefix(pref.charAt(i), wn);
		}
		
		List<String> words = getFormattedWordsForPrefix(wn);
		if (words.size()>size){
			return words.subList(0, size);
		}
		return words;
	}
	
	
	/* (non-Javadoc)
	 * @see Dictionary#getWordsForPrefix(CharacterNode)
	 */	
	//This is faster than the formatted one
	//BUT receiver must keep track of the prefix chars sent in so far
	//TODO observation: since there is no list size limit, can easily deduce
	//					next step words by removing all words from prev step which
	//					done't match current prefix: ie word_i.charAt[nextStep]!=wn.charAt(wn.length()-1]
	public synchronized List<String> getWordsForPrefix(CharacterNodeWithContext cn){
		if (cn==null) return Collections.emptyList();//EMPTY_LIST;

		List<String> allWords= new ArrayList<String>();
		getWords(allWords, "", cn, false);

		return allWords;
	}
	
	
	public synchronized List<String> getFormattedWordsForPrefix(CharacterNodeWithContext cn){
		if (cn==null) return Collections.emptyList();//EMPTY_LIST;
		
		List<String> allWords= new ArrayList<String>();
		String prefix= getPrefix(cn);
		getFormattedWords(prefix, allWords, "", cn, false);
//		getWords(        allWords, "", wn, false);
		return allWords;
	}

	
	/* (non-Javadoc)
	 * @see Dictionary#getAllWords()
	 */
	public synchronized List<String> getAllWords(){
		List<String> allWords= new ArrayList<String>();
		
		for (Map.Entry<Character, CharacterNodeWithContext> pair: wordsMap.entrySet()){
			getWords(allWords, "", wordsMap.get(pair.getValue()), true);
		}
//		for (Character c: startChars){//TODO replace w map.getKeySet and get its pair/value
//			getWords(allWords, "", wdMap.get(c), true);
//		}
		
		return allWords;
	}
	
	//for testing or w/e
	public synchronized List<String> getAllWordsWFakes(){
		List<String> allWords= new ArrayList<String>();

		for (Map.Entry<Character, CharacterNodeWithContext> pair: wordsMap.entrySet()){
			getWords(allWords, "", wordsMap.get(pair.getValue()), false);
		}
//		for (Character c: startChars){//TODO replace w map.getKeySet and get its pair/value
//			getWords(allWords, "", wdMap.get(c), false);
//		}
		
		return allWords;
	}
	
	//always returns original and nonoriginal words
//	private synchronized void getWords(List<CharacterNode> wordsList, CharacterNodeI wn) {
//		if (wn==null) return;
//		//if (original) if (!wn.isOriginal()) return;
//		
//		//if (!wn.hasChildren()){// ie is END
//		if (wn.isEnd()){
//			//if (crtWordChars.length() > 0){
//				wordsList.add(wn);
//			//}
//			return;
//		}
//		
//		//crtWordChars += wn.getWordChar();//gets crt char not word
//		wordsList.add(wn);
//		for (CharacterNodeI crtChild: wn.getChildren()){
//			getWords(wordsList, crtChild);
//		}
//		
//	}
	
	
	/**
	 * Traces up the tree from given wordNode to root (in map), 
	 * to produce a word out of this traversal.
	 * Time: O(n) 
	 */
	private String getPrefix(CharacterNode wordNode) {
		if (wordNode==null) return "";
		
		CharacterNode crt= wordNode;
		StringBuffer prefix=new StringBuffer();
		//TODO based on testing, see if adding chars to a linked list is better than
		//SB, then return end as head (bc list is reversed)
		
		prefix.insert(0,crt.getChar());
		
		//trace up the tree to find the entire current word for crt prefix
		while(crt.getParent()!=null){
			crt=crt.getParent();
			prefix.insert(0,crt.getChar());
		}
		
		return prefix.toString();
	}
	
	
	/* Returns all words stored in this dictionary  
	 * TODO Rewrite w/o recursion; use DFS and a stack instead
	 */
	//initially I wanted to see how it performs via RMI and multiple calls.
	//might not make sense to put a lock on it, but use a load balancer... will see
	private synchronized void getWords(List<String> wordsList, String crtWordChars, CharacterNodeWithContext wn, boolean original) {
		if (wn==null) return;
		if (original) if (!wn.isOriginal()) return;
		
		//if (!wn.hasChildren()){// ie is END
		if (wn.isEnd()){
			if (crtWordChars.length() > 0){
				wordsList.add(crtWordChars);
			}
			return;
		}
		
		
		crtWordChars += wn.getChar();//gets crt char not word
		
		for (CharacterNode crtChild: wn.getChildren()){
			getWords(wordsList, crtWordChars, (CharacterNodeWithContext)crtChild, original);
		}
		
	}
	
	
	
	/* Returns all words stored in this dictionary  
	 * TODO Rewrite w/o recursion; use DFS and a stack instead.
	 * 
	 * Actually this method is here for demonstration how it works. TODO Externalize to test dictionary.
	 */
	/**
	 * @PARAM prefix
	 * @PARAM wordsList
	 * @PARAM crtWordChars
	 * @PARAM wn
	 * @PARAM original
	 * 
	 */
	private synchronized void getFormattedWords(String prefix, List<String> wordsList, String crtWordChars, CharacterNodeWithContext wn, boolean original) {
		if (wn==null) return;
		if (original) if (!wn.isOriginal()) return;
		
		//if (!wn.hasChildren()){// ie is END
		if (wn.isEnd()){
			if (crtWordChars.length() > 0){
				if (wn.isOriginal()){
					//prefix=getPrefix(wn);
					//wordsList.add("<HTML><B>"+ prefix +"</B>" + crtWordChars +"</HTML>");
					wordsList.add("<font color='red'>"+ prefix +"</font>" + crtWordChars.substring(1,crtWordChars.length()));
				} else {
					StringBuffer result=new StringBuffer();
//					result.append("<HTML>");
					result.append(wn.getPrefixWord());
					result.append(' ');
					result.append("<font color='red'>");
					result.append(prefix);//matched prefix
					result.append("</font>");
					result.append(crtWordChars.substring(1,crtWordChars.length()));
					result.append(' ');
					result.append(wn.getSuffixWord() );
//					result.append("</HTML>");
					
					//wordsList.add(wn.getPrefixWord()+" "+PREFIX_DELIMITER+ crtWordChars +" "+ wn.getSuffixWord() );
					wordsList.add(result.toString());
					
				}
			}
			return;
		}
		
		
		crtWordChars += wn.getChar();
		
		for (CharacterNode crtChild: wn.getChildren()){
			getFormattedWords(prefix, wordsList, crtWordChars, (CharacterNodeWithContext)crtChild, original);
		}
		
	}
	
	
	/* (non-Javadoc)
	 * @see Dictionary#hasWord(java.lang.String)
	 */
	public synchronized boolean hasWord(String word){
		//retrieve root for all wds starting w char @ 0
		CharacterNodeWithContext wordNode =getNextNodeForPrefix(word.charAt(0), null);
		if (wordNode==null) return false;
		//TODO check isOriginal() status before returning is word exists
		
		//simulate typing 1 char at a time
		for (int i=1; i<word.length(); i++){
			wordNode= getNextNodeForPrefix(word.charAt(i), wordNode);
			if (wordNode==null) return false;
			if (!wordNode.isOriginal()) return false;
		}
		
		if (wordNode.hasEnd())//complete word to here?
			return true;
			
		return false;
	}

}
