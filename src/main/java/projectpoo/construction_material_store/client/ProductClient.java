package projectpoo.construction_material_store.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ProductClient {

    // Método para buscar todos os produtos (GET)
    public static void getAllProducts() throws Exception {
        URL url = new URL("http://localhost:8080/products");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        // Leitura da resposta
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println("Resposta da API: " + response.toString());
    }

    // Método para criar um produto (POST)
    public static void createProduct(String name, double quantity) throws Exception {
        URL url = new URL("http://localhost:8080/products");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);

        // Dados do produto em formato JSON
        String jsonInputString = "{\"name\": \"" + name + "\", \"quantity\": " + quantity + "}";

        // Envio da requisição
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // Leitura da resposta
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println("Produto criado: " + response.toString());
    }

    public static void main(String[] args) throws Exception {
        // Exemplo de consumo da API
        getAllProducts();
        createProduct("New Product", 10.5);
    }
}
