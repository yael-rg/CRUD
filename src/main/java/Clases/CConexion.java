/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

/**
 *
 * @author yaelr
 * 
 * aqui se esta estableciendo la conexion a la base de datos la cual es una base relacional 
 */
public class CConexion {
    Connection conectar = null; 
    
    String usuario = "root"; 
    String contrasenia = "root"; 
    String bd = "bdusuarios"; 
    String ip = "localhost";
    String puerto = "3306";
    
    String cadena = "jdbc:mysql://"+ip+":"+puerto+"/"+bd;
    
    public Connection estableceConexion(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conectar = DriverManager.getConnection(cadena, usuario, contrasenia);
            JOptionPane.showMessageDialog(null, "se conecto correctamente"); 
        }catch(Exception e){
             JOptionPane.showMessageDialog(null, "No se conecto correctamente"); 
        }
        return conectar; 
    }
    
    public void cerrarConexion(){
        try{
            if(conectar != null && !conectar.isClosed()){
                JOptionPane.showMessageDialog(null, "conexion cerrada");
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "no se pudo cerra la conexion");
        }
    }
}
