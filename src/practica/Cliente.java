package practica;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Cliente extends Thread{
	
	Scanner sc = new Scanner(System.in);
	private Socket socket;
	private BufferedReader entrada;
	private PrintWriter salida;
	private static int PORT = 1119;
	
	public Cliente() {
		System.out.println("Creando cliente...");
		try {
			socket = new Socket(InetAddress.getByName("192.168.56.1"), Cliente.PORT);
		}catch(IOException e) {
			System.err.println("ERROR: Fallo creacion socket");
		}
		
		
		try {
			entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			salida = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
			
			start();
		}catch(IOException e) {
			System.err.println(e.getMessage());
			try {
				socket.close();
			}catch(IOException e2){
				System.err.println("ERROR: socket no cerrado");
			}
		}
	}
	
	
	public void run() {
		try {

		System.out.println(entrada.readLine());
		int numero = sc.nextInt();
		salida.println(numero);
	
		System.out.println(entrada.readLine());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				socket.close();
			}catch(IOException e2) {
				System.err.println("ERROR: socket no cerrado");
			}
		}
	}


	public Socket getSocket() {
		return socket;
	}


	public void setSocket(Socket socket) {
		this.socket = socket;
	}
}