/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package multiserver.tcp;

import Libreria.EstructuraDatos;
import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Leo
 */
public class ActualizadorSalir extends Thread {

    public TCPServerHilo serverHiloAux;
    
    public ActualizadorSalir(TCPServerHilo serverHiloAux) {
        super("TCPServerHilo");
        this.serverHiloAux = serverHiloAux;
        
    }
    @Override
    public void run() {
        String nombre= new String();
        try {
            nombre = (String) serverHiloAux.in.readObject();
        } catch (IOException ex) {
            Logger.getLogger(ActualizadorSalir.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ActualizadorSalir.class.getName()).log(Level.SEVERE, null, ex);
        }
        serverHiloAux.server.enLinea.remove(nombre);
        serverHiloAux.CerrarConexion();
    }    
        
    
    
}

