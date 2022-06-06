/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carrito_sockets.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author dany2
 */
public class Util {
    
    public static byte[] parseObjToBytes(Object o){
        
        try{
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(o);
            return bos.toByteArray();
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return null;
    }
    
    public static Object parseBytesToObject(byte[] bytes){
        try{
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream in = new ObjectInputStream(bis);
            return in.readObject();
        }catch(Exception e){
            System.out.println("Parse Exception");
            e.printStackTrace();
        }
        return null;
    }
    
}
