package ejercicio2.servidor;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;

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
        try
        {
            // Defino las credenciales validas
            String[][] credentials = {
                    {"admin", "admin"},
                    {"user", "1234"}
            };

            // Inicializo el contador de intentos
            int tries = 0;

            // Creo los flujos de entrada y salida
            DataInputStream fentrada = new DataInputStream(sCliente.getInputStream());
            DataOutputStream fsalida = new DataOutputStream(sCliente.getOutputStream());

            while (tries < 3) {
                // leo las credenciales del cliente
                String username = fentrada.readUTF();
                String password = fentrada.readUTF();

                // Valido las credenciales
                boolean loginSuccess = false;
                for (String[] cred : credentials) {
                    if (username.equals(cred[0]) && password.equals(cred[1])) {
                        loginSuccess = true;
                        break;
                    }
                }

                // Envio el resultado de la validacion
                if (loginSuccess) {
                    fsalida.writeUTF("1");
                    break;
                } else {
                    fsalida.writeUTF("Credenciales incorrectas\n Intentalo de nuevo.");
                    tries++;
                }
            }

            if (tries == 3) {
                fsalida.writeUTF("Has realizado demasiados intentos\n Cerrando conexion.");
                sCliente.close();
                return;
            }

            // Muestro el menu de opciones
            while (true) {
                fsalida.writeUTF("\nIntroduce un comando (ls, cat, get, stop, time, exit):");
                String command = fentrada.readUTF();
                if (command.equals("ls")) {
                    // Codigo para listar los archivos y directorios del servidor
                    File folder = new File(".");
                    File[] listOfFiles = folder.listFiles();
                    for (int i = 0; i < listOfFiles.length; i++) {
                        if (listOfFiles[i].isFile()) {
                            fsalida.writeUTF("Archivo: " + listOfFiles[i].getName());
                        } else if (listOfFiles[i].isDirectory()) {
                            fsalida.writeUTF("Directorio: " + listOfFiles[i].getName());
                        } else {
                            fsalida.writeUTF("Presiona enter... ");
                        }
                    }
                } else if (command.startsWith("cat")) {
                    // Codigo para mostrar el contenido de un archivo
                    String[] parts = command.split(" ");
                    String fileName = parts[1];
                    Path path = Paths.get(fileName);
                    byte[] fileBytes = Files.readAllBytes(path);
                    String fileContent = new String(fileBytes, StandardCharsets.UTF_8);
                    fsalida.writeUTF(fileContent);
                } else if (command.startsWith("get")) {
                    // Codigo para descargar un archivo
                    String[] parts = command.split(" ");
                    String fileName = parts[1];
                    String destination = parts[2];
                    InputStream inStream = new FileInputStream(fileName);
                    OutputStream outStream = new FileOutputStream(destination);
                    byte[] buffer = new byte[4096];
                    int length;
                    while ((length = inStream.read(buffer)) > 0) {
                        outStream.write(buffer, 0, length);
                    }
                    inStream.close();
                    outStream.close();
                    fsalida.writeUTF("Descarga completada!");
                } else if (command.equals("stop")) {
                    // Codigo para parar el servidor
                    fsalida.writeUTF("Se ha parado el servidor ...");
                    System.exit(0);
                } else if (command.equals("time")) {
                    // Codigo para medir el tiempo de respuesta
                    Instant start = Instant.now();
                    Instant end = Instant.now();
                    Duration timeElapsed = Duration.between(start, end);
                    fsalida.writeUTF("Tiempo tomado: " + timeElapsed.toMillis() + " ms");
                } else if (command.equals("exit")) {
                    // Codigo para salir del programa
                    fsalida.writeUTF("Apagando el programa...");
                    break;
                } else {
                    fsalida.writeUTF("Comando no reconocido");
                }
            }

            // Cierro los flujos
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
                // Cierro el socket
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
