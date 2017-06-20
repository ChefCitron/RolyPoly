package DualScreenTest;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import javax.swing.JFrame;

import Shared.PressKey;
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
	protected DualScreenTestPanel dst;
	protected TuioClient client;
	protected String type;
	protected ServerSocket serveurSocket;
	protected Socket clientSocket;
	protected PrintWriter out;
	protected BufferedReader in;
	protected Thread envoi, recevoir;
	protected ObjectOutputStream oos;
	protected ObjectInputStream ois;
	protected boolean menu, startThreads;
	
	public Fenetre(){
		
		//Titre de fen�tre
		this.setTitle("RolyPoly DualScreen Test 1.0");
		
		this.type = "Serveur";
		
		//Taille de la fen�tre
		width = (int)screenSize.getWidth();
		height = (int)screenSize.getHeight();
		this.setSize(width, height);

		//Plein �cran
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setUndecorated(true);
		
		//Action � la fermeture
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		dst = new DualScreenTestPanel(this.screenSize, this.type);
		this.setContentPane(dst);
		this.startThreads = true;
		
		//Entr�es clavier
		pk = new PressKey();
		this.addKeyListener(pk);
		this.addMouseListener(this);
		this.addKeyListener(this);
		
	}	
	
	public void go(){
		menu = dst.getMenu();
		initThreads();
		while(true){
			menu = dst.getMenu();
			if(!menu && startThreads){
				if(this.type.equals("Serveur")){
					envoi.start();
				}
				else{
					recevoir.start();
				}
				startThreads = false;
			}
			dst.repaint();
		}
	}
	
	public void initThreads(){
		if(this.type.equals("Serveur")){
			client = new TuioClient();
			client.addTuioListener(this);
			client.connect();
			try{
				serveurSocket = new ServerSocket(4242);
				clientSocket = serveurSocket.accept();
				oos = new ObjectOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));
				System.out.println("Connexion OK");
			} catch(IOException e){
				e.printStackTrace();
			}
			dst.setConnect(true);
			try {
				oos.writeUTF("Test envoi � "+System.currentTimeMillis());
				oos.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			this.envoi = new Thread(new Runnable() {
				public void run() {
					while(true){
						try{
							oos.writeObject(dst.getCar());
							oos.flush();
							oos.reset();
							oos.writeObject(dst.getMeteors());
							oos.flush();
							oos.reset();
						} catch(NullPointerException e){
							System.out.println("Rien n'est envoy�.");
						} catch(IOException e){
							e.printStackTrace();
						}
					}	
				}
			});
		}
		
		else{
			try{
				clientSocket = new Socket("141.115.72.18", 4242);
				ois = new ObjectInputStream(new BufferedInputStream(clientSocket.getInputStream()));
			} catch(IOException e){
				e.printStackTrace();
			}
			dst.setConnect(true);
			System.out.println("ois : "+ois);
			System.out.println("Connexion OK");
			try {
				System.out.println(ois.readUTF());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			this.recevoir = new Thread(new Runnable() {
				Coordinates c;
				ArrayList<Meteor> m;
				@Override
				public void run() {
					try {						
						c = (Coordinates)ois.readObject();
						
						while(c!=null){
							c = (Coordinates)ois.readObject();
							dst.updateCoordinates(c);
							m = (ArrayList<Meteor>)ois.readObject();
							dst.updateMeteors(m);
						}
						System.out.println("Serveur d�connect�");
						ois.close();
						clientSocket.close();
					} catch (NullPointerException e){
						System.out.println("Rien n'a �t� re�u.");
					} catch (SocketException e) {
						System.out.println("Syst�me d�connect�.");
					} catch (IOException e){
						e.printStackTrace();
					} catch (ClassNotFoundException e){
						e.printStackTrace();
					}
				}
			});
		} 	
	}
	
	public void addTuioCursor(TuioCursor tc) {
		dst.addTuioCursor(tc);
	}
	
	public void removeTuioCursor(TuioCursor tc) {
		dst.removeTuioCursor(tc);
	}

	public void mouseClicked(MouseEvent e) {
		dst.mouseClicked(e);		
	}
	
	public void keyPressed(KeyEvent ke) {
		if(ke.getKeyCode() == ke.VK_ESCAPE){
			System.out.println("Fermeture du programme.");
			client.disconnect();
			try {
				oos.close();
				ois.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Frame[] frames = JFrame.getFrames();
			frames[0].dispatchEvent(new WindowEvent(frames[0], WindowEvent.WINDOW_CLOSING));
		}
	}
	
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
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
