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
public class Garcom extends Thread{
    /*Nome do Garçom e a Capacidade de Bebida que cada Garçom pode levar por vez*/
    int nome;
    int capacidade;
    /*Numero de Garçons, Clientes e Rodadas que foram informados no inicio*/
    static int numGarcons;
    static int numCliente;
    static int rodadas;
    /*Variavel de controle para contar as Rodadas*/
    static int contRodadas = 1;
    /*Variavel que vai definir quantas vezes o loop do Cliente e do Garçom vão rodar*/
    static boolean fechouBar = false;
    static boolean flagRodadas = true;
    /*Variavel usada para colocar os Garçons para dormir todo inicio de Rodada*/
    static boolean flagGarcom = true;
    
    /*Lista de o Garçom vai criar de cada Cliente atendido*/
    ArrayList<Integer> filaCliente = new ArrayList();
    
    /*Construtores*/
    public Garcom(){
        
    }
    
    public Garcom(int nome, int capacidade, int rodadas, int numGarcons, int numCliente) {
        this.nome = nome;
        this.capacidade = capacidade;
        Garcom.rodadas = rodadas;
        Garcom.numGarcons = numGarcons;
        Garcom.numCliente = numCliente;
    }
    
    /*Gets e Sets*/
    public int getNumGarcons(){
        return numGarcons;
    }
    
    public int getRodadas(){
        return rodadas;
    }
    
    public void setRodadas(int rodadas){
        Garcom.rodadas = rodadas;
    }
    
    public int getContRodadas(){
        return contRodadas;
    }
    
    public void setContRodadas(int contRodadas){
        Garcom.contRodadas = contRodadas;
    }
    
    public void setFechouBar(boolean fechouBar){
        Garcom.fechouBar = fechouBar;
    }
    
    public void setFimRodadas(boolean flagRodadas){
        Garcom.flagRodadas = flagRodadas;
    }
    
    /*Metodo para printar Rodada atual*/
    public static void printRodada(){
        if(flagRodadas){
            System.out.println("\n\t\t\t\t\tRODADA " + contRodadas + "!!!\n");
        }
    }
    
    /*Metodo para printar Final das Rodadas*/
    public static void printFim(){
        System.out.println("\n\t\t\t\t\tTERMINOU AS RODAS!!!!");
    }
    
    /*Metodo usado no Main para criar Lista de Threads de Garçons*/
    public void carregaListaGarcons(){
        Fila.addGarconsThread(this);        
    }
    
    /*Metodo usado para pegar o maximo de pedidos por vez*/
    public void recebeMaximoPedidos(){
        boolean flag = true;
        int nCli;
        
        /*Na primeira vez de cada Rodada os Garçons são colocados para dormir, para esperar os clientes fazerem pedido*/
        if(flagGarcom){
            synchronized(this){           
                try {            
                    this.wait();
                } catch (InterruptedException e) {                
                }
            }
        }
        
        /*Faz o loop até atender o maximo de Clientes possiveis usando a 'capacidade' como parametro ou até acabar os Clientes*/
        while(flag){
            if(filaCliente.size() < capacidade){
                nCli = Fila.anotarPedidos(nome);
                if(nCli != 0){
                    filaCliente.add(nCli);
                }else{
                    flag = false;
                }                
            }else{
                flag = false;
            }
        }        
    }
    
    /*Se o Garçom tem pedidos na sua Lista ele leva os pedidos para copa*/
    private void registraPedidos(){
        if(filaCliente.size() > 0){
            System.out.println("\t\tO Garçom " + nome + " esta levando os pedidos para copa");
            
            try {            
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                System.out.println("registraPedidos interrupcao " + e);
            }            
        }        
    }
    
    /*O Garçom acorda sempre o primeiro Cliente da Fila e entrega o Pedido*/
    private void entregaPedidos(){
        while(filaCliente.size() > 0){
            Fila.acordaCliente(nome);
            filaCliente.remove(0);
            try {            
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.out.println("entregaPedidos interrupcao " + e);
            }            
        }
        
        /*Se ainda existe Clientes para serem atendidos ele volta para os outros Metodos*/
        if(Fila.quantidadeClientes()){
            recebeMaximoPedidos();
            registraPedidos();
            entregaPedidos();
        }
    }
    
    /*Metodo usado para reiniciar a Rodada ou terminar de executar*/
    private void existemClientesNoBar(){
        /*Verifica se a thread atual é a ultima, caso não seja coloca ele pra Dormir*/
        if(Fila.fecharBar(this)){
            /*Espera todos os Clientes terminarem de Beber*/
            while(Fila.contAuxThread() - numCliente != 0){
            }
            /*Printa o Final da Rodada e o Inicio da proxima*/
            System.out.println("\n\t\t\tTODOS OS CLIENTES TERMINARAM DE BEBER!!!");
            printRodada();
            /*O ultimo Garçom acordar todas as Threads de Clientes e de Garçons que estavam dormindo*/
            Fila.liberaClientesEGarcons();
        }else{
            synchronized(this){           
                try {            
                    this.wait();
                } catch (InterruptedException e) {                
                }
            }
        }        
    }
    
    /*Metodo Run que vai ficar em loop até todas as Rodadas acabarem*/
    @Override
    public void run(){        
        while(!fechouBar){
            recebeMaximoPedidos();
            registraPedidos();
            entregaPedidos();
            existemClientesNoBar();            
        }        
    }
}
