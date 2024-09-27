package multiserver.tcp;

import java.net.*;
import java.io.*;



public class TCPMultiServer {

    //variables compartidas
    boolean listening = true;
    TCPControladorHilo controlador;
	
    public void ejecutar() throws IOException {
        ServerSocket serverSocket = null;
        controlador = new TCPControladorHilo();
        controlador.start();

        try {
            serverSocket = new ServerSocket(4444);
        } catch (IOException e) {
            System.err.println("No se puede abrir el puerto: 4444.");
            System.exit(1);
        }
        
        System.out.println("Puerto abierto: 4444.");

        while (listening) {
        	
            TCPServerHilo hilo = new TCPServerHilo(serverSocket.accept());
            controlador.añadirHilo(hilo);
            hilo.start();
        }

        serverSocket.close();
    }
    
    public static void main(String[] args) throws IOException {
    	
    	TCPMultiServer tms = new TCPMultiServer();
    	tms.ejecutar();
    	
    }
}
