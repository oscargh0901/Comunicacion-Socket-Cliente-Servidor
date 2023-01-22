package ejercicio1.servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor extends Thread {

    Socket sCliente;
    static final int PUERTO = 3500;

    public Servidor(Socket sCliente)
    {
        this.sCliente = sCliente;
    }

    public static void main(String[] args) {

        try
        {
            // Arranco el servidor en el puerto 3500
            ServerSocket sServidor = new ServerSocket(PUERTO);
            System.out.println("Servidor arrancado en el puerto " + PUERTO);

            while (true)
            {
                // Espero a que se conecte un cliente
                Socket sCliente = sServidor.accept();
                System.out.println("Cliente conectado desde " + sCliente.getInetAddress() + ":" + sCliente.getPort());
                // Creo un hilo para atender al cliente
                Servidor hilo = new Servidor(sCliente);
                hilo.start();
            }
        }
        catch(Exception e)
        {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void run()
    {
        DataInputStream fentrada = null;
        DataOutputStream fsalida = null;

        try
        {
            // Creo los flujos de entrada y salida
            fentrada = new DataInputStream(sCliente.getInputStream());
            fsalida = new DataOutputStream(sCliente.getOutputStream());

            // Leo el mensaje del cliente
            String mensaje = fentrada.readUTF();
            System.out.println("Mensaje recibido: " + mensaje);

            // Creo un numero entero aleatorio (1-100) secreto para el cliente
            int numero = (int) (Math.random() * 100) + 1;
            System.out.println("Numero secreto: " + numero);

            // Envio un mensaje al cliente
            fsalida.writeUTF("Tengo un numero secreto entre 1 y 100, adivinalo...");

            boolean acertado = false;

            do {
                // Leo el numero que me envia el cliente
                int numeroCliente = Integer.parseInt(fentrada.readUTF());

                System.out.println("Numero recibido: " + numeroCliente);

                // Compruebo si el cliente quiere salir
                if(numeroCliente == -777)
                {
                    fsalida.writeUTF("Has apagado el servidor");
                    System.exit(0);
                }

                // Compruebo si el numero es correcto
                if(numeroCliente == numero)
                {
                    fsalida.writeUTF("¡Has acertado el numero!");
                    acertado = true;
                }
                else
                {
                    if(numeroCliente > numero)
                    {
                        fsalida.writeUTF("El numero es menor");
                        acertado = false;
                    }
                    else
                    {
                        fsalida.writeUTF("El numero es mayor");
                        acertado = false;
                    }
                }
            }while (!acertado);

        }
        catch (Exception e)
        {
            System.out.println("Error: " + e.getMessage());
        }
        finally {
            try
            {
                // Cierro los flujos y el socket
                fentrada.close();
                fsalida.close();
                sCliente.close();
                System.out.println("Cliente desconectado");
            }
            catch (Exception ex)
            {
                System.out.println("Error: " + ex.getMessage());
            }
        }
    }
}