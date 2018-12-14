package pl.polsl.connection;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;

public class TCPClient implements Closeable
{
    private InetAddress serverAddress;
    private int serverPort;
    private Socket socket;
    private BufferedReader input;
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
        }
    }

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

        if (socket != null)
            socket.close();
    }
}
