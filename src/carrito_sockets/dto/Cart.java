/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carrito_sockets.dto;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author dany2
 */
public class Cart implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    private Auto auto;
    private Integer qty;
    
    public Cart(){
        
    }
    
    public Cart(Auto auto, Integer qty){
        this.auto = auto;
        this.qty = qty;
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
