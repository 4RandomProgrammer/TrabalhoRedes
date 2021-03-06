// Caio Peres
// Eduardo Ravagnani de Melo 771004
// Leonardo Valerio Morales
// Luís Felipe Dobner Henriques 771036
// Exercicio de Redes, comunicacao entre processos por socket
// Cliente

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class App {

    // -------> client aq <------

    public static void main(String[] args) throws Exception {

        Scanner entrada = new Scanner(System.in);
        String ip = null;
        Socket servidor = null;
        ServerSocket localServer;

        System.out.println("Qual seu Nome?");
        String nome = entrada.nextLine();

        System.out.println("Vc deseja conectar ou hostear a conexão?");
        System.out.println("1 - para se conectar");
        System.err.println("2 - para ser host");

        String conexao = entrada.nextLine();

        if(conexao.equalsIgnoreCase("1")){
            //cliente
            do {
                System.out.println("Digite o ip do host:");
                ip = entrada.nextLine();
                try {
                    servidor = new Socket(ip, 7777);
                } catch (UnknownHostException e) {
                    System.out.println("O Ip digitado nao foi encontrado, digite novamente.");
                } 
            } while (servidor == null);
            System.out.println("Cliente eviando para " + ip + ":77777");
        } else {
            //host
            localServer = new ServerSocket(7777);
            localServer.setSoTimeout(50000);
            do {
                try {
                    System.out.println("Tentando aceitar conexao...");
                    servidor = localServer.accept();
                } catch (SocketTimeoutException e) {
                    System.out.println("Tempo de conexao esgotado, para esperar novamente digite alguma tecla.");
                    entrada.nextLine();
                }
            } while (servidor == null);
            //notifica caso haja conexao
            System.out.println("Conectado com: " + servidor.getInetAddress().getHostAddress());

        }


        Runnable r2 = new EnviarData(servidor, entrada, nome);
        new Thread(r2).start();

        Runnable r3 = new ReceberData(servidor);
        new Thread(r3).start();

        
        //entrada.close();

    }
    
    public static class EnviarData implements Runnable {


        Socket servidor;
        Scanner entrada;
        String nome;

        public EnviarData(Socket servidor, Scanner entrada, String nome) {
            this.servidor = servidor;
            this.entrada = entrada;
            this.nome = nome;
        }
        
        @Override
        public void run(){
            OutputStream saida;
            try {
                saida = servidor.getOutputStream();
            } catch (IOException e1) {
                e1.printStackTrace();
                return;
            }
            System.out.println("Escreva uma mensagem :D");
            do {
                // Le a proxima linha do teclado no formato char int int
                String str_mensagem = entrada.nextLine();
                try {
                    //ESPERA DA UI O INPUT EM STRING string
                    if (str_mensagem.equalsIgnoreCase("sai bosta")) {
                        break;
                    }
                    char mensagem[] = new String(nome+": "+str_mensagem).toCharArray();
                    for (int i = 0; i < mensagem.length; i++) {
                        saida.write(mensagem[i]);        
                    }
                    // Flush para enviar.
                    saida.flush();   
                    
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } while (true);
            entrada.close();
        }
    };
    
    public static class ReceberData implements Runnable {

        Socket servidor;

        public ReceberData(Socket servidor) {
            this.servidor = servidor;
        }
        

        @Override
        public void run(){

            InputStream in;
            
            try {
                in = servidor.getInputStream();
            } catch (IOException e1) {
                e1.printStackTrace();
                return;
            }

            while (true) {
                try {
                    //estabelece a stream in para receber dados
    
                    while(in.available() == 0){
                        //so estamos esperando XD
                        Thread.sleep(10);
                    }
    
                    //le a mensagem envida
                    byte[] msg = new byte[500];
                    in.read(msg);
    
                    //convertendo mensagem para string
                    String s = new String(msg, StandardCharsets.UTF_8);
                    System.out.println(s);
    
                    //enviar para interface
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            //in.close();
        }
    };

    public class Bundinha {

    }
}

