package multiserver.tcp;

import Libreria.EstructuraDatos;
import java.net.*;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class TCPServerHilo extends Thread {

    private Socket socket = null;
    
    public TCPServerHilo(Socket socket) {
        super("TCPServerHilo");
        this.socket = socket;
    }
    
    private boolean validar(EstructuraDatos datos)
    {
        String username = datos.getUser();
        String password = datos.getPassword();
        
        System.out.println(username);
        System.out.println(password);
        boolean respuesta = false;  
         
     
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
    
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                stmt.setString(2, password);
        
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        respuesta = true;  // Credenciales válidas
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }                  
            
        return respuesta;
    }

    @Override
    public void run() {
        
        EstructuraDatos datos;

        try {
            // Recibir el objeto serializado con los datos del cliente
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            
            while(true)
            {
                datos = (EstructuraDatos) in.readObject(); 
            
                boolean respuesta = validar(datos);
                
                out.writeObject(respuesta);
                out.reset();
            }
        }
        catch (UnknownHostException e) 
        {
            System.err.println("Host desconocido");
            System.exit(1);
        } 
        catch (IOException e)
        {
            System.err.println("Error de I/O en la conexion al host");
            System.exit(1);
        } catch (ClassNotFoundException ex) {
           System.err.println("Clase de objeto no encontrada");
           System.exit(1);
        }
    }
}

