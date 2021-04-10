// Leonardo Valerio Morales 771030
// Exercicio de Redes, comunicacao entre processos por socket
// Cliente

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
		// try catch apenas existe para exibir a mensagem de erro caso o usuario cause uma excecao.
        try {
            // Instancia um socket de cliente na porta 12345
            Socket servidor = new Socket("localhost", 12345);
            System.out.println("Cliente eviando para porta 12345");

            // Cria Scanner para entrada do teclado
            Scanner teclado = new Scanner(System.in);
            // Le a proxima linha do teclado no formato char int int
			System.out.println("Escreva a Requisicao desejada no formato 'operacao numero1 numero2'");
            char op = teclado.next().charAt(0);
            int num1 = teclado.nextInt();
            int num2 = teclado.nextInt();
            teclado.close();
    
            // Cria a Stream de saida para o servidor, escreve um Byte com a operacao
            // e dois Ints com os operandos
            DataOutputStream saida = new DataOutputStream(servidor.getOutputStream());
            saida.writeByte(op);
            saida.writeInt(num1);
            saida.writeInt(num2);
			System.out.println(saida.size());
            // Flush para enviar.
            saida.flush();

            // Recebe uma resposta do servidor
            DataInputStream resposta_serv = new DataInputStream(servidor.getInputStream());

            // Separa o caracter de sucesso da resposta em si.
            char sucesso = (char) resposta_serv.readByte();
            int resposta = resposta_serv.readInt();

            if (sucesso == '=') {
                // Caso caracter de sucesso esteja valido exibe a resposta e envia uma confirmacao de sucesso ao servidor, e depois fecha a conexao.
                System.out.println("Resposta do Servidor Recebida, Operacao foi Sucesso!");
                System.out.println("Resultado da Operacao: " + resposta);
                System.out.println("Enviando Confirmacao de Sucesso ao Servidor...");
                saida.write('1');
                saida.close();
                servidor.close();
            } else if (sucesso == '?') {
                // Caso caracter de sucesso seja invalido apenas envia uma confirmacao de erro ao servidor, e depois fecha a conexao.
                System.out.println("Resposta do Servidor Recebida, Operacao nao Suportada!");
                System.out.println("Enviando Confirmacao de Falha ao Servidor...");
                saida.write('n');
                saida.close();
                servidor.close();
                return;
            } else {
                // Caso nao receba resposta, envia uma confirmacao de erro ao servidor, e depois fecha a conexao.
                System.out.println("Resposta do Servidor Nao Recebida!");
                System.out.println("Enviando Confirmacao de Falha ao Servidor...");
                saida.write('n');
                saida.close();
                servidor.close();
            }

        } catch (Exception e) {
            System.out.println("Erro:" + e.getMessage());
        }
    }
}
