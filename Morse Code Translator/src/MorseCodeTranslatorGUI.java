import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MorseCodeTranslatorGUI extends JFrame implements KeyListener {
    private MorseCodeController morseCodeController;

    // text input area - user input area ( text to be translated )
    // morse code area -  translated text into morse code
    private JTextArea textInputArea, morseCodeArea;

    public MorseCodeTranslatorGUI() {
        // Basically adds text to the title bar
        super("Morse Code Translator");

        // Set the size og the frame
        setSize(540, 760);

        // Prevent GUI from being able to be resized
        setResizable(false);

        // Setting the layout null, allows us to manually position and set the size of the components in our GUI
        setLayout(null);

        // Exits program when closing the GUI
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Change the color of the background
        getContentPane().setBackground(Color.decode("#264653"));

        // Places the GUI in the center of the screen
        setLocationRelativeTo(null);

        morseCodeController = new MorseCodeController();
        addGuiComponents();
    }

    private void addGuiComponents() {

        JLabel titleLabel = new JLabel("Morse Code Translator");

        // Changes the font size for the label and the weight
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 32));

        // Change the font color of the text to white
        titleLabel.setForeground(Color.WHITE);

        // Centers the text
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Sets the x,y position and the width and height dimensions
        // to make sure the title aligns to the center of our gui
        // we made the same width
        titleLabel.setBounds(-10, 0, 540, 100);

        // text input
        JLabel textInputLabel = new JLabel("Text:");
        textInputLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        textInputLabel.setForeground(Color.WHITE);
        textInputLabel.setBounds(20, 100, 200, 30);

        textInputArea = new JTextArea();
        textInputArea.setFont(new Font("Dialog", Font.BOLD, 18));

        // makes it so that we are listening to key presses whenever we are typing in this text area
        textInputArea.addKeyListener(this);

        // simulates padding of 10px in the text area
        textInputLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // makes it so that words wrap to the next line after reaching the end of the text area
        textInputArea.setLineWrap(true);

        // makes is so that when the words do get wrap, the words doesn't split off
        textInputArea.setWrapStyleWord(true);

        // adds scrolling ability to input text area
        JScrollPane textInputScroll = new JScrollPane(textInputArea);
        textInputScroll.setBounds(20, 132, 484, 236);

        // morse code input
        JLabel morseCodeInputLabel = new JLabel("Morse Code:");
        morseCodeInputLabel.setFont(new Font("Dialog", Font.PLAIN, 16));
        morseCodeInputLabel.setForeground(Color.WHITE);
        morseCodeInputLabel.setBounds(20, 390, 200, 30);

        morseCodeArea = new JTextArea();
        morseCodeArea.setFont(new Font("Dialog", Font.PLAIN, 18));
        morseCodeArea.setEditable(false);
        morseCodeArea.setLineWrap(true);
        morseCodeArea.setWrapStyleWord(true);
        morseCodeArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Adds scrolling ability to morse code text area
        JScrollPane morseCodeScroll = new JScrollPane(morseCodeArea);
        morseCodeScroll.setBounds(20, 400, 484, 236);

        // play sound button
        JButton playSoundButton = new JButton("Play Sound");
        playSoundButton.setBounds(210, 680, 100, 30);
        playSoundButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // disable the play button
                playSoundButton.setEnabled(false);

                Thread playMorseCodeThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // ATTEMPT TO PLAY THE MORSE CODE SOUND
                        try{
                            String[] morseCodeMessage = textInputArea.getText().split(" ");
                            morseCodeController.playSound(morseCodeMessage);
                        } catch (LineUnavailableException lineUnavailableException) {
                            lineUnavailableException.printStackTrace();
                        }catch (InterruptedException interruptedException) {
                            interruptedException.printStackTrace();
                        }
                        finally {
                            // enable sound button
                            playSoundButton.setEnabled(true);
                        }
                    }
                });
                playMorseCodeThread.start();
            }
        });

        // add to gui
        add(titleLabel);
        add(textInputLabel);
        add(textInputScroll);
        add(morseCodeInputLabel);
        add(morseCodeScroll);
        add(playSoundButton);
    }

    @Override
    public void keyTyped(KeyEvent e){

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        // ignore shift key press
        if(e.getKeyCode() != KeyEvent.VK_SHIFT){
            // retrieve text input
            String inputText = textInputArea.getText();

            // Update the GUI with the translated text
            morseCodeArea.setText(morseCodeController.translateToMorse(inputText));
        }
    }
}
