package src;

import org.json.simple.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class weatherapp extends JFrame {

    private JSONObject dataClima;
    public weatherapp(){

        //Estructura de la app
        super ("Weather App");

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setSize(500, 750);

        setLocationRelativeTo(null);

        setLayout(null);

        setResizable(false);

        addGuiComponents();

    }

    private void addGuiComponents() {

        //diseño de la app
        JTextField cuadroDeBusqueda = new JTextField();

        cuadroDeBusqueda.setBounds(15, 15, 350, 45);

        cuadroDeBusqueda.setFont(new Font("Dialog", Font.PLAIN, 24));

        add(cuadroDeBusqueda);

        JButton botonBusqueda = new JButton(loadImage("src/assets/search.png"));

        JLabel imagenDeCondicion = new JLabel(loadImage("src/assets/cloudy.png"));
        imagenDeCondicion.setBounds(0, 130, 450, 230);
        add(imagenDeCondicion);

        JLabel textoTemperatura = new JLabel("10 C");
        textoTemperatura.setBounds(0, 350, 450, 55);
        textoTemperatura.setFont(new Font("Dialog", Font.BOLD, 48));

        textoTemperatura.setHorizontalAlignment(SwingConstants.CENTER);
        add(textoTemperatura);

        JLabel condicionClima = new JLabel("Nublado");
        condicionClima.setBounds(0, 410, 450, 35);
        condicionClima.setFont(new Font("Dialog", Font.BOLD, 48));
        condicionClima.setHorizontalAlignment(SwingConstants.CENTER);
        add(condicionClima);

        JLabel imagenHumedad =new JLabel(loadImage("src/assets/humidity.png"));
        imagenHumedad.setBounds(15, 500, 75, 66);
        add(imagenHumedad);

        JLabel textoHumedad = new JLabel("<html><b>Humedad</b> 100%</html>");
        textoHumedad.setBounds(95, 500, 85, 55);
        textoHumedad.setFont(new Font("Dialog", Font.BOLD, 16));
        add(textoHumedad);

        JLabel imagenViento = new JLabel(loadImage("src/assets/windspeed.png"));
        imagenViento.setBounds(220, 500, 75, 66);
        add(imagenViento);

        JLabel textoViento = new JLabel("<html><b>Viento</b> 15km/h</html>");
        textoViento.setBounds(310, 500, 85, 55);
        textoViento.setFont(new Font("Dialog", Font.BOLD, 16));
        add(textoViento);

        botonBusqueda.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        botonBusqueda.setBounds(375, 15, 45, 45);
        botonBusqueda.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputUsuario = cuadroDeBusqueda.getText();

                if (inputUsuario.replaceAll("\\s", "").length() <= 0){
                    return;
                }

                dataClima = ConsultaClima.obtenerClima(inputUsuario);

                String condicionClimas = (String) dataClima.get("condicion_clima");

                switch (condicionClimas){
                    case "Clear":
                        imagenDeCondicion.setIcon(loadImage("src/assets/clear.png"));
                        break;
                    case "Cloudy":
                        imagenDeCondicion.setIcon(loadImage("src/assets/cloudy.png"));
                        break;
                    case "Rain":
                        imagenDeCondicion.setIcon(loadImage("src/assets/rain.png"));
                        break;
                    case "Snow":
                        imagenDeCondicion.setIcon(loadImage("src/assets/snow.png"));
                        break;
                }

                double temperatura = (double) dataClima.get("temperatura");
                textoTemperatura.setText(temperatura + " C");

                condicionClima.setText(condicionClimas);

                long humedad = (long) dataClima.get("humedad");
                textoHumedad.setText("<html><b>Humedad</b> " + humedad + "</html>");

                double velocidadViento = (double) dataClima.get("velocidad_viento");
                textoViento.setText("<html><b>Viento</b>" + velocidadViento + "km/h</html>");
            }
        });
        add(botonBusqueda);



    }

    private ImageIcon loadImage(String direccionRecurso){
        try{
            BufferedImage image = ImageIO.read(new File(direccionRecurso));

            return new ImageIcon(image);
        } catch (IOException e){
            e.printStackTrace();
        }

        System.out.println("No se pudo encontrar el recurso");
        return null;
    }
}
