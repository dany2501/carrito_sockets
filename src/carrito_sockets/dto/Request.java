/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carrito_sockets.dto;

import java.io.Serializable;

/**
 *
 * @author dany2
 */
public class Request implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    private String opcion;
    private Auto auto;
    private Integer index;
    private Integer qty;
    
    public Request(){
        
    }
    
    public Request(String opcion){
        this.opcion = opcion;
    }
    
    public Request(String opcion, Auto auto){
        this.opcion = opcion;
        this.auto = auto;
    }
    
    public Request(String opcion, Integer index, Integer qty){
        this.opcion = opcion;
        this.index = index;
        this.qty = qty;
    }
    
    public Request(String opcion, Auto auto, Integer index){
        this.opcion = opcion;
        this.auto=auto;
        this.index = index;
    }

    /**
     * @return the opcion
     */
    public String getOpcion() {
        return opcion;
    }

    /**
     * @param opcion the opcion to set
     */
    public void setOpcion(String opcion) {
        this.opcion = opcion;
    }

    /**
     * @return the auto
     */
    public Auto getAuto() {
        return auto;
    }

    /**
     * @param auto the auto to set
     */
    public void setAuto(Auto auto) {
        this.auto = auto;
    }

    /**
     * @return the index
     */
    public Integer getIndex() {
        return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(Integer index) {
        this.index = index;
    }

    /**
     * @return the qty
     */
    public Integer getQty() {
        return qty;
    }

    /**
     * @param qty the qty to set
     */
    public void setQty(Integer qty) {
        this.qty = qty;
    }
    
    
    
    
    
    
    
    
    
}
