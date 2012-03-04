/**
 * 
 */
package ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import eamocanu.autocomplete.CharacterNode;
import eamocanu.autocomplete.DictionaryWithContext;


/**
 * @author Adrian
 *
 */
public class UI extends JFrame {

	private JPanel mainPanel;
	private JTextField inputField;
	private DictionaryWithContext d;
	
	
	
	/**
	 * 
	 */
	public UI(DictionaryWithContext d) {
		this.d=d;
		setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        setTitle(" Dictionary word lookup ");
		init();
		addListeners();
		setSize(500,500);
	}
	
	
	private void init() {
		mainPanel = new JPanel();
		getContentPane().add(mainPanel);
		
		inputField= new JTextField(20);
		mainPanel.add(inputField);
		
	}
	
	
	private void addListeners() {
		inputField.addKeyListener(
				
			    new KeyListener() {
					List<String> words;
					CharacterNode wn;
					
					@Override
					public void keyPressed(KeyEvent arg0) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void keyReleased(KeyEvent arg0) {
						String s= inputField.getText();
						
						if (s.length() < 1) return;
						
						//put this on new thread and add to requestQueue each letter
						//then when this method is called again if request still in queue
						//cancel it and add a new one w one more char (this should speed up
						//things on both sides
						words= d.getFormattedWordsForPrefix(s,55);
						
						
						//add the words to the UI
						//sort by rating based on END of word.thats where rating is kept
						FloatingWindow fw= new FloatingWindow();
						//fw.setAlwaysOnTop(true);
						
						StringBuffer text=new StringBuffer("<html>");
						for (String wd: words){
//							System.out.println(wd);
//							text += wd+"<BR>" ;
							text.append(wd);
							text.append("<BR>");
						}						
						text.append("</html>");
						
						fw.setText(inputField.getX(), inputField.getY(), text.toString(), 10, 40);
						fw.show();
					}

					@Override
					public void keyTyped(KeyEvent arg0) {
				
						
					}
			    });
	}

}
