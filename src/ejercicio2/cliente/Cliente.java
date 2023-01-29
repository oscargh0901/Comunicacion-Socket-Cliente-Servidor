package ejercicio2.cliente;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
    static final int PUERTO = 3500;
    static final String HOST = "localhost"; // recordar cambiar por la IP del servidor

    public Cliente()
    {
        try
        {
            Socket sCliente = new Socket(HOST, PUERTO);

            System.out.println("Conectado al servidor " + HOST + ":" + PUERTO);

            // Creo los flujos de entrada y salida
            DataInputStream fentrada = new DataInputStream(sCliente.getInputStream());
            DataOutputStream fsalida = new DataOutputStream(sCliente.getOutputStream());

            // Verifico el login
            Scanner input = new Scanner(System.in);
            while (true)
            {
                System.out.print("Usuario: ");
                String username = input.nextLine();
                System.out.print("Contraseña: ");
                String password = input.nextLine();

                // Envio el usuario y la contraseña al servidor
                fsalida.writeUTF(username);
                fsalida.writeUTF(password);

                // obtener la respuesta del servidor
                String response = fentrada.readUTF();

                // Si la respuesta es 1, el login es correcto
                if (response.equals("1")) {
                    break;
                }
            }

            // Si el login es correcto, se muestra el menú
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
