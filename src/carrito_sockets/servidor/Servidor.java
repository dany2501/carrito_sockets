/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carrito_sockets.servidor;

import carrito_sockets.dto.Auto;
import carrito_sockets.dto.Cart;
import carrito_sockets.dto.Request;
import carrito_sockets.util.Util;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dany2
 */
public class Servidor {
    
    private static Socket s;
    private static DatagramPacket dEnviado;
    private static InetAddress ip;
    private static int port;
    private static byte[] enviado;
    private static DatagramSocket ss;
    private static final String FILE_NAME = "C:\\Users\\dany2\\Desktop\\ESCOM\\carrito_sockets\\src\\carrito_sockets\\servidor\\data\\data.txt";
    private static final String FILE_CART = "C:\\Users\\dany2\\Desktop\\ESCOM\\carrito_sockets\\src\\carrito_sockets\\servidor\\data\\cart.txt";
    public static void main(String[] args){
        
        try{
            ss = new DatagramSocket(3080);
            System.out.println("Esperando conexiones");
            
            while(true){
                
                byte[] recibido = new byte[1024];
                enviado = new byte[1024];
                
                DatagramPacket dRecibido = new DatagramPacket(recibido,recibido.length);
                ss.receive(dRecibido);
                ip = dRecibido.getAddress();
                port = dRecibido.getPort();
                
                ByteArrayInputStream bis = new ByteArrayInputStream(dRecibido.getData());
                ObjectInputStream in = new ObjectInputStream(bis);
                Request rq = (Request) in.readObject();
                System.out.println("Opcion "+rq.getOpcion());
                String mensaje = rq.getOpcion();
                switch(String.valueOf(mensaje)){
                    case "Mostrar":
                        mostrarCatalogo();
                        break;
                    case "Carrito":
                        mostrarCarrito();
                        break;
                    case "Modificar":
                        modificarAutos(rq.getAuto(),rq.getIndex());
                        break;
                    case "Eliminar":
                        eliminarAuto(rq.getAuto());
                        break;
                    case "Agregar":
                        agregarAuto(rq.getAuto());
                        break;
                    case "AddToCart":
                        agregarCarrito(rq.getIndex(), rq.getQty());
                        break;
                    case "UpdateCart":
                        agregarCarrito(rq.getIndex(), rq.getQty());
                        
                    default:
                        System.out.println("Entró aquí");
                        break;
                }
                
                System.out.println("Accion Cliente: "+mensaje);
                
                
                
                
                
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public static void eliminarAuto(Auto auto){
        try{
            List<Auto> autos = getAutos();
            int index = 0;
            for (int i =0;i<autos.size();i++){
                if(autos.get(i).getNombre().equals(auto.getNombre())){
                    index = i;
                }
            }
            autos.remove(index);
            updateAutos(autos);
            String msj = "Eliminado";
            enviado = msj.getBytes();
            dEnviado = new DatagramPacket(enviado,enviado.length,ip,port);
            ss.send(dEnviado);
            
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static void mostrarCatalogo(){
        List<Auto> autos = new ArrayList<>();
        try{
            autos = getAutos();
            enviado = Util.parseObjToBytes(autos);
            dEnviado = new DatagramPacket(enviado,enviado.length,ip,port);
            ss.send(dEnviado);
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static void mostrarCarrito(){
        List<Cart> autos = new ArrayList<>();
        try{
            autos = getCart();
            enviado = Util.parseObjToBytes(autos);
            dEnviado = new DatagramPacket(enviado,enviado.length,ip,port);
            ss.send(dEnviado);
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    public static void agregarAuto(Auto auto){
        try{
            String outputFile = "C:\\Users\\dany2\\Desktop\\ESCOM\\carrito_sockets\\src\\carrito_sockets\\servidor\\img\\"+auto.getNombre()+auto.getImagen().substring(auto.getImagen().lastIndexOf("."));
            File file = new File(outputFile);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(auto.getImagenData());
            fos.flush();
            fos.close();
            System.out.println("Imagen guardada");
            List<Auto> autos = new ArrayList<>();
            File f = new File(FILE_NAME);
            if(f.exists() && !f.isDirectory()){
                FileInputStream fis = new FileInputStream(FILE_NAME);
                ObjectInputStream ois = new ObjectInputStream(fis);
                autos = (List<Auto>) ois.readObject();
                if (autos!=null){
                    autos.add(auto);
                }else{
                    autos = new ArrayList<>();
                    autos.add(auto);
                }
                fis.close();
                ois.close();
            }
            
            updateAutos(autos);
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static void agregarCarrito(Integer index, Integer qty){
        try{
            Auto auto = getAutos().get(index);
            if (auto == null){
                String msj = "Ocurrió un error";
                enviado = msj.getBytes();
                dEnviado = new DatagramPacket(enviado,enviado.length,ip,port);
                ss.send(dEnviado);
            }
            if(auto.getStock()<qty){
                String msj = "Ocurrió un error, stock insuficiente";
                enviado = msj.getBytes();
                dEnviado = new DatagramPacket(enviado,enviado.length,ip,port);
                ss.send(dEnviado);
            }
            List<Cart> autos = new ArrayList<>();
            File f = new File(FILE_CART);
            if(f.exists() && !f.isDirectory()){
                FileInputStream fis = new FileInputStream(FILE_CART);
                ObjectInputStream ois = new ObjectInputStream(fis);
                autos = (List<Cart>) ois.readObject();
                System.out.println("Before if autos != null");
                if (autos!=null){
                    if (autos.size()!=0){
                        for (Cart a : autos){
                            System.out.println("Cart "+a.getQty());
                            if(a.getAuto().getNombre().equals(auto.getNombre())){

                                a.setQty(qty);
                            }
                        }
                    }else{
                        autos.add(new Cart(auto,qty));
                    }
                    
                }else{
                    autos.add(new Cart(auto,qty));
                }
                fis.close();
                ois.close();
            }else{
                autos.add(new Cart(auto,qty));
            }
            System.out.println("Before update");
            updateCart(autos);
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static void modificarAutos(Auto a,Integer index){
        try{
            
            List<Auto> autos = getAutos();
            autos.get(index).setNombre(a.getNombre());
            autos.get(index).setPrecio(a.getPrecio());
            autos.get(index).setStock(a.getStock());
            updateAutos(autos);
            
            String msj = "Actalizado correctamente";
            enviado = msj.getBytes();
            dEnviado = new DatagramPacket(enviado,enviado.length,ip,port);
            ss.send(dEnviado);
            
            
            
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static void updateAutos(List<Auto> autos){
        try{
            FileOutputStream fData = new FileOutputStream(FILE_NAME);
            ObjectOutputStream salida = new ObjectOutputStream(fData);
            salida.writeObject(autos);
            fData.close();
            salida.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public static void updateCart(List<Cart> autos){
        try{
            FileOutputStream fData = new FileOutputStream(FILE_CART);
            ObjectOutputStream salida = new ObjectOutputStream(fData);
            salida.writeObject(autos);
            fData.close();
            salida.close();
            String msj = "Agregado correctamente";
            enviado = msj.getBytes();
            dEnviado = new DatagramPacket(enviado,enviado.length,ip,port);
            ss.send(dEnviado);
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public static List<Auto> getAutos(){
        List<Auto> autos = new ArrayList<>();
        try{
            
            File f = new File(FILE_NAME);
            if(f.exists() && !f.isDirectory()){
                FileInputStream fis = new FileInputStream(FILE_NAME);
                ObjectInputStream ois = new ObjectInputStream(fis);
                autos = (List<Auto>) ois.readObject();
                fis.close();
                ois.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return autos;
    }
    
    public static List<Cart> getCart(){
        List<Cart> autos = new ArrayList<>();
        try{
            
            File f = new File(FILE_CART);
            if(f.exists() && !f.isDirectory()){
                FileInputStream fis = new FileInputStream(FILE_CART);
                ObjectInputStream ois = new ObjectInputStream(fis);
                autos = (List<Cart>) ois.readObject();
                System.out.println("Cart length "+autos.size());
                fis.close();
                ois.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return autos;
    }
    
}

