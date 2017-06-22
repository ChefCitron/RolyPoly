package SplitFocusTest;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class InitThread extends Thread {

	protected String type;
	protected ServerSocket serveurSocket;
	protected Socket clientSocket;
	protected Fenetre fenetre;
	
	public InitThread(Fenetre fen){
		this.fenetre = fen;
	}
	
	@Override
	public void run() {
		if(!fenetre.on){
			try{
				fenetre.serveurSocket = new ServerSocket(4242);
				fenetre.clientSocket = fenetre.serveurSocket.accept();
				fenetre.oos = new ObjectOutputStream(new BufferedOutputStream(fenetre.clientSocket.getOutputStream()));
				System.out.println("Connexion OK");
				fenetre.on = true;
			} catch(IOException e){
				e.printStackTrace();
			}
			fenetre.sft.setConnect(true);

		}
		this.interrupt();
	}

}