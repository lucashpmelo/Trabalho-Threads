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
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Conta c = new Conta();
        Thread d = new Deposito(c);
        Thread r = new Retirada(c);
        
        d.start();
        r.start();
        
        try {
            d.join();
            r.join();
        } catch (InterruptedException e) {
            System.out.println("interrupcao" + e);
        }
        
        System.out.println("Saldo = " + c.getSaldo());
    }
}
