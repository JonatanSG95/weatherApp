package src;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;


//Consulta en la api el estado del clima
public class ConsultaClima {
    public static JSONObject obtenerClima(String localidad){
        JSONArray infoLocalidad = datosLocalidad(localidad);

        JSONObject ubicacion = (JSONObject) infoLocalidad.get(0);
        double latitud = (double) ubicacion.get("latitude");
        double longitud = (double) ubicacion.get("longitude");

        String urlString = "https://api.open-meteo.com/v1/forecast?" +
                "latitude=" + latitud + "&longitude=" + longitud +
                "&hourly=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m&timezone=America%2FSao_Paulo";

        try {
            HttpURLConnection conexion = tomarRespuestaApi(urlString);

            if (conexion.getResponseCode() != 200){
                System.out.println("Error: No se pudo conectar con la API");
                return null;
            }

            StringBuilder resultadoJson = new StringBuilder();
            Scanner escaner = new Scanner(conexion.getInputStream());
            while (escaner.hasNext()){
                resultadoJson.append(escaner.nextLine());
            }

            escaner.close();

            conexion.disconnect();

            JSONParser parser = new JSONParser();
            JSONObject resultadoJsonObj = (JSONObject) parser.parse(String.valueOf(resultadoJson));

            JSONObject hourly = (JSONObject) resultadoJsonObj.get("hourly");

            JSONArray time = (JSONArray) hourly.get("time");
            int index = encontrarHoraActual(time);

            JSONArray dataTemperatura = (JSONArray) hourly.get("temperature_2m");
            double temepreatura = (double) dataTemperatura.get(index);

            JSONArray codigoClima = (JSONArray) hourly.get("weather_code");
            String condicionClima = convertirCodigoClima((long) codigoClima.get(index));

            JSONArray humedadRelativa = (JSONArray) hourly.get("relative_humidity_2m");
            long humedad = (long) humedadRelativa.get(index);

            JSONArray velocidadViento = (JSONArray) hourly.get("wind_speed_10m");
            double vientoVel = (double) velocidadViento.get(index);

            JSONObject dataClima = new JSONObject();
            dataClima.put("temperatura", temepreatura);
            dataClima.put("condicion_clima", condicionClima);
            dataClima.put("humedad", humedad);
            dataClima.put("velocidad_viento", vientoVel);

            return dataClima;




        }catch (Exception e){
            e.printStackTrace();
        }


        return null;
    }

    private static String convertirCodigoClima(long codigoClima) {
        String condicionClima = "";
        if (codigoClima == 0){
            condicionClima = "Clear";
        } else if (codigoClima <= 3 && codigoClima > 0) {
            condicionClima = "Cloudy";
        } else if ((codigoClima >= 51 && codigoClima <= 67) || (codigoClima >= 80 && codigoClima <= 99)){
            condicionClima = "Rain";
        } else if (codigoClima >= 71 && codigoClima <= 77) {
            condicionClima = "Snow";
        }

        return condicionClima;
    }

    public static JSONArray datosLocalidad(String localidad){
        localidad = localidad.replaceAll(" ", "+");

        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name=" + localidad + "&count=10&language=en&format=json";

        try {
            HttpURLConnection conexion = tomarRespuestaApi(urlString);

            if (conexion.getResponseCode() != 200) {
                System.out.println("Error, no se pudo conectar a la API");
                return null;
            }else {
                StringBuilder resultadoJson = new StringBuilder();
                Scanner escaner = new Scanner(conexion.getInputStream());
                while (escaner.hasNext()){
                    resultadoJson.append(escaner.nextLine());
                }

                escaner.close();

                conexion.disconnect();

                JSONParser parser = new JSONParser();
                JSONObject resultadoJsonObj = (JSONObject) parser.parse(String.valueOf(resultadoJson));

                JSONArray infoLocalidad = (JSONArray) resultadoJsonObj.get("results");

                return infoLocalidad;
            }


        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    private static HttpURLConnection tomarRespuestaApi(String urlString){
        try {
            URL url = new URL(urlString);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();

            conexion.setRequestMethod("GET");

            conexion.connect();
            return conexion;
        }catch (IOException e){
            e.printStackTrace();
        }

        return null;
    }

    private static int encontrarHoraActual(JSONArray listaHora){
        String horaActual = consultarHoraActual();

        for (int i = 0; i < listaHora.size(); i++){
            String time = (String) listaHora.get(i);
            if (time.equalsIgnoreCase(horaActual)){

                return i;
            }
        }

        return 0;
    }

    public static String consultarHoraActual(){
        LocalDateTime horaActual = LocalDateTime.now();

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':00'");

        String horaConFormato = horaActual.format(formato);

        return horaConFormato;
    }
}
