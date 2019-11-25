/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portserial2;


import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import gnu.io.*;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.Writer;
import java.util.Enumeration;
/**
 *
 * @author maria.sanchez
 */
public class PortSerial2 {
  
     //inicializamos y decalramos variables
    CommPortIdentifier portId;
    Enumeration puertos;
    SerialPort serialport;
    static InputStream entrada = null;
    Thread t;
    //creamos un constructor para realizar la conexion del puerto
    public PortSerial2() {
        super();
        puertos=CommPortIdentifier.getPortIdentifiers();
        t = new Thread(new LeerSerial());
        while (puertos.hasMoreElements()) { //para recorrer el numero de los puertos, y especificar con cual quiero trabajar 
            //hasmorelements mientras tenga mas eleementos
            portId = (CommPortIdentifier) puertos.nextElement(); //next elemento recorre uno por uno
            System.out.println(portId.getName()); //puertos disponbibles
            
            if (portId.getName().equalsIgnoreCase("COM5")) {
                try {
                serialport= (SerialPort)portId.open("LecturaSerial", 500);//tiempo en ms
                    entrada = serialport.getInputStream();//esta variable del tipo InputStream obtiene el dato serial
                    try (Writer writer = new FileWriter("lecturas.csv")) {
                        StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer).build();
                        beanToCsv.write(entrada);
                    }
                    t.start(); // inciamos el hilo para realizar nuestra accion de imprimir el dato serial
              
            } catch (Exception e) {
            } } }
  }
    //con este metodo del tipo thread relaizamos 
 
    public static class LeerSerial implements Runnable {
       int aux;
       public void run () {
           while(true){
              try {
                aux = entrada.read(); // aqui estamos obteniendo nuestro dato serial
                Thread.sleep(100);
                if (aux>0) {
                    System.out.println((char)aux);//imprimimos el dato serial
                }               
            } catch (Exception e) {
            } } }
  }public static void main(String[] args) {
       new PortSerial2();
         }}

