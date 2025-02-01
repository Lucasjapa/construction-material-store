package projectpoo.construction_material_store.screens;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import projectpoo.construction_material_store.dto.DashboardDTO;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class DashboardScreen extends JPanel {

    private static final String API_URL_TOTAL_INVOICES = "http://localhost:8080/dashboard/total-current-month";
    private static final String API_URL_TOP3 = "http://localhost:8080/dashboard/top3";
    private static final String API_URL = "http://localhost:8080/dashboard";
    private JLabel label;     // Para exibir o total de faturas
    private JLabel labelProduct1; // Para exibir o primeiro produto
    private JLabel labelProduct2; // Para exibir o segundo produto
    private JLabel labelProduct3; // Para exibir o terceiro produto
    private JLabel titleLabel; // Título para a lista de produtos

    public DashboardScreen() {
        // Layout vertical com centralização
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Centralizar o conteúdo
        setAlignmentX(CENTER_ALIGNMENT);

        // Primeira JLabel para o total de faturas
        label = new JLabel("Carregando dashboard...", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setAlignmentX(CENTER_ALIGNMENT); // Centraliza a label

        // Título para os produtos
        titleLabel = new JLabel("3 Produtos Mais Vendidos", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(CENTER_ALIGNMENT); // Centraliza o título

        // JLabels para os produtos
        labelProduct1 = new JLabel("Produto 1: ", SwingConstants.CENTER);
        labelProduct1.setFont(new Font("Arial", Font.PLAIN, 18));
        labelProduct1.setAlignmentX(CENTER_ALIGNMENT); // Centraliza a label

        labelProduct2 = new JLabel("Produto 2: ", SwingConstants.CENTER);
        labelProduct2.setFont(new Font("Arial", Font.PLAIN, 18));
        labelProduct2.setAlignmentX(CENTER_ALIGNMENT); // Centraliza a label

        labelProduct3 = new JLabel("Produto 3: ", SwingConstants.CENTER);
        labelProduct3.setFont(new Font("Arial", Font.PLAIN, 18));
        labelProduct3.setAlignmentX(CENTER_ALIGNMENT); // Centraliza a label

        // Adicionando as labels ao layout
        add(label);           // Primeira label (total de faturas)
        add(Box.createVerticalStrut(10)); // Espaçamento entre as labels
        add(titleLabel);      // Título para os produtos
        add(Box.createVerticalStrut(10)); // Espaçamento entre o título e os produtos
        add(labelProduct1);   // Segunda label (produto 1)
        add(labelProduct2);   // Terceira label (produto 2)
        add(labelProduct3);   // Quarta label (produto 3)

        // Executar a requisição em segundo plano
        new LoadInvoicesWorker().execute();
    }

    public class LoadInvoicesWorker extends SwingWorker<Void, Void> {

        @Override
        protected Void doInBackground() throws Exception {
            if (!isApiAvailable()) {
                return null; // API não ficou pronta
            }

            try {
                // Requisição para obter o DashboardDTO
                RestTemplate restTemplate = new RestTemplate();
                DashboardDTO dashboardDTO = restTemplate.exchange(
                        API_URL,
                        HttpMethod.GET,
                        null,
                        DashboardDTO.class
                ).getBody();

                SwingUtilities.invokeLater(() -> {
                    // Exibe o total de faturas
                    label.setText("Total de vendas no mês atual: " + dashboardDTO.getCountSales());

                    // Obtem os 3 primeiros produtos
                    List<Map<String, Object>> topProducts = dashboardDTO.getTopProducts();

                    // Atualiza cada label de produto com os valores correspondentes
                    if (topProducts.size() > 0) {
                        String productName = (String) topProducts.get(0).get("name");
                        int quantity = (int) topProducts.get(0).get("quantity");
                        labelProduct1.setText("Produto 1: " + productName + ", Quantidade: " + quantity + " vendas");
                    }

                    if (topProducts.size() > 1) {
                        String productName = (String) topProducts.get(1).get("name");
                        int quantity = (int) topProducts.get(1).get("quantity");
                        labelProduct2.setText("Produto 2: " + productName + ", Quantidade: " + quantity + " vendas");
                    }

                    if (topProducts.size() > 2) {
                        String productName = (String) topProducts.get(2).get("name");
                        int quantity = (int) topProducts.get(2).get("quantity");
                        labelProduct3.setText("Produto 3: " + productName + ", Quantidade: " + quantity + " vendas");
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
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