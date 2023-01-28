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
        Socket sCliente = null;

        try
        {
            sCliente = new Socket(HOST, PUERTO);

            System.out.println("Conectado al servidor " + HOST + ":" + PUERTO);

            // Creo los flujos de entrada y salida
            DataInputStream fentrada = new DataInputStream(sCliente.getInputStream());
            DataOutputStream fsalida = new DataOutputStream(sCliente.getOutputStream());

            // Prompt the user for their login credentials
            Scanner input = new Scanner(System.in);
            while (true)
            {
                System.out.print("Usuario: ");
                String username = input.nextLine();
                System.out.print("Contraseña: ");
                String password = input.nextLine();

                // Send the client's login credentials to the server
                fsalida.writeUTF(username);
                fsalida.writeUTF(password);

                // Get the result of the login attempt from the server
                String response = fentrada.readUTF();

                // If the login attempt was successful, break out of the loop
                if (response.equals("1")) {
                    break;
                }
            }

            System.out.println("\nBienvenid@");
            while (true) {
                String result = fentrada.readUTF();
                System.out.println(result);

                String command = input.nextLine();
                fsalida.writeUTF(command);

                String result2 = fentrada.readUTF();
                System.out.println(result2);

                if(command.equals("exit")) {
                    break;
                }
            }
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
