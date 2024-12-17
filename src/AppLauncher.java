package src;

import javax.swing.*;

public class AppLauncher {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new weatherapp().setVisible(true);

                // System.out.println(ConsultaClima.datosLocalidad("Tokyo"));

                System.out.println(ConsultaClima.consultarHoraActual());
            }
        });
    }
}
