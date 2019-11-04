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
public class Cliente extends Thread{
    private int nome;
    private boolean fim = false;
    Random ran;
    
    private static ArrayList<Thread> barbeiroThread = new ArrayList();
    
    public Cliente(int nome, Random ran){
        this.nome = nome;
        this.ran = ran;
    }
    
    public static void barbeiroDormindo(Thread barThread){
        barbeiroThread.add(barThread);
    }
    
    public static synchronized void acordarBarbeiro(){
        if(barbeiroThread.size() > 0){
            barbeiroThread.get(0).interrupt();
            barbeiroThread.remove(0);
        }
    }
    
    public void ChegadaCliente() {
        try{
            Thread.sleep(ran.nextInt(8001));
        } catch (InterruptedException e){
            System.out.println("interrupcao " + e);
        }
        
        System.out.println("Chegou Cliente: " + nome);
        
        if(Barbeiro.addClientesFila(nome)){
            System.out.println("Cliente: " + nome + " sentou");
            acordarBarbeiro();
            fim = true;
        }else{
            System.out.println("Cliente: " + nome + " saiu por falta de cadeiras");
        }
    }
    
    public void run() {                
        while(!fim) {            
            ChegadaCliente();            
        }
    }
}
