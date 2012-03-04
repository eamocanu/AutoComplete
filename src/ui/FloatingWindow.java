/**
 * 
 */
package ui;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JWindow;
import javax.swing.Timer;




/**
 * @author Adrian Mocanu
 */
public class FloatingWindow extends JWindow{

	private int x=400;
	private int y=400;
	private int w=300;
	private int h=100;
	private int beginY,beginX;
	private JPanel jp;
	private JLabel jl;
	private JEditorPane jep;
	private JScrollPane jsp;
	//private Timer timer;
	
	private int mouseX,mouseY;
	private int offsetX=50;
	
	
	
	@Override
	public void toFront(){}
	
	public FloatingWindow() {
		init();
		//addListeners();		
		w=300;
		h=50;
		
	//	this.setLocation(x+100,y+offsetX);
//		this.setLocation(x,y);
		this.setVisible(true);
		setBounds(x,y,w,h);

		setFocusableWindowState(false);
		setVisible(false);
		setVisible(true);
		
	}

	/** Initialize all elements of this window*/
	private void init(){
		//addMoveTimer();
		jp=new JPanel();
		jl=new JLabel();
		jep=new JEditorPane();
		jsp=new JScrollPane();
		correctScrolling(jsp);
		jp.add(jl);
		//jsp.add(jp);
		jsp.setViewportView(jp);
		//jp.add(jep);
		//this.getContentPane().add(new JLabel("label"));
		this.getContentPane().add(jsp);
		
	}
	
	/** Add content to this window.
	 * 
	 * @param x 
	 * @param y
	 * 			where text should be put
	 * @param text The text to be added
	 * @param rows Height of text
	 * @param maxLength Width of text 
	 */ 
	public void setText(int x, int y, String text, int rows, int maxLength) {
		this.removeNotify();//no more auto repaint
		beginX=x;
		beginY=y;
		this.x=x;
		this.y=y;
		jl.setText(text);
		//jep.setText(text);
		this.setLocation(x+100,y+100);
		setFocusable(false);
		computeSize(rows, maxLength);
		
		setVisible(false);
		toBack();
		setVisible(true);
	}
	
	private void correctScrolling(JScrollPane pane) {
		pane.setMaximumSize(new Dimension(9, 9));
		pane.setMinimumSize(new Dimension(9, 9));
		pane.setPreferredSize(new Dimension(9, 9));
	}
	
	public void show(){
		super.show();
		//timer.restart();	
	}
	
//	private void addMoveTimer(){
//		ActionListener taskPerformer = new ActionListener() {
//		      public void actionPerformed(ActionEvent evt) {
//						Rectangle rect = getBounds();
//						if (rect.contains(mouseX, mouseY)) {
//							//mouse in
//							//setVisible(true);
//							timer.restart();
//						}
//						else{//mouse out
//							if (mouseX==beginX && mouseY==beginY)
//								setVisible(true); else setVisible(false);
//							timer.restart();
//						}
//		      }
//		};
//		//start timer
//		timer=new Timer(1000, taskPerformer);
//		timer.setRepeats(false);//disable auto repeat (should have left it on)
//	}

	/** Use this instead of the internal listener since the internal one makes MainWindow flash a lot*/
	public void setMouseCoords(int x, int y){
		mouseX=x;
		mouseY=y;
	}
	
	public Rectangle getBounds() {
		//System.out.println("boundaries in vars: "+ x+", "+y+", "+(x+w)+", "+(y+h));
		//System.out.println("actual boundaries: "+ mouseX+", "+mouseY+", "+(mouseX+w)+", "+(mouseY+h));
		return new Rectangle(x, y, w, h);
	}
	
	/**Compute new size of the window based on the number 
	 * of rows and the maximum length of the rows.
	 */
	private void computeSize(int rows, int maxLength){

		w=maxLength*8;
		h=rows*16+15;
		
		if (w>2*300)
			w=600;
		else
			if(w>300) w=300;
		if(h>200) h=200;
		if(w<100) w=100;
		if(h<60) h=60;
		
		this.setSize(w,h);

		this.addNotify();
		repaint();
		
	}
	
}
