package projectpoo.construction_material_store.screens;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import projectpoo.construction_material_store.dto.DashboardDTO;
import projectpoo.construction_material_store.dto.ProductDTO;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DashboardScreen extends JPanel {

    private static final String API_URL_TOTAL_INVOICES = "http://localhost:8080/dashboard/total-current-month";
    private static final String API_URL = "http://localhost:8080/dashboard";
    private List<ProductDTO> lowStockProducts = new ArrayList<>();
    private JLabel labelTotalSales, titleTopProducts, titleLowProducts, titleStockEvaluation;
    private JLabel[] labelTopProducts = new JLabel[3];
    private JLabel[] labelLowProducts = new JLabel[3];
    private JButton btnViewLowStockProducts;

    public DashboardScreen() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(CENTER_ALIGNMENT);

        labelTotalSales = createLabel("Carregando dashboard...", 24, Font.BOLD);
        titleTopProducts = createLabel("3 Produtos Mais Vendidos", 18, Font.BOLD);
        titleLowProducts = createLabel("3 Produtos Menos Vendidos", 18, Font.BOLD);
        titleStockEvaluation = createLabel("Carregando avaliação de estoque", 24, Font.BOLD);

        for (int i = 0; i < 3; i++) {
            labelTopProducts[i] = createLabel("Produto " + (i + 1) + ": ", 18, Font.PLAIN);
            labelLowProducts[i] = createLabel("Produto " + (i + 1) + ": ", 18, Font.PLAIN);
        }

        btnViewLowStockProducts = new JButton("Ver Produtos");
        btnViewLowStockProducts.setAlignmentX(CENTER_ALIGNMENT);
        btnViewLowStockProducts.setVisible(false);
        btnViewLowStockProducts.addActionListener(e -> showLowStockProducts());

        addComponents();
        new LoadInvoicesWorker().execute();
    }

    private JLabel createLabel(String text, int size, int style) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Arial", style, size));
        label.setAlignmentX(CENTER_ALIGNMENT);
        return label;
    }

    private void addComponents() {
        add(Box.createVerticalStrut(10));
        add(labelTotalSales);
        add(Box.createVerticalStrut(10));
        add(Box.createVerticalStrut(10));
        add(createSeparator());

        add(Box.createVerticalStrut(10));
        add(titleTopProducts);
        for (JLabel label : labelTopProducts) add(label);
        add(Box.createVerticalStrut(10));
        add(createSeparator());

        add(Box.createVerticalStrut(10));
        add(titleLowProducts);
        for (JLabel label : labelLowProducts) add(label);
        add(Box.createVerticalStrut(10));
        add(createSeparator());

        add(Box.createVerticalStrut(10));
        add(titleStockEvaluation);
        add(btnViewLowStockProducts);
        add(Box.createVerticalStrut(10));
        add(createSeparator());
    }

    private class LoadInvoicesWorker extends SwingWorker<Void, Void> {
        @Override
        protected Void doInBackground() {
            if (!isApiAvailable()) return null;
            try {
                RestTemplate restTemplate = new RestTemplate();
                DashboardDTO dashboardDTO = restTemplate.exchange(API_URL, HttpMethod.GET, null, DashboardDTO.class).getBody();
                lowStockProducts = dashboardDTO.getLowStockProducts();
                updateDataDashboard(dashboardDTO);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void updateDataDashboard(DashboardDTO dashboardDTO) {
        SwingUtilities.invokeLater(() -> {
            labelTotalSales.setText("Total de vendas no mês atual: " + dashboardDTO.getCountSales());
            updateProductLabels(dashboardDTO.getTopMostProducts(), labelTopProducts);
            updateProductLabels(dashboardDTO.getTopLeastProducts(), labelLowProducts);

            if (!lowStockProducts.isEmpty()) {
                titleStockEvaluation.setText("Avaliação de estoque: Baixo estoque");
                btnViewLowStockProducts.setVisible(true);
            } else {
                titleStockEvaluation.setText("Avaliação de estoque: Estoque OK");
                btnViewLowStockProducts.setVisible(false);
            }

            // Garantir que a UI seja redesenhada
            revalidate();
            repaint();
        });
    }

    private void updateProductLabels(List<Map<String, Object>> products, JLabel[] labels) {
        for (int i = 0; i < labels.length; i++) {
            if (i < products.size()) {
                String name = (String) products.get(i).get("name");
                int quantity = (int) products.get(i).get("quantity");
                labels[i].setText("Produto " + (i + 1) + ": " + name + ", Quantidade: " + quantity + " vendas");
            } else {
                labels[i].setText("Produto " + (i + 1) + ": -");
            }
        }
    }

    private boolean isApiAvailable() {
        RestTemplate restTemplate = new RestTemplate();
        for (int i = 0; i < 10; i++) {
            try {
                ResponseEntity<String> response = restTemplate.getForEntity(API_URL_TOTAL_INVOICES, String.class);
                if (response.getStatusCode().is2xxSuccessful()) return true;
            } catch (Exception e) {
                System.out.println("API ainda não está pronta. Tentando novamente...");
                try { Thread.sleep(3000); } catch (InterruptedException ex) { Thread.currentThread().interrupt(); }
            }
        }
        return false;
    }

    public void updateDashboard() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            Long totalInvoices = restTemplate.getForObject(API_URL_TOTAL_INVOICES, Long.class);

            // Trata caso a API retorne null
            long total = (totalInvoices != null) ? totalInvoices : 0;

            SwingUtilities.invokeLater(() -> {
                labelTotalSales.setText("Total de faturas: " + total);
                loadDashboardData();
            });
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao atualizar total de faturas.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadDashboardData() {
        // Esta função irá fazer a requisição e atualizar os dados do dashboard
        new LoadInvoicesWorker().execute();
    }

    private JSeparator createSeparator() {
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        return separator;
    }

    private void showLowStockProducts() {
        new DashboardItensModal().showItensModal(lowStockProducts);
    }
}