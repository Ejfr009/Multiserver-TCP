package multiserver.tcp;

import java.net.*;
import java.io.*;

public class TCPServerHilo extends Thread {

    private Socket socket = null;
    
    public TCPServerHilo(Socket socket) {
        super("TCPServerHilo");
        this.socket = socket;
    }

    public void run(){
        
        String username; 
        String password; 
        boolean respuesta = true;
        
        try {
            // Enviar el objeto serializado al servidor
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
 
            EstructuraDatos datos = (EstructuraDatos) in.readObject(); 
            
            username = datos.getUser();
            password = datos.getPassword();
            
            // HACER LA LOGICA DE: VERIFICAR DATOS EN EL SERVIDOR
            //
            //
            
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(respuesta);
             
        } catch (IOException e) {
            // Manejo de errores de I/O
            e.printStackTrace();
            System.out.println("Error de comunicación con el servidor.");
        } catch (ClassNotFoundException e) {
            // Manejo de errores en la deserialización (cuando no se encuentra la clase del objeto recibido)
            e.printStackTrace();
            System.out.println("Error en la lectura de datos: clase no encontrada.");
        }
        
        //socket.close();
        //System.out.println("Finalizando Hilo");
       
    }
}
