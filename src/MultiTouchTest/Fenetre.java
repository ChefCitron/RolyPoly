package MultiTouchTest;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;

import Shared.PressKey;

public class Fenetre extends JFrame implements MouseListener, KeyListener {
	
	private static final long serialVersionUID = 1L;
	protected Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	protected int width, height;
	protected PressKey pk;
	protected MultiTouchTestPanel mtt;
	
	public Fenetre(){
		//Titre de fen�tre
		this.setTitle("RolyPoly MultiTouch Test 0.1");
		
		//Taille de la fen�tre
		width = (int)screenSize.getWidth();
		height = (int)screenSize.getHeight();
		this.setSize(width, height);

		//Plein �cran
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setUndecorated(true);
		
		//Action � la fermeture
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		mtt = new MultiTouchTestPanel(this.screenSize);
		this.setContentPane(mtt);
		
		//Entr�es clavier
		pk = new PressKey();
		this.addKeyListener(pk);
		this.addMouseListener(this);
		this.addKeyListener(this);
	}	
	
	public void go(){
		while(true){
			mtt.repaint();
		}
	}

	public void mouseClicked(MouseEvent e) {
		mtt.mouseClicked(e);		
	}
	
	public void keyPressed(KeyEvent ke) {
		mtt.keyPressed(ke);
	}
	
	public void mousePressed(MouseEvent e) {
		mtt.mousePressed(e);
	}
	
	public void mouseReleased(MouseEvent e) {
		mtt.mouseReleased(e);
	}
	
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}

}
