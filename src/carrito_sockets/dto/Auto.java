package carrito_sockets.dto;


import java.io.Serializable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author dany2
 */
public class Auto implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    private String nombre;
    private String imagen;
    private Integer stock;
    private Integer precio;
    private byte[] imagenData;
    
    public Auto(){
        
    }
    
    public Auto(String nombre, String imagen, Integer stock, Integer precio){
        this.nombre=nombre;
        this.imagen=imagen;
        this.stock=stock;
        this.precio=precio;
    }
    
    public Auto(String nombre, String imagen, Integer stock, Integer precio, byte[] imagenData){
        this.nombre=nombre;
        this.imagen=imagen;
        this.stock=stock;
        this.precio=precio;
        this.imagenData = imagenData;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the imagen
     */
    public String getImagen() {
        return imagen;
    }

    /**
     * @param imagen the imagen to set
     */
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    /**
     * @return the stock
     */
    public Integer getStock() {
        return stock;
    }

    /**
     * @param stock the stock to set
     */
    public void setStock(Integer stock) {
        this.stock = stock;
    }

    /**
     * @return the precio
     */
    public Integer getPrecio() {
        return precio;
    }

    /**
     * @param precio the precio to set
     */
    public void setPrecio(Integer precio) {
        this.precio = precio;
    }

    /**
     * @return the imagenData
     */
    public byte[] getImagenData() {
        return imagenData;
    }

    /**
     * @param imagenData the imagenData to set
     */
    public void setImagenData(byte[] imagenData) {
        this.imagenData = imagenData;
    }
    
    
    
    
    
}
