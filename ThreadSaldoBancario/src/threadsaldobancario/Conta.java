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
public class Conta {
    private int saldo = 0;

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }
    
    private void ContaRetirada(int valor){
        saldo -= valor;
        System.out.println("\t\tValor da Retirada = " + valor + " Saldo após Retirada = " + saldo + "\n");
    }
    
    private void ContaDeposito(int valor){
        saldo += valor;
        System.out.println("Valor do Deposito = " + valor + " Saldo após Deposito = " + saldo + "\n");
    }
    
    public synchronized void ContaOperacao(int valor, boolean op){        
        System.out.println("\tSaldo Atual = " + saldo);
        if(op){
            ContaDeposito(valor);
        }else{
            ContaRetirada(valor);
        }        
    }
}
