/**
 * 
 */
package eamocanu.autocomplete;

import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Adrian
 *
 */
public class CondensedCharacterNode /*implements CharacterNode*/ {
	public static final char END = 11;
	public static final CondensedCharacterNode END_NODE = new CondensedCharacterNode(new StringBuffer(END));
	public static final CondensedCharacterNode EMPTY = new CondensedCharacterNode( new StringBuffer((char) 28));
	
	
	private CharSequence chars;
	
	//rank of this
	private int rank=0;
	
	private Map<Character, CondensedCharacterNode> childrenMap =null;
	private CondensedCharacterNode parent=null;
	
	//for debug
	private static int id=0;
	private int crtId=0;
	
	
	
	/**
	 * 
	 */
	public CondensedCharacterNode(CharSequence data) {
		this.chars= data;
		crtId= id++;
		
		if (chars.charAt(0)==END) return;
		
		childrenMap= new HashMap<Character, CondensedCharacterNode>(4);
	}

	
	public CondensedCharacterNode(CharSequence data, int depth) {
		this.chars= data;
		crtId= id++;
		
		if (chars.charAt(0)==END) return;
		
		//allocate children space based on observations/heuristics
		int numChildren=10;
		
		if (depth>3){
			numChildren=2;
		}
		if (depth>6){
			numChildren=1;
		}
		childrenMap= new HashMap<Character, CondensedCharacterNode>(numChildren);
	}
	

	
	public void addChild(CharSequence child) {
		CondensedCharacterNode ch= new CondensedCharacterNode(child);
		addChild(ch);
	}


	
	public void addChild(CondensedCharacterNode child) {
		
		if (character==END) return; //END doesnt have children
		if (childrenMap.get(child) != null)
			return;
		
		childrenMap.put(child.getChar(), child);
		child.setParent(this);
	}


	
	public CharSequence getChar() { return chars; }


	
	public CondensedCharacterNode getChild(char childString) {
		if (childrenMap == null) return EMPTY;
		return childrenMap.get(childString);
	}


	public boolean hasChild(char childCharacter){
		return childrenMap.containsKey(childCharacter);
	}
	
	
	
	public List<CondensedCharacterNode> getChildren() {
		if (childrenMap == null) return Collections.emptyList();
		return new ArrayList<CondensedCharacterNode>(childrenMap.values());
	}


	
	public CondensedCharacterNode getParent() {
		 return parent;
	}


	
	public boolean hasChildren() {
		if (childrenMap == null) return false;
		return childrenMap.size()>0;
	}


	public boolean hasEnd() {
		if (childrenMap == null) return false;
		return childrenMap.containsKey(END_NODE.getChar());//looks only at char data END
	}


	
	public void setParent(CondensedCharacterNode parent) {
		this.parent= parent;
	}
	
	public String toString(){	return ""+character +":"+crtId;	}
	public boolean isEnd(){ return character==END; }
	
	
	@Override
	public boolean equals(Object that){
		if (!( that instanceof CondensedCharacterNode))
			return false;
		CondensedCharacterNode thatWordNode=(CondensedCharacterNode)that;
		return (thatWordNode.character == this.character);// && this!=END_NODE && thatWn!=END_NODE;
	}
	
	@Override
	public int hashCode(){
		return character;//.hashCode();
	}
	
	
//	public int getDepth(){ return level;}
}
