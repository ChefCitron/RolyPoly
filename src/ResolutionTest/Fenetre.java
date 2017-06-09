package ResolutionTest;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class Fenetre extends JFrame {
	
	private static final long serialVersionUID = 1L;
	protected Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	protected int width, height;
	protected PressKey pk;
	
	public Fenetre(){
		//Titre de fen�tre
		this.setTitle("RolyPoly Resolution Test 0.3");
		
		//Taille de la fen�tre
		width = (int)screenSize.getWidth();
		height = (int)screenSize.getHeight();
		this.setSize(width, height);

		//Plein �cran
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setUndecorated(true);
		
		//Action � la fermeture
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		ResolutionTestPanel rtp = new ResolutionTestPanel(this.screenSize);
		this.setContentPane(rtp);
		
		//Entr�es clavier
		pk = new PressKey();
		this.addKeyListener(pk);

	}	

}
