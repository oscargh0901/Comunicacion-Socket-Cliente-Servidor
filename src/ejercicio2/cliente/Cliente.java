package ejercicio2.cliente;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
    static final int PUERTO = 3500;
    static final String HOST = "localhost";

    public Cliente()
    {
        DataInputStream fentrada = null;
        DataOutputStream fsalida = null;
        Socket sCliente = null;

        try
        {
            sCliente = new Socket(HOST, PUERTO);

            System.out.println("Conectado al servidor " + HOST + ":" + PUERTO);

            // Creo los flujos de entrada y salida
            fentrada = new DataInputStream(sCliente.getInputStream());
            fsalida = new DataOutputStream(sCliente.getOutputStream());

            // Envio un mensaje al servidor
            fsalida.writeUTF("Hola servidor, soy el cliente");

            // Leo el mensaje del servidor
            String datos = fentrada.readUTF();

            boolean salirLogin = false;
            Scanner sc = new Scanner(System.in);

            do {
                // Muestro el mensaje del servidor
                System.out.println(datos);

                // Envio un mensaje al servidor
                String leerTexto = sc.nextLine();
                fsalida.writeUTF(leerTexto);

                datos = fentrada.readUTF();

                if(datos.contains("Bienvenido")) {
                    // he entrado correctamente
                    salirLogin = true;
                }
            } while (!salirLogin);

            boolean salirOrdenes = false;

            //

            // Cierro los flujos y el socket
            fentrada.close();
            fsalida.close();
        }
        catch (Exception e)
        {
            System.out.println("Error: " + e.getMessage());
        }
        finally
        {
            try
            {
                sCliente.close();
                System.out.println("Conexion finalizada");
                System.exit(0);
            }
            catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new Cliente();
    }
}
