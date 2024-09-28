package multiserver.tcp;

import java.net.*;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TCPServerHilo extends Thread {

    private Socket socket = null;
    
    public TCPServerHilo(Socket socket) {
        super("TCPServerHilo");
        this.socket = socket;
    }

    @Override
    public void run() {
        String username; 
        String password; 
        boolean respuesta = false;  // Será `true` si las credenciales son correctas

        try {
            // Recibir el objeto serializado con los datos del cliente
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            
            System.out.println("1");
            EstructuraDatos datos = (EstructuraDatos) in.readObject(); 
            System.out.println("3");

            username = datos.getUser();
            password = datos.getPassword();

            try ( // Verificar las credenciales en la base de datos
                Connection conn = DatabaseConnection.getConnection()) {
                String query = "SELECT * FROM users WHERE username = ? AND password = ?";
                
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, username);
                    stmt.setString(2, password);
                    
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            respuesta = true;  // Credenciales válidas
                        }
                        // Enviar la respuesta al cliente
                        out.writeObject(respuesta);
                        System.out.println("4");
                    }
                }
            }

        } catch (IOException | ClassNotFoundException | SQLException e) {
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
    }

}
