package eamocanu.autocomplete;

import java.util.List;

public interface CharacterNode {

	public static final char END = 11;
//	public static final CharacterNode END_NODE = new CharacterNodeWithContext(END);
//	public static final CharacterNode EMPTY = new CharacterNodeWithContext((char) 28);

	
	public boolean hasChild(char childCharacter);
	public abstract boolean hasChildren();

	public abstract CharacterNode getChild(char childForCharacter);

	
	/** @return true if this character node marks end of a word
	 * 			false otherwise (if this is not end of word)
	 */
	public abstract boolean hasEnd();

	public abstract void addChild(char child);

	public abstract void addChild(CharacterNode child);

	public abstract List<CharacterNode> getChildren();

	public abstract char getChar();

	public abstract String toString();

	public abstract CharacterNode getParent();
	
	public abstract void setParent(CharacterNode parent);
	
	public abstract boolean isEnd();
	
}