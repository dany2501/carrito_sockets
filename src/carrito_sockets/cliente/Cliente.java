/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carrito_sockets.cliente;

/**
 *
 * @author dany2
 */
import carrito_sockets.dto.Auto;
import carrito_sockets.dto.Cart;
import carrito_sockets.dto.Request;
import carrito_sockets.util.Util;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;

/**
 *
 * @author dany2
 */
public class Cliente {
    
    private static Boolean flag = true;
    private static BufferedReader bf;
    private static DatagramSocket s;
    private static InetAddress host;
    private static int pto,autosLength=0;
    public static void main(String[] args){
        try{
            
            bf = new BufferedReader(new InputStreamReader(System.in));
            s = new DatagramSocket();
            host = InetAddress.getByName("localhost");
            System.out.println("Escriba el puerto");
            pto = Integer.parseInt(bf.readLine());
            
            
            while(flag){
                System.out.println("Que deseas hacer?");
                System.out.println("1.- Ver catalogo");
                System.out.println("2.- Ver carrito");
                System.out.println("3.- Salir");
                Integer opcion = Integer.parseInt(bf.readLine().trim());
                switch(opcion){
                    case 1:
                        catalogo();
                        break;
                    case 2:
                        enviar();
                        break;
                    case 3:
                        System.out.println("¡Vuelve pronto!");
                        flag = false;
                        break;
                }
                
            }
            
            
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public static void enviar(){
        try{
            byte[] enviado = new byte[1024];
            byte[] recibido = new byte[1024];
            
            Request rq = new Request("Carrito");

            enviado = Util.parseObjToBytes(rq);
            DatagramPacket dEnviado = new DatagramPacket(enviado,enviado.length,host,pto);
            s.send(dEnviado);

            DatagramPacket dRecibido = new DatagramPacket(recibido,recibido.length);
            s.receive(dRecibido);

            List<Cart> autos = (List<Cart>) Util.parseBytesToObject(dRecibido.getData());
            autosLength = autos.size();
            if (autosLength !=0){
                System.out.println("Articulos en carrito");
                for(int i=0;i<autos.size();i++){
                    Cart cart = autos.get(i);
                    Auto a = cart.getAuto();
                    int j = i+1;
                    if(a.getStock()!=0){
                        System.out.println("Auto #"+j);
                        System.out.println("Nombre "+a.getNombre());
                        System.out.println("Precio "+a.getPrecio());
                        System.out.println("Agregados "+cart.getQty());
                        System.out.println("\n");
                    }
                }
                updateCart();
                //addToCart();
                
            }else{
                System.out.println("No hay autos disponibles");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static void catalogo(){
        try{
            byte[] enviado = new byte[1024];
            byte[] recibido = new byte[1024];
            Request rq = new Request("Mostrar");

            enviado = Util.parseObjToBytes(rq);
            DatagramPacket dEnviado = new DatagramPacket(enviado,enviado.length,host,pto);

            s.send(dEnviado);

            DatagramPacket dRecibido = new DatagramPacket(recibido,recibido.length);
            s.receive(dRecibido);
            
            List<Auto> autos = (List<Auto>) Util.parseBytesToObject(dRecibido.getData());
            autosLength = autos.size();
            if (autosLength !=0){
                System.out.println("Autos disponibles");
                for(int i=0;i<autos.size();i++){
                    Auto a = autos.get(i);
                    int j = i+1;
                    if(a.getStock()!=0){
                        System.out.println("Auto #"+j);
                        System.out.println("Nombre "+a.getNombre());
                        System.out.println("Precio "+a.getPrecio());
                        System.out.println("Disponibles "+a.getStock());
                        System.out.println("\n");
                    }
                }
                addToCart();
                
            }else{
                System.out.println("No hay autos disponibles");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private static void addToCart(){
        try{
            byte[] enviado = new byte[1024];
            byte[] recibido = new byte[1024];
            System.out.println("Deseas agregar alguno a tu carrito?");
            System.out.println("1.- Si");
            System.out.println("2.- No");
            if (Integer.parseInt(bf.readLine().trim()) == 1){
                System.out.println("Ingresa el indice del auto a agregar");
                Integer index = Integer.parseInt(bf.readLine().trim());
                int j = index-1;
                System.out.println("Cuantos deseas agregar?");
                Integer qty = Integer.parseInt(bf.readLine().trim());
                Request rq = new Request("AddToCart",j,qty);
                enviado = Util.parseObjToBytes(rq);
                
                DatagramPacket dEnviado = new DatagramPacket(enviado,enviado.length,host,pto);
                s.send(dEnviado);
                
                DatagramPacket dRecibido = new DatagramPacket(recibido,recibido.length);
                s.receive(dRecibido);
                
                String mensaje = new String(dRecibido.getData());
                System.out.println("Servidor: "+mensaje);
                
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private static void updateCart(){
        try{
            byte[] enviado = new byte[1024];
            byte[] recibido = new byte[1024];
            System.out.println("Deseas modificar algún artículo?");
            System.out.println("1.- Si");
            System.out.println("2.- No");
            if (Integer.parseInt(bf.readLine().trim()) == 1){
                System.out.println("Ingresa el indica el indice del articulo");
                Integer index = Integer.parseInt(bf.readLine().trim());
                int j = index-1;
                System.out.println("Cuantos deseas agregar?");
                Integer qty = Integer.parseInt(bf.readLine().trim());
                Request rq = new Request("UpdateCart",j,qty);
                enviado = Util.parseObjToBytes(rq);
                
                DatagramPacket dEnviado = new DatagramPacket(enviado,enviado.length,host,pto);
                s.send(dEnviado);
                
                DatagramPacket dRecibido = new DatagramPacket(recibido,recibido.length);
                s.receive(dRecibido);
                
                String mensaje = new String(dRecibido.getData());
                System.out.println("Servidor: "+mensaje);
                
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
}
