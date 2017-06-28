package SplitFocusTest;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import javax.swing.JFrame;

import Shared.PressKey;
import Shared.Window;
import TUIO.TuioBlob;
import TUIO.TuioClient;
import TUIO.TuioCursor;
import TUIO.TuioListener;
import TUIO.TuioObject;
import TUIO.TuioTime;

public class Fenetre extends JFrame implements MouseListener, KeyListener, TuioListener {
	
	private static final long serialVersionUID = 1L;
	protected Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	protected int width, height;
	protected PressKey pk;
	protected TuioClient client;
	protected SplitFocusTestPanel sft;
	protected ServerSocket serveurSocket;
	protected Socket clientSocket;
	protected ObjectOutputStream oos;
	protected ObjectInputStream ois;
	protected boolean on, startThreads, menu; 
	protected volatile boolean moves, alive;
	protected InitThread it;
	protected Thread envoi, recevoir;
	
	public Fenetre(){
		
		//Titre de fen�tre
		this.setTitle("RolyPoly SplitFocus Test 0.17");
		
		//Taille de la fen�tre
		width = (int)screenSize.getWidth();
		height = (int)screenSize.getHeight();
		this.setSize(width, height);

		//Plein �cran
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setUndecorated(true);
		
		//Action � la fermeture
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		sft = new SplitFocusTestPanel(this.screenSize);
		this.setContentPane(sft);
		
		//Entr�es clavier
		pk = new PressKey();
		this.addKeyListener(pk);
		this.addKeyListener(this);
		
		//Entr�es souris
		this.addMouseListener(this);
		
		//Entr�es touch
		client = new TuioClient();
		client.addTuioListener(this);
		client.connect();
		
		this.on = false;
		this.startThreads = true;
		
		this.it = new InitThread(this);
		this.it.start();
		initThreads();
	}	
	
	public void go(){
		while(true){
			menu = sft.getMenu();
			if(!menu && startThreads){
				envoi.start();
				startThreads = false;
			}
			sft.repaint();
			this.moves = this.moves();
		}
	}
	
	public boolean moves(){
		return(sft.moves());
	}
	
	public void initThreads(){
		this.envoi = new Thread(new Runnable() {
			String control = "SplitView";
			boolean ctrl = true; 
			public void run(){
				while(true){
					try{
						if(moves){
							control = "SplitView";
						}
						oos.writeBoolean(ctrl);
						oos.flush();
						oos.reset();
						if(control == "SplitView"){
							oos.writeObject(sft.getWindows());
							oos.flush();
							oos.reset();
						}
						oos.writeBoolean(sft.getAlive());
						oos.flush();
						oos.reset();
					} catch(NullPointerException e){
						//System.out.println("Rien n'est envoy�.");
					} catch(SocketException e){
						System.out.println("D�connexion");
						break;
					} catch(IOException e){
						e.printStackTrace();
					}
				}	
			}
		});
//		this.recevoir = new Thread(new Runnable() {
//			ArrayList<Window> windows, tempW;
//			boolean alive = true;
//			String control = "SplitView";
//			@Override
//			public void run() {
//				windows = sft.getWindows();
//				while(alive){
//					try {
//						control = ois.readUTF();
//						if(control == "MainView"){
//							tempW = (ArrayList<Window>)ois.readObject();
//							if(tempW != null){
//								windows = tempW;
//								sft.updateWindows(windows);
//							}
//						}
//						sft.setAlive(alive);
//					} catch (NullPointerException e){
//						//System.out.println("Rien n'a �t� re�u.");
//					} catch (SocketException e) {
//						System.out.println("Syst�me d�connect�.");
//					} catch (IOException e){
//						e.printStackTrace();
//					} catch (ClassNotFoundException e){
//						e.printStackTrace();
//					}
//				}
//				try{
//					System.out.println("Serveur d�connect�");
//					ois.close();
//					clientSocket.close();
//				} catch(IOException e){
//					e.printStackTrace();
//				}
//			}
//		});
	}
	
	public void addTuioCursor(TuioCursor tc) {
		sft.addTuioCursor(tc);
	}
	
	public void removeTuioCursor(TuioCursor tc) {
		sft.removeTuioCursor(tc);
	}

	public void mouseClicked(MouseEvent e) {
		sft.mouseClicked(e);		
	}
	
	public void keyPressed(KeyEvent ke) {
		sft.keyPressed(ke);
	}
	
	public void mousePressed(MouseEvent e) {
		sft.mousePressed(e);
	}
	
	public void mouseReleased(MouseEvent e) {
		sft.mouseReleased(e);
	}
	
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
	public void addTuioBlob(TuioBlob t) {}
	public void addTuioObject(TuioObject t) {}
	public void refresh(TuioTime t) {}
	public void removeTuioBlob(TuioBlob t) {}
	public void removeTuioObject(TuioObject t) {}
	public void updateTuioBlob(TuioBlob t) {}
	public void updateTuioCursor(TuioCursor t) {}
	public void updateTuioObject(TuioObject t) {}

}
