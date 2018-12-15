package pl.polsl.control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import pl.polsl.connection.TCPClient;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Class object controls flow of data between Model and View
 *
 * @author Piotr Musioł
 * @version 2.0
 */

public class Controller implements Initializable
{
    /**
     * input text field
     */
    @FXML
    private TextField inputText;
    @FXML
    /**
     * button used for coding message into Morse code
     */
    private Button codeBtn;
    /**
     * button used for decoding message into Polish
     */
    @FXML
    private Button decodeBtn;
    /**
     * output text field
     */
    @FXML
    private Label outputText;

    /**
     * object responsible for communication with server
     */
    private TCPClient client;

    /**
     * Referencing buttons to the handler method
     *
     * @param location  The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        codeBtn.setOnAction(this::translateToMorse);
        decodeBtn.setOnAction(this::translateToPolish);

        try
        {
            client = new TCPClient();
        } catch (IOException e)
        {
            System.err.println(e.getMessage());
            outputText.setText("No Connection");
        }
    }

    /**
     * Coding button handler
     *
     * @param event An Event representing some type of action.
     */
    private void translateToMorse(ActionEvent event)
    {
        client.sendText(inputText.getText(), 1);
        outputText.setText(client.getText());
    }

    /**
     * Decoding button handler
     *
     * @param event An Event representing some type of action.
     */
    private void translateToPolish(ActionEvent event)
    {
        client.sendText(inputText.getText(), 2);
        outputText.setText(client.getText());
    }
}

