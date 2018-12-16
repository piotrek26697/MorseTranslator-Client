package pl.polsl.connection;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;

/**
 * Class object is responsible for connection to the server
 *
 * @author Piotr Musioł
 * @version 1.0
 */

public class TCPClient implements Closeable
{
    /**
     * server's IP address
     */
    private InetAddress serverAddress;

    /**
     * server's port
     */
    private int serverPort;

    /**
     * client's socket
     */
    private Socket socket;

    /**
     * buffer from which data is being read
     */
    private BufferedReader input;

    /**
     * buffer to send data
     */
    private PrintWriter output;


    public TCPClient() throws IOException
    {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream(".properties"))
        {
            properties.load(input);
            serverPort = Integer.parseInt(properties.getProperty("port"));
            serverAddress = InetAddress.getByName(properties.getProperty("address"));
        } catch (IOException e)
        {
            System.err.println(e.getMessage());
        }

        socket = new Socket(serverAddress, serverPort);
        output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    /**
     * Method used to send data to server
     *
     * @param text   text to code or decode
     * @param choice choice between coding and decoding
     */
    public void sendText(String text, int choice)
    {
        switch (choice)
        {
            case 1:
                output.println("c " + text);
                break;
            case 2:
                output.println("d " + text);
                break;
            case 3:
                output.println("q");
                break;
        }
    }

    /**
     * Method used to get processed data from server
     *
     * @return translated data
     */
    public String getText()
    {
        String temp = "";
        try
        {
            temp = input.readLine();
        } catch (IOException e)
        {
            System.err.println(e.getMessage());
        }
        return temp;
    }

    /**
     * Closing stream and releasing any system resources
     *
     * @throws IOException if an I/O error occur
     */
    @Override
    public void close() throws IOException
    {
        Properties properties = new Properties();
        properties.setProperty("port", Integer.toString(serverPort));
        String temp = serverAddress.toString();
        properties.setProperty("address", temp.substring(1));
        try (FileOutputStream output = new FileOutputStream(".properties"))
        {
            properties.store(output, "Client Config");
        } catch (IOException e)
        {
            System.err.println(e.getMessage());
        }

        input.close();
        output.close();
        if (socket != null)
            socket.close();
    }
}
