package servidor;

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

            // Envio un mensaje al cliente
            fsalida.writeUTF("Hola cliente, soy el servidor");
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
            catch (Exception x)
            {
                System.out.println("Error: " + x.getMessage());
            }
        }
    }
}