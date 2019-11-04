/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threadbarbeirodorminhoco;

import java.util.ArrayList;

/**
 *
 * @author lucas
 */
public class Barbeiro extends Thread{
        
    static int numCadeiras;
    static int numClientes;
    private static ArrayList<Integer> FilaCliente = new ArrayList();
    
    public Barbeiro(){
    }
    
    public Barbeiro(int numCadeiras, int numClientes){
        Barbeiro.numCadeiras = numCadeiras;
        Barbeiro.numClientes = numClientes;
    }
    
    public static synchronized boolean addClientesFila(int cliNome){
        if(FilaCliente.size() <= numCadeiras){
            FilaCliente.add(cliNome);
            return true;
        }else{
            return false;
        }
    }
    
    public void CortandoOUDormindo() {
        while(true) {
                        
            if(FilaCliente.size() > 0){
                System.out.println("\t\t\tFila de Clientes na Barbearia: " + FilaCliente + " Total de Cadeiras vagas: " + (numCadeiras - FilaCliente.size()));
                System.out.println("\t\t\tCortando cabelo do Cliente: " + FilaCliente.get(0));                
                
                try {
                    Thread.sleep(3500); 
                    System.out.println("\t\t\tTerminou de cortar o cabelo do Cliente: " + FilaCliente.get(0));
                    FilaCliente.remove(0);
                }
                catch (InterruptedException e) {
                    System.out.println("interrupcao " + e);
                }
                
                numClientes--;
                
                if(numClientes == 0)
                    return;
                
            }else{
                System.out.println("\t\t\tFila de Clientes na Barbearia: " + FilaCliente + " Total de Cadeiras vagas: " + (numCadeiras - FilaCliente.size()));
                System.out.println("\t\t\tBarbeiro esta Dormindo");
                
                Cliente.barbeiroDormindo(this);
                
                synchronized(this){           
                    try {            
                        this.wait();
                    } catch (InterruptedException e) {                
                    }
                }
            }
        }
    }
    
    public void run() {         
        CortandoOUDormindo();
    }
}
