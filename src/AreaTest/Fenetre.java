package AreaTest;

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
	protected AreaTestPanel mzt;
	
	public Fenetre(){
		//Titre de fen�tre
		this.setTitle("RolyPoly Area Test 1.0");
		
		//Taille de la fen�tre
		width = (int)screenSize.getWidth();
		height = (int)screenSize.getHeight();
		this.setSize(width, height);

		//Plein �cran
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setUndecorated(true);
		
		//Action � la fermeture
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		mzt = new AreaTestPanel(this.screenSize);
		this.setContentPane(mzt);
		
		//Entr�es clavier
		pk = new PressKey();
		this.addKeyListener(pk);
		this.addMouseListener(this);
		this.addKeyListener(this);
	}	
	
	public void go(){
		while(true){
			mzt.repaint();
		}
	}

	public void mouseClicked(MouseEvent e) {
		mzt.mouseClicked(e);		
	}
	
	public void keyPressed(KeyEvent ke) {
		mzt.keyPressed(ke);
	}
	
	public void mousePressed(MouseEvent e) {
		mzt.mousePressed(e);
	}
	
	public void mouseReleased(MouseEvent e) {
		mzt.mouseReleased(e);
	}
	
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}

}
