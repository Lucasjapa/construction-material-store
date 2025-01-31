package projectpoo.construction_material_store.screens;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import java.awt.*;

public class DashboardScreen extends JPanel {

    private static final String API_URL_TOTAL_INVOICES = "http://localhost:8080/invoices/total-current-month"; // URL da API

    public DashboardScreen() {
        setLayout(new BorderLayout());

        JLabel label = new JLabel("Outra Tela", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));

        add(label, BorderLayout.CENTER);

        try {
            Thread.sleep(3000); // Atraso de 3 segundos
            // Chama o método para pegar o total de invoices
            long totalInvoices = getTotalInvoicesFromAPI();
            System.out.println("Total de invoices: " + totalInvoices);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private long getTotalInvoicesFromAPI() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        int maxAttempts = 3;
        int attempt = 0;
        long waitTime = 2000; // 2 seconds

        while (attempt < maxAttempts) {
            try {
                // Envia a requisição GET para obter o total de invoices do mês atual
                ResponseEntity<Long> response = restTemplate.exchange(API_URL_TOTAL_INVOICES, HttpMethod.GET, null, Long.class);

                if (response.getStatusCode().is2xxSuccessful()) {
                    return response.getBody();
                } else {
                    throw new Exception("Erro ao obter o total de invoices: " + response.getStatusCode());
                }
            } catch (Exception e) {
                attempt++;
                if (attempt >= maxAttempts) {
                    System.out.println("Erro ao fazer a requisição: " + e.getMessage());
                    e.printStackTrace();
                    throw e;
                }
                System.out.println("Tentativa " + attempt + " falhou, tentando novamente em " + waitTime / 1000 + " segundos...");
                Thread.sleep(waitTime);
            }
        }
        throw new Exception("Não foi possível obter o total de invoices após " + maxAttempts + " tentativas.");
    }

}
