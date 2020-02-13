package practica;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

public class Multiserver {
	
	public static Scanner sc = new Scanner(System.in);
	static final int PORT = 1119;
	public static HashMap<InetAddress,Integer> jasmap;
	static String asignatura;
	static String hora;
	static SimpleDateFormat formato1 = new SimpleDateFormat("dd_MM_yyyy_kk_mm");
	
	public static void main(String[]args) {
		ServerSocket s;
		Socket socket;
		hora = formato1.format(new Date().getTime());
		
		
		jasmap = new HashMap<InetAddress,Integer>();
		System.out.print("Asignatura: ");
		asignatura = sc.nextLine();
		System.out.println();

		System.out.println("*****SERVER LEVANTADO*****");
		
		try {
			s = new ServerSocket(PORT);
			while (true) {
				socket = s.accept();
				
				new Handler(socket);
			}  
		} catch (IOException ex) {
			ex.printStackTrace();
		}	
	}
}