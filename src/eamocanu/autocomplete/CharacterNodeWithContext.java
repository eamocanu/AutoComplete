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
public class CharacterNodeWithContext implements CharacterNode {
	
	public static final CharacterNodeWithContext END_NODE = new CharacterNodeWithContext(END);
	public static final CharacterNodeWithContext EMPTY = new CharacterNodeWithContext((char) 28);
	
	
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
	
	public DefaultMutableTreeNode tn;//so I can optimize by seeing ADT in a UI Tree :)
	public DefaultMutableTreeNode getUINode(){ return tn;}
	
	
	private static int id=0;
	private int crtId=0;
	
	public CharacterNodeWithContext(char wd, CharacterNode parent){
		this(wd);
		this.parent= parent;
	}
	
	public CharacterNodeWithContext(char wd){
		tn= new DefaultMutableTreeNode( this );
		crtId= id++;
		this.character= wd;
		if (wd==END) return;
//		children= new ArrayList<CharacterNode>();
		childrenMap= new HashMap<Character, CharacterNode>(3);
	}
	
	
	/* (non-Javadoc)
	 * @see eamocanu.autocomplete.CharacterNodeI#hasChildren()
	 */
	public boolean hasChildren(){
//		return children.size()>0;
		if (childrenMap == null) return false;
		return childrenMap.size()>0;
	}
	
	
	/* (non-Javadoc)
	 * @see eamocanu.autocomplete.CharacterNodeI#getChild(char)
	 */
	public CharacterNodeWithContext getChild(char childString){
		if (childrenMap == null) return (CharacterNodeWithContext) EMPTY;
		return (CharacterNodeWithContext)childrenMap.get(childString);
	}
	
	
	/* (non-Javadoc)
	 * @see eamocanu.autocomplete.CharacterNodeI#hasEnd()
	 */
	public boolean hasEnd(){
		if (childrenMap == null) return false;
		return childrenMap.containsKey(((CharacterNodeWithContext)END_NODE).character);//looks only at char data END
	}
	
	/* (non-Javadoc)
	 * @see eamocanu.autocomplete.CharacterNodeI#addChild(char)
	 */
	public void addChild(char child){
		CharacterNodeWithContext ch= new CharacterNodeWithContext(child);
		addChild(ch);
	}
	
	/* (non-Javadoc)
	 * @see eamocanu.autocomplete.CharacterNodeI#addChild(eamocanu.autocomplete.CharacterNode)
	 */
	public void addChild(CharacterNode child){
		CharacterNodeWithContext child2= (CharacterNodeWithContext)child;
		
		if (character==END) return; //END doesnt have children
		if (childrenMap.get(child) != null)
			return;
		
		tn.add(child2.tn);//for graphic visualization
		
//		children.add(child);
		childrenMap.put(child2.character, child);
		child2.parent=this;
	}
	
	
	/* (non-Javadoc)
	 * @see eamocanu.autocomplete.CharacterNodeI#getChildren()
	 */
	public List<CharacterNode> getChildren(){
		if (childrenMap == null) return Collections.emptyList();
		return new ArrayList<CharacterNode>(childrenMap.values());
	}
	
	
	/* (non-Javadoc)
	 * @see eamocanu.autocomplete.CharacterNodeI#getChar()
	 */
	public char getChar(){ return character; }
	
	
	@Override
	public boolean equals(Object that){
		if (!( that instanceof CharacterNodeWithContext ))
			return false;
		CharacterNodeWithContext thatWn=(CharacterNodeWithContext)that;
		return (thatWn.character == this.character);// && this!=END_NODE && thatWn!=END_NODE;
	}
	
	@Override
	public int hashCode(){
		return character;//.hashCode();
	}
	
	
	/* (non-Javadoc)
	 * @see eamocanu.autocomplete.CharacterNodeI#toString()
	 */
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
	
	
	/* (non-Javadoc)
	 * @see eamocanu.autocomplete.CharacterNodeI#getParent()
	 */
	public CharacterNode getParent(){ return parent; }

	
	
	@Override
	public void setParent(CharacterNode parent) {
		this.parent= parent;
	}

	@Override
	public boolean hasChild(char childCharacter) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
