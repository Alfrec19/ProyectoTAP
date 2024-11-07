/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package topicosproyecto;

/**
 *
 * @author Victor
 */
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class TopicosComponente extends JPanel {
    private JTextArea textArea;
    private JButton btnReproducir;
    private JButton btnAbrir; 
    private Voice voice;

    public TopicosComponente() {
        textArea = new JTextArea(40, 60);
        btnReproducir = new JButton("Reproducir");
        btnAbrir = new JButton("Abrir Archivo"); // Inicializar el botón

        // Configurar la voz
        VoiceManager voiceManager = VoiceManager.getInstance();
        voice = voiceManager.getVoice("kevin");

        if (voice == null) {
            JOptionPane.showMessageDialog(this, "No se pudo encontrar la voz.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            voice.allocate();
        }

        // Agregar el área de texto y los botones al panel
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new JScrollPane(textArea));
        add(btnAbrir); // Agregar el botón de abrir
        add(btnReproducir);

        // Configurar el evento del botón de reproducir
        btnReproducir.addActionListener(evt -> reproducirTexto());

        // Configurar el evento del botón de abrir
        btnAbrir.addActionListener(evt -> abrirArchivo());
    }

    public void reproducirTexto() {
        String texto = textArea.getText();
        if (!texto.isEmpty()) {
            voice.speak(texto);
        } else {
            JOptionPane.showMessageDialog(this, "No hay texto para reproducir.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Método para abrir un archivo y cargar su contenido en el JTextArea
    private void abrirArchivo() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File archivoSeleccionado = fileChooser.getSelectedFile();
            try (BufferedReader br = new BufferedReader(new FileReader(archivoSeleccionado))) {
                textArea.setText(""); // Limpiar el área de texto
                String linea;
                while ((linea = br.readLine()) != null) {
                    textArea.append(linea + "\n"); // Agregar cada línea al área de texto
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al abrir el archivo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Liberar recursos
    public void liberarVoz() {
        if (voice != null) {
            voice.deallocate();
        }
    }

    // Método para establecer texto externamente
    public void setText(String texto) {
        textArea.setText(texto);
    }

    // Método para obtener texto
    public String getText() {
        return textArea.getText();
    }

    // Método principal para ejecutar la aplicación
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Aplicación de Texto a Voz");
            TopicosComponente topicosComponente = new TopicosComponente();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(topicosComponente);
            frame.pack();
            frame.setSize(400, 300);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
