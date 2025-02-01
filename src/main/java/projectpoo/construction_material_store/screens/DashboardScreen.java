package projectpoo.construction_material_store.screens;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import java.awt.*;

public class DashboardScreen extends JPanel {

    private static final String API_URL_TOTAL_INVOICES = "http://localhost:8080/dashboard/total-current-month";
    private static final String API_URL_TOP3 = "http://localhost:8080/dashboard/top3";
    private JLabel label;

    public DashboardScreen() {
        setLayout(new BorderLayout());

        label = new JLabel("Carregando o dashboard...", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));

        add(label, BorderLayout.CENTER);

        // Executar a requisição em segundo plano
        new LoadInvoicesWorker().execute();

    }

//    public DashboardScreen() {
//        setLayout(new BorderLayout());
//
//        totalInvoicesLabel = new JLabel("Total de faturas: Carregando...", SwingConstants.CENTER);
//        totalInvoicesLabel.setFont(new Font("Arial", Font.BOLD, 24));
//
//        add(totalInvoicesLabel, BorderLayout.CENTER);
//
//        // Carregar o total de faturas na inicialização
//        updateTotalInvoices();
//    }

    public class LoadInvoicesWorker extends SwingWorker<Long, Void> {
        @Override
        protected Long doInBackground() throws Exception {
            if (!isApiAvailable()) {
                return 0L; // API não ficou pronta
            }

            try {
                RestTemplate restTemplate = new RestTemplate();
                return restTemplate.getForObject(API_URL_TOTAL_INVOICES, Long.class);
            } catch (Exception e) {
                e.printStackTrace();
                return 0L;
            }
        }

        @Override
        protected void done() {
            try {
                long totalInvoices = get();
                label.setText("Total de vendas no mês atual: " + totalInvoices);
            } catch (Exception e) {
                label.setText("Erro ao carregar dashboard");
            }
        }
    }

    private boolean isApiAvailable() {
        RestTemplate restTemplate = new RestTemplate();
        int maxRetries = 10;
        int waitTime = 3000;

        for (int i = 0; i < maxRetries; i++) {
            try {
                ResponseEntity<String> response = restTemplate.getForEntity(API_URL_TOTAL_INVOICES, String.class);
                if (response.getStatusCode().is2xxSuccessful()) {
                    return true;
                }
            } catch (Exception e) {
                System.out.println("API ainda não está pronta. Tentando novamente...");
                try {
                    Thread.sleep(waitTime);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return false;
    }

    public void updateTotalInvoices() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            Long totalInvoices = restTemplate.getForObject(API_URL_TOTAL_INVOICES, Long.class);

            // Trata caso a API retorne null
            long total = (totalInvoices != null) ? totalInvoices : 0;

            SwingUtilities.invokeLater(() -> label.setText("Total de faturas: " + total));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao atualizar total de faturas.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}