package multiserver.tcp;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class TCPMultiServer {

    //variables compartidas
    boolean listening = true;
    //TCPControladorHilo controlador;
    public List<String> enLinea = new ArrayList<>();
    
    
    public void ejecutar() throws IOException {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(4444);
        } catch (IOException e) {
            System.err.println("No se puede abrir el puerto: 4444.");
            System.exit(1);
        }
        
        System.out.println("Puerto abierto: 4444.");

        while (listening) {
        	
            TCPServerHilo hilo = new TCPServerHilo(serverSocket.accept(),this);
            
            hilo.start();
            
        }

        serverSocket.close();
    }
    
    public static void main(String[] args) throws IOException {
    	
    	TCPMultiServer tms = new TCPMultiServer();
    	tms.ejecutar();
    	
    }
}
