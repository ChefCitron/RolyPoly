package DualScreenTest;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class InitThread extends Thread {

	protected String type;
	protected ServerSocket serveurSocket;
	protected Socket clientSocket;
	protected Fenetre fenetre;
	
	public InitThread(String type, Fenetre fen){
		this.type = type;
		this.fenetre = fen;
	}
	
	@Override
	public void run() {
		if(this.type.equals("Serveur") && !fenetre.on){
			try{
				System.out.println("Non connect�.");
				fenetre.serveurSocket = new ServerSocket(4242);
				fenetre.clientSocket = fenetre.serveurSocket.accept();
				fenetre.oos = new ObjectOutputStream(new BufferedOutputStream(fenetre.clientSocket.getOutputStream()));
				System.out.println("Connexion OK");
				fenetre.on = true;
			} catch(IOException e){
				e.printStackTrace();
			}
			fenetre.dst.setConnect(true);
			try {
				fenetre.oos.writeUTF("Test envoi � "+System.currentTimeMillis());
				fenetre.oos.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (NullPointerException e){
				
			}

		}
		
		else if(this.type.equals("Client") && !fenetre.on){
			try{
				System.out.println(fenetre);
				fenetre.clientSocket = new Socket("141.115.72.18", 4242);
				fenetre.ois = new ObjectInputStream(new BufferedInputStream(fenetre.clientSocket.getInputStream()));
				System.out.println("Connexion OK");
				fenetre.on = true;
			} catch(IOException e){
				e.printStackTrace();
			}
			fenetre.dst.setConnect(true);
			
			try {
				System.out.println(fenetre.ois.readUTF());
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (NullPointerException e){
				
			}

		} 	
	}

}
