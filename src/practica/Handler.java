package practica;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Handler extends Thread{

	private Socket socket;
    private BufferedReader entrada;
    private PrintWriter salida;
    
    private final int col=4;
    private int[]tarjeta;
    private int posicion;
    private ArrayList<String> registros = new ArrayList<String>();
    SimpleDateFormat formato2 = new SimpleDateFormat("kk:mm:ss");
    
    public Handler(Socket socket2){
    	this.socket = socket2;
    	try {
			this.entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.salida = new PrintWriter(new ObjectOutputStream(socket.getOutputStream()),true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        start();   	
	}
    
    
    @Override
    public void run() {
    	try {
    		//String tarjeta = generarTarjeta();
    		salida.println(generarTarjeta()+"\t,"+generarPosicion());
    		

                int numero = Integer.parseInt(entrada.readLine());
                System.out.println("Recibido: "+numero);
                if (comprobarPosicion(numero)) 
                {
                	if(!Multiserver.jasmap.containsKey(socket.getInetAddress())) 
                	{
                		Multiserver.jasmap.put(socket.getInetAddress(), numero);
                		//guardamos la hora de registro
                		registros.add(formato2.format(new Date().getTime()));
                		
                		//probamos con varios registros en el HashMap
                		//Multiserver.jasmap.put(InetAddress.getByName("192.255.255.1"), 123);
                		//registros.add(formato2.format(new Date().getTime()));
                		
                		//ahora tenemos que mandarlo a un fichero
                		guardarEnFichero(Multiserver.jasmap, registros);
                		
                		
                		System.out.println("Todo correcto, se ha registrado la ip "+socket.getInetAddress());
                		salida.println("Todo correcto, se ha registrado tu ip");
                	}else {
                		System.out.println("Ip doblada");
                		salida.println("Tu IP ya se ha registrado");
                	}
                }else {
                	salida.println("El numero no es correcto");
                	System.out.println("Numero erróneo");
                	}

                
                
            
            System.out.println("Cerrando Handler...");
        } catch (IOException e) {
            System.err.println("I/O Exception");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Socket no cerrado");
            }
        }
    }

    
	private void guardarEnFichero(HashMap<InetAddress, Integer> jasmap, ArrayList<String> registros) {
		
		String nombreFichero = Multiserver.asignatura+"_"+Multiserver.hora+".txt";
		/////////////////////////////////////////////////////////
		//Aquí pon la ruta donde quieras guardar el fichero txt//
		/////////////////////////////////////////////////////////
		String directorio = "/Users/jaliy/Desktop/ControlFaltas";
		
		String ruta_de_archivo = directorio+"/"+nombreFichero;
		BufferedWriter br = null;
		try {
			 br= new BufferedWriter(new FileWriter(ruta_de_archivo));
			String contenido = "";
			
			int contador = 0;
			for (InetAddress key : jasmap.keySet()) {
			    String ip = String.valueOf(key);
			    String hora = registros.get(contador);
			    contador++;
			    contenido += ip+"\t"+hora+"\n";
			}
			br.write(contenido);
			System.out.println("Añadidos los registros en la ruta "+ruta_de_archivo);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
	}


	private boolean comprobarPosicion(int numero) {
		boolean resultado = false;
		if(numero == tarjeta[posicion]) {
			resultado = true;
		}
		System.out.println("Comprobacion_Numero: "+resultado);
		return resultado;
	}

	private String generarPosicion() {
		posicion = (int) Math.floor(Math.random()*4);
		String cadena_posicion="Introduce el número de la posición: "+(posicion+1);
		return cadena_posicion;
	}

	private String generarTarjeta() {
		tarjeta= new int[col];
		String cadena_tarjeta = "";
		
		for (int i=0; i<col;i++)
		{
			tarjeta[i] = (int)(Math.random()*1000);
			cadena_tarjeta+=String.valueOf(tarjeta[i])+"\t";
		}
		return cadena_tarjeta;
	}

}
