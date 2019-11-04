/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threadbarbeirodorminhoco;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author lucas
 */
public class Main {
    
    public static void main(String[] args) {
        int numClientes = 10;
        int numCadeiras = 3;
        
        Random ran = new Random();
        
        ArrayList<Cliente> clientes = new ArrayList();
        
        for(int i = 0; i < numClientes; i++){
            Cliente cli = new Cliente(i+1, ran);
            clientes.add(cli);
        }
        
        Barbeiro bar = new Barbeiro(numCadeiras, numClientes);
        
        bar.start();
        
        for(int i = 0; i < numClientes; i++){            
            clientes.get(i).start();
        }
        
        try {
            for(int i = 0; i < numClientes; i++){            
                clientes.get(i).join();
            }
            
            bar.join();
        } catch (InterruptedException e) {
            System.out.println("interrupcao" + e);
        }
    }
}
