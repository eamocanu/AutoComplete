package eamocanu.autocomplete;
import java.util.List;

/**
 * Provides auto complete and prefix services 
 * 
 * @author Adrian
 *
 */
public interface Dictionary {

	public static final String PREFIX_DELIMITER=""+(char)11;
	
	
	/** Add a word to dictionary
	 * 
	 * @param word		The word to add
	 * @throws Exception	If the word contains space
	 */
	public abstract void addWordSentence(String word) throws Exception;

	
	/** Given a wordNode and a next character it returns a character node, 
	 * if it exists in dictionary (of currently looked for word)
	 * In other words, in the tree of words, it looks for an edge labeled 
	 * nextChar from node wordNode, and returns the node the edge links to.
	 * 
	 * If wordNode is null returns root node of all words starting with nextChar
	 * If no children of wordNode match nextChar null, is returned
	 * 
	 * Includes non original words
	 * 
	 * @param wordNode	the current word node to look at
	 * @param nexTchar	the char to see if wordNode children contains
	 */
	public abstract CharacterNode getNextNodeForPrefix(char nextChar,
			CharacterNode wordNode);
	

	/** Traverses dictionary tree and concatenates character nodes into
	 * entire words.
	 * 
	 *  @return		all words starting with prefChar */
	public abstract List<String> getAllWordsStartingWith(char prefChar);

	
	/** Looks for children starting at wordNode node and returns all combinations
	 * thus creating suffixes for wordNode
	 * 
	 * Includes non original words
	 * 
	 * @param wordNode	the character node to start at
	 * @return	list of all words starting from wordNode 
	 */
	public abstract List<String> getWordsForPrefix(CharacterNode wordNode);

	
	/** Includes ONLY original words
	 * @return	All words stored in dictionary
	 */
	public abstract List<String> getAllWords();

	
	/** Checks to see if a given word is in dictionary
	 * Includes ONLY original words
	 * 
	 * @param word	the word to look in this dictionary
	 * @return	true if word wd is in dictionary
	 */
	public abstract boolean hasWord(String word);
	

	/**
	 * This method is slow and not recommended. It is here for convenience.
	 * The proper way is to send in one character at a time and use
	 * <I>getNextNodeForPrefix(..)</I>
	 * 
	 * Runtime: O(prefix.length). If this is called every time a new char
	 * 			is added to prefix it will be O(n^2) 
	 * 
	 * @param pref	The prefix that matches all words returned
	 * @return 	Returns list of words which start with prefix pref
	 */
	public List<String> getWordsForPrefix(String pref);

	//TODO move to test interface
	List<String> getAllFormattedWordsStartingWith(char prefChar);

}