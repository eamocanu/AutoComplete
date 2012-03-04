package eamocanu.autocomplete;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

/**
 * 
 */

/**
 * @author Adrian
 * inita lot of variables to null; finals; statics; 
 * need remove Tree nodes; yuck
 */
public class CharacterNode {
	//public static final CharacterNode END= new CharacterNode(' ');//this implies words cannot contain words
	public static final char END= 11;
	public static final CharacterNode END_NODE= new CharacterNode(END);
	public static final CharacterNode EMPTY= new CharacterNode((char)28);
	
	//the character stored in this node
	private char character;
	
	//rank of this
	private int rank=0;
	
	//store children nodes as both map and in list
	//private List<CharacterNode> children;
	private Map<Character, CharacterNode> childrenMap =null;
	CharacterNode parent=null;
	
	//Some words maybe be part of a sentence separated w space
	//such words when looked up are shown in context with one
	//one before them (prefix) and one word after (suffix)
//	private String suffix=null;//"";//replace these 2 strs w pointers to node up and down the branch
//	private String prefix=null;//"";//that way user could move the cursor and load more of the prefix or suffix
	private boolean original;
	private char[] suffix=null;
	private char[] prefix=null;
	
	public DefaultMutableTreeNode tn;//so I can optimizr by seeing ADT in a UI Tree :)
	
	
	private static int id=0;
	private int crtId=0;
	
	public CharacterNode(char wd, CharacterNode parent){
		this(wd);
		this.parent= parent;
	}
	
	public CharacterNode(char wd){
		tn= new DefaultMutableTreeNode( this );
		crtId= id++;
		this.character= wd;
		if (wd==END) return;
//		children= new ArrayList<CharacterNode>();
		childrenMap= new HashMap<Character, CharacterNode>(3);
	}
	
	
	public boolean hasChildren(){
//		return children.size()>0;
		if (childrenMap == null) return false;
		return childrenMap.size()>0;
	}
	
	
	public CharacterNode getChild(char childString){
		if (childrenMap == null) return EMPTY;
		return childrenMap.get(childString);
	}
	
	
	/** @return true if this character node marks end of a word
	 * 			false otherwise (if this is not end of word)
	 */
	public boolean hasEnd(){
		if (childrenMap == null) return false;
		return childrenMap.containsKey(END_NODE.character);//looks only at char data END
	}
	
	public void addChild(char child){
		CharacterNode ch= new CharacterNode(child);
		addChild(ch);
	}
	
	public void addChild(CharacterNode child){
		if (character==END) return; //END doesnt have children
		if (childrenMap.get(child) != null)
			return;
		
		tn.add(child.tn);//for graphic visualization
		
//		children.add(child);
		childrenMap.put(child.character, child);
		child.parent=this;
	}
	
	
	public List<CharacterNode> getChildren(){
		if (childrenMap == null) return Collections.emptyList();
		return new ArrayList<CharacterNode>(childrenMap.values());
	}
	
	
	public char getChar(){ return character; }
	
	
	@Override
	public boolean equals(Object that){
		if (!( that instanceof CharacterNode ))
			return false;
		CharacterNode thatWn=(CharacterNode)that;
		return (thatWn.character == this.character);// && this!=END_NODE && thatWn!=END_NODE;
	}
	
	@Override
	public int hashCode(){
		return character;//.hashCode();
	}
	
	
	@Override
	public String toString(){	return ""+character +":"+crtId +" "+original;	}


	public String getSuffixWord() {
		if (suffix==null) return "";
		return new String(suffix);
//		return suffix;
//		return "";
	}


	public String getPrefixWord() {
		if (prefix==null) return "";
//		return suffix;
		return new String(prefix);
//		return "";
	}
	
	public void setPrefixWord(String prefix){
		if (prefix.length()>0){
			this.prefix = new char[prefix.length()];
			byte [] b= prefix.getBytes();
			for (int i=0; i<b.length; i++){
				this.prefix[i]=(char)b[i];
			}
		} else this.prefix=null;
//		this.prefix=prefix; 
	}
	public void setSuffixWord(String suffix){
		if (suffix.length()>0){
			this.suffix = new char[suffix.length()];
			byte [] b= suffix.getBytes();
			for (int i=0; i<b.length; i++){
				this.suffix[i]=(char)b[i];
			}
		} else this.suffix=null;
//		this.suffix=suffix; 
	}
	
	public boolean isEnd(){ return character==END; }
	public void setOriginalWord(boolean status){original=status;}
	public boolean isOriginal(){ return original;}
	
	public CharacterNode getParent(){ return parent; }
	
}
