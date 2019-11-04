/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threadsaldobancario;

/**
 *
 * @author lucas
 */
public class Deposito extends Thread{
    private Conta c;

    public Deposito() {
    }

    public Deposito(Conta c) {
        this.c = c;
    }
    
    public void run(){        
        int vlrs[] = {40, 50, 60, 10, 20, 30};
        
        for(int i=0; i < vlrs.length; i++){
            c.ContaOperacao(vlrs[i],true);            
        }
    }
}
