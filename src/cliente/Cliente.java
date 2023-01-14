package cliente;

import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;

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
            String datos = fentrada.readUTF();
            System.out.println("Mensaje recibido: " + datos);
        }
        catch (Exception e)
        {
            System.out.println("Error: " + e.getMessage());
        }
        finally
        {
            try
            {
                fentrada.close();
                fsalida.close();
                sCliente.close();
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
