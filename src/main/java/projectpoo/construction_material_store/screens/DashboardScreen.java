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
    private final JLabel label;     // Para exibir o total de faturas
    private final JLabel labelProduct1; // Para exibir o primeiro produto
    private final JLabel labelProduct2; // Para exibir o segundo produto
    private final JLabel labelProduct3; // Para exibir o terceiro produto
    private JLabel labelProduct4; // Para exibir o terceiro produto
    private JLabel labelProduct5; // Para exibir o terceiro produto
    private JLabel labelProduct6; // Para exibir o terceiro produto
    private JLabel titleLabel; // Título para a lista de produtos
    private JLabel title2Label; // Título para a lista de produtos

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

        // Título para os produtos
        title2Label = new JLabel("3 Produtos Menos Vendidos", SwingConstants.CENTER);
        title2Label.setFont(new Font("Arial", Font.BOLD, 18));
        title2Label.setAlignmentX(CENTER_ALIGNMENT); // Centraliza o título

        // JLabels para os produtos
        labelProduct4 = new JLabel("Produto 1: ", SwingConstants.CENTER);
        labelProduct4.setFont(new Font("Arial", Font.PLAIN, 18));
        labelProduct4.setAlignmentX(CENTER_ALIGNMENT); // Centraliza a label

        labelProduct5 = new JLabel("Produto 2: ", SwingConstants.CENTER);
        labelProduct5.setFont(new Font("Arial", Font.PLAIN, 18));
        labelProduct5.setAlignmentX(CENTER_ALIGNMENT); // Centraliza a label

        labelProduct6 = new JLabel("Produto 3: ", SwingConstants.CENTER);
        labelProduct6.setFont(new Font("Arial", Font.PLAIN, 18));
        labelProduct6.setAlignmentX(CENTER_ALIGNMENT); // Centraliza a label

        add(Box.createVerticalStrut(10));
        add(label);
        add(Box.createVerticalStrut(10));
        add(createSeparator());

        add(Box.createVerticalStrut(10));
        add(titleLabel);
        add(Box.createVerticalStrut(10));
        add(labelProduct1);
        add(labelProduct2);
        add(labelProduct3);
        add(Box.createVerticalStrut(10));
        add(createSeparator());

        add(Box.createVerticalStrut(10));
        add(title2Label);
        add(Box.createVerticalStrut(10));
        add(labelProduct4);
        add(labelProduct5);
        add(labelProduct6);
        add(Box.createVerticalStrut(10));
        add(createSeparator());

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
                    List<Map<String, Object>> topMostProducts = dashboardDTO.getTopMostProducts();
                    List<Map<String, Object>> topLeastProducts = dashboardDTO.getTopLeastProducts();

                    // Atualiza cada label de produto com os valores correspondentes
                    if (topMostProducts.size() > 0) {
                        String productName = (String) topMostProducts.get(0).get("name");
                        int quantity = (int) topMostProducts.get(0).get("quantity");
                        labelProduct1.setText("Produto 1: " + productName + ", Quantidade: " + quantity + " vendas");
                    }

                    if (topMostProducts.size() > 1) {
                        String productName = (String) topMostProducts.get(1).get("name");
                        int quantity = (int) topMostProducts.get(1).get("quantity");
                        labelProduct2.setText("Produto 2: " + productName + ", Quantidade: " + quantity + " vendas");
                    }

                    if (topMostProducts.size() > 2) {
                        String productName = (String) topMostProducts.get(2).get("name");
                        int quantity = (int) topMostProducts.get(2).get("quantity");
                        labelProduct3.setText("Produto 3: " + productName + ", Quantidade: " + quantity + " vendas");
                    }

                    // Atualiza cada label de produto com os valores correspondentes
                    if (topLeastProducts.size() > 0) {
                        String productName = (String) topLeastProducts.get(0).get("name");
                        int quantity = (int) topLeastProducts.get(0).get("quantity");
                        labelProduct4.setText("Produto 1: " + productName + ", Quantidade: " + quantity + " vendas");
                    }

                    if (topLeastProducts.size() > 1) {
                        String productName = (String) topLeastProducts.get(1).get("name");
                        int quantity = (int) topLeastProducts.get(1).get("quantity");
                        labelProduct5.setText("Produto 2: " + productName + ", Quantidade: " + quantity + " vendas");
                    }

                    if (topLeastProducts.size() > 2) {
                        String productName = (String) topLeastProducts.get(2).get("name");
                        int quantity = (int) topLeastProducts.get(2).get("quantity");
                        labelProduct6.setText("Produto 3: " + productName + ", Quantidade: " + quantity + " vendas");
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

    private JSeparator createSeparator() {
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2)); // Definir altura de 2px
        return separator;
    }
}