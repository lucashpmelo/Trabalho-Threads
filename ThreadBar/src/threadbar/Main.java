/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threadbar;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author lucas
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    
    
    public static void main(String[] args) {
        /*Numero de Clientes no Bar*/
        int numCliente = 5;
        /*Numero de Garçons no Bar*/
        int numGarcons = 2;
        /*Capacidade de Bebida que cada Garçom pode carregar por vez*/
        int numCapacidade = 2;
        /*Numero de Rodadas livres*/
        int numRodadas = 5;
        
        /*Listas de Clientes e Garçons para facilitar criar as Threads*/
        ArrayList<Cliente> clientes = new ArrayList();
        ArrayList<Garcom> garcons = new ArrayList();
        
        /*Random que será passado por parametro para a Classe Cliente*/
        Random ran = new Random();
        
        /*Instanciando objeto Cliente usando a quantidade especificada*/
        for(int i = 0; i < numCliente; i++){            
            Cliente cli = new Cliente(i+1, ran);            
            clientes.add(cli);            
        }        
        
        /*Instanciando objeto Garçom usando a quantidade especificada*/
        for(int i = 0; i < numGarcons; i++){            
            Garcom gar = new Garcom(i+1, numCapacidade, numRodadas, numGarcons, numCliente);
            garcons.add(gar);
        }
        
        /*Criando uma Lista de Threads 'auxGarcomThreads' na Classe Fila com dos os Garçons*/
        for(int i = 0; i < numGarcons; i++){            
            garcons.get(i).carregaListaGarcons();
        }        
        
        Garcom.printRodada();
        
        /*Iniciando Threads Cliente*/
        for(int i = 0; i < numCliente; i++){            
            clientes.get(i).start();
        }
        
        /*Iniciando Threads Garçom*/
        for(int i = 0; i < numGarcons; i++){            
            garcons.get(i).start();
        }
        
        /* join() das Threads Cliente e Garçom */
        try {            
            for(int i = 0; i < numCliente; i++){            
                clientes.get(i).join();
            }

            for(int i = 0; i < numGarcons; i++){            
                garcons.get(i).join();
            }
        } catch (InterruptedException e) {
            System.out.println("interrupcao " + e);
        }
        
        Garcom.printFim();
    }    
}
