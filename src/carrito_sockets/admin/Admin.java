package carrito_sockets.admin;

import carrito_sockets.dto.Auto;
import carrito_sockets.dto.Request;
import carrito_sockets.util.Util;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author dany2
 */
public class Admin {
    
    private static BufferedReader bf;
    private static DatagramSocket s;
    private static InetAddress host;
    private static int pto, autosLength = 0;
    private static Boolean flag = true;
    private static JFileChooser jf = new JFileChooser();
    private static List<Auto> autos = new ArrayList<>();
    public static void main(String[] args){
        try{
            
            bf = new BufferedReader(new InputStreamReader(System.in));
            s = new DatagramSocket();
            host = InetAddress.getByName("localhost");
            System.out.println("Escriba el puerto");
            pto = Integer.parseInt(bf.readLine());
            
            while(flag){
                System.out.println("¿Qué deseas hacer?");
                System.out.println("1.- Agregar auto");
                System.out.println("2.- Mostrar autos");
                System.out.println("3.- Modificar auto");
                System.out.println("4.- Eliminar auto");
                System.out.println("5.- Salir");
                Integer opcion = Integer.parseInt(bf.readLine().trim());
                switch(opcion){
                    case 1:
                        agregarAuto();
                        break;
                    case 2:
                        catalogo();
                        break;
                    case 3:
                        modificar();
                        break;
                    case 4:
                        eliminar();
                        break;
                    case 5:
                        System.out.println("¡Vuelve pronto!");
                        flag = false;
                        break;
                }
                
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static void agregarAuto(){
        try{
            System.out.println("Escribe el nombre del auto");
            String nombre = bf.readLine();
            System.out.println("Agrega el precio");
            Integer precio = Integer.parseInt(bf.readLine().trim());
            System.out.println("Agrega el stock");
            Integer stock = Integer.parseInt(bf.readLine().trim());
            int r = jf.showOpenDialog(null);
            
            if(r == JFileChooser.APPROVE_OPTION){
                File file = jf.getSelectedFile();
                if(file.isFile()){
                    String imagen = file.getPath();
                    DataInputStream dis = new DataInputStream(new FileInputStream(file));
                    long len = (int) file.length();
                    byte[] fileBytes = new byte[(int)len];
                    int read = 0;
                    int numRead=0;
                    while(read < fileBytes.length && (numRead=dis.read(fileBytes,read,fileBytes.length -read ))>=0){
                        read += numRead;
                    }
                    
                    Auto a = new Auto(nombre,imagen,stock,precio,fileBytes);
                    
                    byte[] enviado = new byte[1024];
                    Request rq = new Request("Agregar",a);
                    enviado = Util.parseObjToBytes(rq);
                    DatagramPacket dEnviado = new DatagramPacket(enviado,enviado.length,host,pto);

                    s.send(dEnviado);
                    
                }
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
            
            autos = (List<Auto>) Util.parseBytesToObject(dRecibido.getData());
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
            }else{
                System.out.println("No hay autos disponibles");
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static void eliminar(){
        try{
            catalogo();
            System.out.println("Ingresa el indice del item a eliminar");
            Integer i = Integer.parseInt(bf.readLine().trim());
            
            if (i>autosLength){
                System.out.println("Ingresa un indice valido");
                eliminar();
            }else{
                i= i-1;
                Auto a = autos.get(i);
                System.out.println("Auto "+a.getNombre());
                Request rq = new Request("Eliminar",a);
                byte[] enviado = Util.parseObjToBytes(rq);
                byte[] recibido = new byte[1024];
                
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
    
    public static void modificar(){
        
        try{
            catalogo();
            System.out.println("Ingresa el indice del item a modificar");
            Integer i = Integer.parseInt(bf.readLine().trim());
            
            if (i>autosLength){
                System.out.println("Ingresa un indice valido");
                modificar();
            }else{
                i= i-1;
                Auto a = autos.get(i);
                System.out.println("Que dato deseas modificar");
                System.out.println("1.- Nombre");
                System.out.println("2.- Precio");
                System.out.println("3.- Stock");
                
                Integer o = Integer.parseInt(bf.readLine().trim());
                
                switch(o){
                    case 1:
                        System.out.println("Ingresa el nuevo nombre");
                        String nombre = bf.readLine();
                        a.setNombre(nombre);
                        break;
                    case 2:
                        System.out.println("Ingresa el nuevo precio");
                        Integer precio = Integer.parseInt(bf.readLine().trim());
                        a.setPrecio(precio);
                        break;
                    case 3:
                        System.out.println("Ingresa el nuevo stock");
                        Integer stock = Integer.parseInt(bf.readLine().trim());
                        a.setStock(stock);
                        break;
                }
                
                Request rq = new Request("Modificar",a,i);
                byte[] enviado = Util.parseObjToBytes(rq);
                byte[] recibido = new byte[1024];
                
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
