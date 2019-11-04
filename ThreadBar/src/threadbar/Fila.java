/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threadbar;

import java.util.ArrayList;

/**
 *
 * @author lucas
 */
public class Fila {
    /*Listas usada para manter a ordem a ordem de pedidos dos Clientes*/
    private static ArrayList<Integer> filaPedidosClientes = new ArrayList();
    private static ArrayList<Integer> auxClientes = new ArrayList();
    /*Fila de Threads para acordar os Clientes na ordem certa*/
    private static ArrayList<Thread> filaClientesThreads = new ArrayList();
    /*Lista usada para deixar os Clientes dormindo no final de cada Rodada*/
    private static ArrayList<Thread> auxThreadClientes = new ArrayList();
    /*Lista usada para deixar os Garçons dormindo no final de cada Rodada*/
    private static ArrayList<Thread> listaGarcomThreads = new ArrayList();
    /*Lista usada para deixar os Garçons dormindo no inicio de cada Rodada*/
    public static ArrayList<Thread> auxGarcomThreads = new ArrayList();
    
    /*Metodo Sincronizado para criar a ordem de Pedidos de cada Cliente*/
    public static synchronized void addPedidosCliente(int cliente, Thread cliThread, boolean fazerPedido){
        if(fazerPedido){
            filaPedidosClientes.add(cliente);
            auxClientes.add(cliente);
            filaClientesThreads.add(cliThread);        
            System.out.println("Cliente " + cliente + " esta fazendo Pedido.");
        }else{
            System.out.println("Cliente " + cliente + " pulou essa rodada.");
        }
        
        try {            
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("interrupcao " + e);
        }
    }
    
    /*Metodo Sincronizado para o Garçom anotar o Pedido de cada Cliente seguindo a ordem correta*/
    public static synchronized int anotarPedidos(int nomeGarcom){
        int nCli;
        
        /*Caso não tenha mais Pedidos o Metodo retorna 0(zero) para o Garçom continuar*/
        if(!auxClientes.isEmpty()){
            nCli = auxClientes.get(0);
            auxClientes.remove(0);
            System.out.println("\tO Garçom " + nomeGarcom + " esta anotando o pedido do Cliente " + nCli);
            try {            
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("interrupcao " + e);
            }
        }else{
            nCli = 0;
        }
        return nCli;
    }
    
    /*Metodo Sincronizado para acordar os Clientes na ordem correta para entregar o Pedido*/
    public static synchronized void acordaCliente(int nomeGarcom){        
        if(filaPedidosClientes.size() > 0){
            System.out.println("\t\t\tO Garçom  " + nomeGarcom + " esta entregando o pedido do Cliente " + filaPedidosClientes.get(0));            
            filaClientesThreads.get(0).interrupt();
            filaClientesThreads.remove(0);
            filaPedidosClientes.remove(0);
        }        
    }
    
    /*Metodo Sincronizado que Gerencia cada Rodada*/
    public static synchronized boolean fecharBar(Thread garThread){
        Garcom gar = new Garcom();
        
        /*Verifica se a Thread Garçom atual é ultima, caso contrario adiciona ela na Lista de Thread*/
        if(gar.getNumGarcons() - listaGarcomThreads.size() == 1){
            /*Subtrai o valor da Rodada para fazer o controle*/
            int rodadas = gar.getRodadas();            
            rodadas--;
            gar.setRodadas(rodadas);
            
            /*Caso a rodada chegue a zero o Run Garçom e Cliente pode parar de executar*/
            if(rodadas == 0){
                gar.setFechouBar(true);
                gar.setFimRodadas(false);
            }else{
                int contRodadas = gar.getContRodadas();            
                contRodadas++;
                gar.setContRodadas(contRodadas);
            }
            Garcom.flagGarcom = true;
            return true;
        } else {
            listaGarcomThreads.add(garThread);
            return false;
        }
    }
    
    /*Metodo Sincronizado usado pelo ultimo Cliente fazendo Pedido para acordar os Garçons pro inicio da Rodada*/
    public static synchronized void liberaGarcons(){
        /*Verifica se esse é o ultimo Cliente a fazer Pedido*/
        if(Garcom.numCliente - Cliente.contCli == 0){
            /*Espera todos os Clientes dormirem*/
            while(auxGarcomThreads.size() - Garcom.numGarcons != 0){
            }
            Garcom.flagGarcom = false;
            Cliente.contCli = 0;
            /*Acorda os Garçons*/
            for(int i = 0; i < auxGarcomThreads.size(); i++){
                auxGarcomThreads.get(i).interrupt();                
            }
        }
    }
    
    /*Metodo Sincronizado que retorna se existem Clientes para serem atendidos*/
    public static synchronized boolean quantidadeClientes(){        
        return auxClientes.size() > 0;
    }
    
    /*Metodo Sincronizado que popula a Lista de Thread Cliente*/
    public static synchronized void addAuxThread(Thread cliThread){
        auxThreadClientes.add(cliThread);
    }
    
    /*Metodo Sincronizado que popula a Lista de Thread de Garçom*/
    public static synchronized void addGarconsThread(Thread garThread){
        auxGarcomThreads.add(garThread);
    }
    
    /*Metodo Sincronizado que retorna se existem Clientes Bebendo*/
    public static synchronized int contAuxThread(){
        return auxThreadClientes.size();
    }
    
    /*Metodo Sincronizado que acorda os Clientes e Garçons dormindo e limpa suas listas para uma nova Rodada*/
    public static synchronized void liberaClientesEGarcons(){
        for(int i = 0; i < auxThreadClientes.size(); i++){
            auxThreadClientes.get(i).interrupt();            
        }
        auxThreadClientes.clear();
        
        for(int i = 0; i < listaGarcomThreads.size(); i++){
            listaGarcomThreads.get(i).interrupt();                
        }
        listaGarcomThreads.clear();
    }    
}
