package multiserver.tcp;

import Libreria.EstructuraDatos;
import java.net.*;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;




public class TCPServerHilo extends Thread {

    public Socket socket;
    public EstructuraDatos datos;
    public ObjectOutputStream out; 
    public ObjectInputStream in; 
    private boolean respuesta;
    Timer timer;
    
    public TCPMultiServer server;
    private String Personas;
    public TCPServerHilo(Socket socket, TCPMultiServer server) {
        super("TCPServerHilo");
        this.socket = socket;
        CrearConexion();
        this.respuesta = false; 
        this.server=server;
        
    }
    
    
    @Override
    public void run() {
        
        login();
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
        
        server.enLinea.add(datos.getUser());
        
        
        timer = new Timer(1050, (ActionEvent e) -> {
            Personas="";
            for (String nombre:server.enLinea){
                Personas=Personas+"-"+nombre+"\n";
            }
            
            try {
                out.writeObject(Personas);
            } catch (IOException ex) {
                timer.setRepeats(false);
            } 
        });
        timer.setRepeats(true); // Para que solo se ejecute una vez
        timer.start();
        ActualizadorSalir hiloSalir = new ActualizadorSalir(this);
        hiloSalir.start();
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
    
    public void CerrarConexion()
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

