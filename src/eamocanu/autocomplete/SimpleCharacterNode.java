/**
 * 
 */
package eamocanu.autocomplete;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Adrian
 *
 */
public class SimpleCharacterNode implements CharacterNode {
	public static final CharacterNode END_NODE = new SimpleCharacterNode(END);
	public static final CharacterNode EMPTY = new SimpleCharacterNode((char) 28);
	
	
	private char character;
	
	//rank of this
	private int rank=0;
	
	private Map<Character, CharacterNode> childrenMap =null;
	private CharacterNode parent=null;
	
	//for debug
	private static int id=0;
	private int crtId=0;
	
	
	
	/**
	 * 
	 */
	public SimpleCharacterNode(Character data) {
		this.character= data;
		crtId= id++;
		
		if (data==END) return;
		
		childrenMap= new HashMap<Character, CharacterNode>(4);
	}

	
	public SimpleCharacterNode(Character data, int depth) {
		this.character= data;
		crtId= id++;
		
		if (data==END) return;
		
		//allocate children space based on observations/heuristics
		int numChildren=10;
		
		if (depth>3){
			numChildren=2;
		}
		if (depth>6){
			numChildren=1;
		}
		childrenMap= new HashMap<Character, CharacterNode>(numChildren);
	}
	

	@Override
	public void addChild(char child) {
		CharacterNode ch= new SimpleCharacterNode(child);
		addChild(ch);
	}


	@Override
	public void addChild(CharacterNode child) {
		
		if (character==END) return; //END doesnt have children
		if (childrenMap.get(child) != null)
			return;
		
		childrenMap.put(child.getChar(), child);
		child.setParent(this);
	}


	@Override
	public char getChar() { return character; }


	@Override
	public CharacterNode getChild(char childString) {
		if (childrenMap == null) return EMPTY;
		return childrenMap.get(childString);
	}


	public boolean hasChild(char childCharacter){
		return childrenMap.containsKey(childCharacter);
	}
	
	
	@Override
	public List<CharacterNode> getChildren() {
		if (childrenMap == null) return Collections.emptyList();
		return new ArrayList<CharacterNode>(childrenMap.values());
	}


	@Override
	public CharacterNode getParent() {
		 return parent;
	}


	@Override
	public boolean hasChildren() {
		if (childrenMap == null) return false;
		return childrenMap.size()>0;
	}


	@Override
	public boolean hasEnd() {
		if (childrenMap == null) return false;
		return childrenMap.containsKey(END_NODE.getChar());//looks only at char data END
	}


	
	@Override
	public void setParent(CharacterNode parent) {
		this.parent= parent;
	}
	
	public String toString(){	return ""+character +":"+crtId;	}
	public boolean isEnd(){ return character==END; }
	
	
	@Override
	public boolean equals(Object that){
		if (!( that instanceof SimpleCharacterNode))
			return false;
		SimpleCharacterNode thatWordNode=(SimpleCharacterNode)that;
		return (thatWordNode.character == this.character);// && this!=END_NODE && thatWn!=END_NODE;
	}
	
	@Override
	public int hashCode(){
		return character;//.hashCode();
	}
	
	
//	public int getDepth(){ return level;}
}
