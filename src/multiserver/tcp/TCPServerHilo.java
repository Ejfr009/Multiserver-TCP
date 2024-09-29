package multiserver.tcp;

import Libreria.EstructuraDatos;
import java.net.*;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;





public class TCPServerHilo extends Thread {

    private Socket socket;
    private EstructuraDatos datos;
    private ObjectOutputStream out; 
    private ObjectInputStream in; 
    private boolean respuesta;

    
    public TCPServerHilo(Socket socket) {
        super("TCPServerHilo");
        this.socket = socket;
        CrearConexion();
        this.respuesta = false; 
        
    }
    
    
    @Override
    public void run() {
        
        
        while(true)
        {
            login();
            logout();
        }
           
        //solucionar el while infinito
        //CerrarConexion();
    }
    
    
    private void login()
    {
        try{
            while(!respuesta)
            {

                datos = (EstructuraDatos) in.readObject();
                validar(datos);

                out.writeObject(respuesta);
                out.reset();
            }
        }catch( IOException | ClassNotFoundException e){
            System.err.println("Error de login");
            System.exit(1);
        }
        
        this.respuesta = false; 
    }
    
    private void logout()
    {
      
   
    }
    
    private void CrearConexion()
    {
        
        try {
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream()); 
        } catch (IOException ex) {
            System.err.println("Error de Stream");
            System.exit(1);
        }
        
    }
    
    private void CerrarConexion()
    {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
        } catch (IOException ex) {
            System.err.println("Error de Stream");
            System.exit(1);
        }
    }
    
    
    private void validar(EstructuraDatos datos)
    {
        String username = datos.getUser();
        String password = datos.getPassword();
     
         
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
    
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                stmt.setString(2, password);
        
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        this.respuesta = true;  // Credenciales válidas
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }                  

    }
    
}

