/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import com.toedter.calendar.JDateChooser;
import java.sql.CallableStatement;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author yaelr
 */
public class CUsuario {
    
    int idSexo; 

    public void establecerIdSexo(int idSexo) {
        this.idSexo = idSexo;
    }
    
    /*aqui se esta llenando el combobox con las opciones que le declare en la base de datos */
    public void MostrarSexoComboBox(JComboBox comboSexo){
        Clases.CConexion objetoConexion = new Clases.CConexion(); 
        
        String sql = "Select * from sexo";
        
        Statement st; 
        
        try{
            st = objetoConexion.estableceConexion().createStatement();
            
            ResultSet rs = st.executeQuery(sql); 
            comboSexo.removeAllItems();
            
            while(rs.next()){
                String nombreSexo = rs.getString("sexo");
                this.establecerIdSexo(rs.getInt("id"));
                
                comboSexo.addItem(nombreSexo); 
                comboSexo.putClientProperty(nombreSexo, idSexo);
            }
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "error al mostrar sexo"+ e.toString());
        }finally{
            objetoConexion.cerrarConexion();
        }
        
    }
    
    
    /*aqui se le estan pasando los datos a la base de datos de los campos que defini en el formulario */
    public void agregarUsuarios(JTextField nombres, JTextField apellidos, JComboBox combosexo, JTextField edad, JDateChooser fnacimiento){
        CConexion objetoConexion = new Clases.CConexion(); 
        
        String consulta = "insert into usuarios (nombres,apellidos,fksexo,edad,fnacimiento) values (?,?,?,?,?)";
        
        try{
            CallableStatement  cs = objetoConexion.estableceConexion().prepareCall(consulta); 
            cs.setString(1, nombres.getText());
            cs.setString(2, apellidos.getText());
            
            int idSexo = (int) combosexo.getClientProperty(combosexo.getSelectedItem()); 
            cs.setInt(3, idSexo);
            cs.setInt(4, Integer.parseInt(edad.getText()));
            
            Date fechaSeleccionada = fnacimiento.getDate();
            java.sql.Date fechaSQL = new java.sql.Date(fechaSeleccionada.getTime()); 
            cs.setDate(5, fechaSQL);
            
            cs.execute(); 
            
            JOptionPane.showMessageDialog(null, "Se Guardo usuario correctamente");
            
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "error al mostrar sexo"+ e.toString());
        }
    }
    
    /*Aqui se van a mostrar todos los usuarios que hemos guardado en la base de datos*/
    public void mostrarUsuarios(JTable tablaTotalUsuarios){
        Clases.CConexion objetoConexion = new Clases.CConexion(); 
        
        DefaultTableModel modelo = new DefaultTableModel(); 
        
        String sql = ""; 
        
      
        
        
        modelo.addColumn("Id");
        modelo.addColumn("Nombres");
        modelo.addColumn("Apellidos");
        modelo.addColumn("sexo");
        modelo.addColumn("Edad");
        modelo.addColumn("FNacimeinto");
        
        tablaTotalUsuarios.setModel(modelo);
        
        sql = "select usuarios.id, usuarios.nombres, usuarios.apellidos, sexo.sexo, usuarios.edad, usuarios.fnacimiento from usuarios inner join sexo on usuarios.fksexo = sexo.id";
        
          try{
            Statement st = objetoConexion.estableceConexion().createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            while(rs.next()){
                String id = rs.getString("id");
                String nombres = rs.getString("nombres");
                String apellidos = rs.getString("apellidos");
                String sexo = rs.getString("sexo");
                String edad = rs.getString("edad");
                
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                java.sql.Date fechaSQL = rs.getDate("fnacimiento"); 
                String nuevaFecha = sdf.format(fechaSQL);
                
                modelo.addRow(new Object[]{id,nombres, apellidos, sexo, edad, nuevaFecha});
            }
            
            tablaTotalUsuarios.setModel(modelo);
            
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "error al mostrar usuarios"+ e.toString());
        } finally{
              objetoConexion.cerrarConexion();
          }
    }
    
    /*aqui se selecciona el registro para que se llene el formulario para poder editar un registro*/
    
    public void Seleccionar(JTable totalUsuarios, JTextField id, JTextField nombres, JTextField apellidos, JComboBox sexo, JTextField edad, JDateChooser fnacimiento){
        int fila = totalUsuarios.getSelectedRow();
        
        if(fila >= 0){
            id.setText(totalUsuarios.getValueAt(fila, 0).toString());
            nombres.setText(totalUsuarios.getValueAt(fila, 1).toString());
            apellidos.setText(totalUsuarios.getValueAt(fila, 2).toString());
            
            sexo.setSelectedItem(totalUsuarios.getValueAt(fila, 3).toString());
            edad.setText(totalUsuarios.getValueAt(fila, 4).toString());
            String fechaString = totalUsuarios.getValueAt(fila, 5).toString();
            
            try{
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date fechaDate = sdf.parse(fechaString);
                fnacimiento.setDate(fechaDate);
            }catch(Exception e){
                JOptionPane.showMessageDialog(null, "error al Seleccionar"+ e.toString());
            }
            
        }
    }
    
    /*aqui ya se vana a modificar los registros que hemos seleccionado*/
    public void ModificarUsuario(JTextField id ,JTextField nombres, JTextField apellidos, JComboBox combosexo, JTextField edad, JDateChooser fnacimiento){
        CConexion objetoConexion = new CConexion();
        
        String consulta = "update usuarios set usuarios.nombres=?, usuarios.apellidos=?, usuarios.fksexo=?, usuarios.edad=?, usuarios.fnacimiento=? where usuarios.id=? ";
        
        try{
           CallableStatement cs = objetoConexion.estableceConexion().prepareCall(consulta);
           cs.setString(1, nombres.getText());
           cs.setString(2, apellidos.getText());
           
           int idSexo = (int) combosexo.getClientProperty(combosexo.getSelectedItem());
           
           cs.setInt(3, idSexo);
           cs.setInt(4, Integer.parseInt(edad.getText()));
           
           Date fechaSeleccionada = fnacimiento.getDate();
            java.sql.Date fechaSQL = new java.sql.Date(fechaSeleccionada.getTime()); 
            cs.setDate(5, fechaSQL);
            cs.setInt(6, Integer.parseInt(id.getText()));
            
            cs.execute();
           
            JOptionPane.showMessageDialog(null, "se modifico correctamente");
        }catch(Exception e){
             JOptionPane.showMessageDialog(null, "error al modificar"+ e.toString());
        }finally{
            objetoConexion.cerrarConexion();
        }
    }
    
    /*Aqui ya se hace la funcion de eliminar */
    public void EliminarUsuario(JTextField id){
        CConexion objetoConexion = new CConexion();
        
        String consulta = "delete from usuarios where usuarios.id=?";
        
        try{
            CallableStatement cs = objetoConexion.estableceConexion().prepareCall(consulta);
            cs.setInt(1, Integer.parseInt(id.getText()));
            cs.execute();
            
            JOptionPane.showMessageDialog(null, "registro eliminado correctamente");
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "error al eliminar"+ e.toString());
        }finally{
            objetoConexion.cerrarConexion();
        }
    }
}
