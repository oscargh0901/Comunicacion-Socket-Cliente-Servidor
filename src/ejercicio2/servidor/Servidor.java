package ejercicio2.servidor;

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

            String nombreUsuario = "";
            String contrasena = "";
            int intentos = 0;

            // Solicito el inicio de sesion
            do {
                if (intentos > 3)
                {
                    throw new Exception("Demasiados intentos fallidos");
                }

                fsalida.writeUTF("Introduzca su nombre de usuario: ");
                nombreUsuario = fentrada.readUTF();

                fsalida.writeUTF("Introduzca su contraseña: ");
                contrasena = fentrada.readUTF();

                if(!nombreUsuario.contains("javier") && !contrasena.contains("secreta"))
                {
                    fsalida.writeUTF("Usuario o contraseña incorrectos");
                    System.out.println("Intentos: " + intentos);
                    intentos++;
                }
            }while (!nombreUsuario.contains("javier") && !contrasena.contains("secreta"));

            fsalida.writeUTF("Bienvenido " + nombreUsuario);
        }
        catch (Exception e)
        {
            System.out.println("Error: " + e.getMessage());
        }
        finally
        {
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
