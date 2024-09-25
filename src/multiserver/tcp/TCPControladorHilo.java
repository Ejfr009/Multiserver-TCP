package multiserver.tcp;

import java.util.List;

public class TCPControladorHilo extends Thread {

    public List<TCPServerHilo> hilosClientes;
    int var;
    
    
    public TCPControladorHilo() {
        
    }
    
    public void añadirHilo(TCPServerHilo Hilo)
    {
        this.hilosClientes.add(Hilo);
    }
    
    public void actualizar()
    {
        for (int i = 0; i < this.hilosClientes.size(); i++) 
        {
            this.hilosClientes.get(i).actualizarUsuarios();
        }
    }

    public void run() {

       while(true)
       {
           try
           {
                Thread.sleep(100); // 2000 milisegundos = 2 segundos
           }
           catch (InterruptedException e) 
           {
                System.exit(1);
           }
           
           if(var != Base de datos)
           {
                actualizar();
                var = Base de datos;
           }
       }
    }
}
