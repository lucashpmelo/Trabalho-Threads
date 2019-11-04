/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threadbar;

import java.util.Random;
import static threadbar.Garcom.fechouBar;

/**
 *
 * @author lucas
 */
public class Cliente extends Thread{
    /*Nome do Cliente*/
    int nome;
    /*Contador estatico para registrar quantas Threads passaram pelo Metodo fazPedido()*/
    static int contCli = 0;
    /*Variavel para receber o valor randomizado para determinar se o Cliente vai beber na Rodada atual*/
    int fazerPedido;
    /*Recebe o Random da Main()*/
    Random ran;
    
    /*Construtores*/
    public Cliente() {        
    }
    
    public Cliente(int nome, Random ran) {
        this.nome = nome;
        this.ran = ran;
    }
    
    /*Metodo Sincronizado para adicionar 1 ao contCli*/
    private static synchronized void contaClientes(){
        contCli++;
    }
    
    /*Metodo para criar pedido do Cliente*/
    private void fazPedido(){
        /*Randomiza um valor entre 0 e 4, dando uma chance 80% para o Cliente beber na Rodada atual*/
        fazerPedido = ran.nextInt(5);
        
        /*Verifica se o Cliente vai Beber e cria o pedido*/
        if(fazerPedido != 4){
            Fila.addPedidosCliente(nome, this, true);
        }else{
            Fila.addPedidosCliente(nome, this, false);
        }
        
        /*Chama o Metodo para contar o numero de Threads que passou por esse Metodo*/
        contaClientes();
        /*Verifica se essa é a ultima Thread Cliente para poder liberar os Garçons*/
        Fila.liberaGarcons();
    }
    
    /*Metodo que vai colocar as Threads dos Clientes que fizeram o pedido para dormir*/
    private void esperaPedido(){
        if(fazerPedido != 4){
            synchronized(this){           
                try {            
                    this.wait();
                } catch (InterruptedException e) {                
                }
            }
        }
    }
    
    /*Metodo que vai receber o pedido logo após que o Garçom acordar a Thread do Cliente*/
    private void recebePedido(){        
        if(fazerPedido != 4){
            try {            
                System.out.println("\t\t\t\tCliente " + nome + " esta pegando o pedido.");
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                System.out.println("recebePedido interrupcao " + e);
            }
        }
    }
    
    /*Metodo que vai consumir o pedido com um tempo rantomizado entre 2000 a 4000*/
    private void consomePedido(){
        if(fazerPedido != 4){
            try {            
                System.out.println("\t\t\t\t\tCliente " + nome + " esta bebendo.");
                Thread.sleep(ran.nextInt(2001) + 2000);
            } catch (InterruptedException e) {
                System.out.println("consomePedido interrupcao " + e);
            }
        }
        
        /*Cria Fila de Threads 'auxThreadClientes' de Clientes que vão acabando de beber*/
        Fila.addAuxThread(this);
        
        /*Coloca todas as Threads de Cliente para dormir para esperar a proxima Rodada*/
        synchronized(this){           
            try {            
                this.wait();
            } catch (InterruptedException e) {                
            }
        } 
    }
    
    /*Metodo Run que vai ficar em loop até todas as Rodadas acabarem*/
    @Override
    public void run(){
        while(!fechouBar){
            fazPedido();
            esperaPedido();
            recebePedido();
            consomePedido();
        }
    }
}
