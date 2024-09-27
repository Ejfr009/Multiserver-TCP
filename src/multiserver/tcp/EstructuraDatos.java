/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package multiserver.tcp;
import java.io.Serializable;

/**
 *
 * @author EJFR0
 */
public class EstructuraDatos implements Serializable {
    private String user;
    private String password;
    
    public EstructuraDatos()
    {
        
    }
    
    public void setUser( String user)
    {
       this.user = user;
    }
    
    public void setPassword( String password)
    {
    
       this.password = password;
    }
    
    public String getUser()
    {
        return user;
    }
    
    public String getPassword()
    {
    
        return password;
    }
           
}
