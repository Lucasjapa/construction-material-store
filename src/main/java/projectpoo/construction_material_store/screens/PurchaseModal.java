package projectpoo.construction_material_store.screens;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import projectpoo.construction_material_store.domain.Purchase;
import projectpoo.construction_material_store.dto.PurchaseDTO;
import projectpoo.construction_material_store.dto.PurchaseItemDTO;
import projectpoo.construction_material_store.screens.components.TableComponent;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PurchaseModal extends JDialog {

    private static final String API_URL = "http://localhost:8080/purchases"; // URL da sua API

    public PurchaseModal() {
    }

    public void purchaseActionModal(TableComponent tableComponent, PurchaseDTO purchaseDTO, DashboardScreen dashboardScreen) {
        // Lista para armazenar os itens selecionados
        List<PurchaseItemDTO> itens = new ArrayList<>();

        // Determina se é criação ou edição
        boolean isEdit = purchaseDTO != null;
        String dialogTitle = "Criar venda";

        JDialog dialog = new JDialog(this, dialogTitle, true);
        dialog.setSize(750, 600);
        dialog.setLocationRelativeTo(this);

        // Painel principal com BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Painel para os campos alinhados horizontalmente
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new GridLayout(3, 2, 10, 10)); // 3 linhas, 2 colunas, espaçamento de 10px

        // Campo Total
        JLabel totalLabel = new JLabel("TOTAL:");
        JTextField totalPriceField = new JTextField(String.valueOf(0.0));
        totalPriceField.setEditable(false);
        fieldsPanel.add(totalLabel);
        fieldsPanel.add(totalPriceField);

        mainPanel.add(fieldsPanel, BorderLayout.NORTH);

        // Painel para a tabela
        JPanel tablePanel = new JPanel(new BorderLayout());
        JLabel tableLabel = new JLabel("Produtos:");
        tablePanel.add(tableLabel, BorderLayout.NORTH);

        // Tabela de itens
        String[] colunas = {"Produto", "Quantidade", "Preço de compra", "Total"};
        DefaultTableModel tableModel = new DefaultTableModel(colunas, 0);
        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(600, 200));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // Painel para os botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        PurchaseItemModal purchaseItemModal = new PurchaseItemModal();

        if (isEdit) {
            // Carregar itens existentes na tabela
            itens.addAll(purchaseDTO.getPurchaseItens());
            for (PurchaseItemDTO item : itens) {
                Object[] row = new Object[]{
                        item.getProduct().getName(),
                        item.getQuantityPurchase(),
                        item.getUnitPrice(),
                        String.format("R$ %.2f", item.getUnitPrice() * item.getQuantityPurchase())
                };
                tableModel.addRow(row);
            }
            double total = itens.stream()
                    .mapToDouble(item -> item.getUnitPrice() * item.getQuantityPurchase())
                    .sum();
            totalPriceField.setText(String.format("%.2f", total));
        } else {
            // Botão para adicionar itens
            JButton btnAddItem = new JButton("Adicionar Produtos");
            btnAddItem.addActionListener(e -> {
                purchaseItemModal.purchaseItemActionModal();
                PurchaseItemDTO selectedItem = purchaseItemModal.getSelectedItem();
                double total;

                boolean exist = itens.stream()
                        .filter(item -> item.getProduct().getId().equals(selectedItem.getProduct().getId()))
                        .findFirst()
                        .map(existingItem -> {
                            int index = itens.indexOf(existingItem);
                            itens.set(index, selectedItem);
                            // Atualiza a linha correspondente na tabela
                            for (int i = 0; i < tableModel.getRowCount(); i++) {
                                if (tableModel.getValueAt(i, 0).equals(existingItem.getProduct().getName())) {
                                    tableModel.setValueAt(selectedItem.getQuantityPurchase(), i, 1);
                                    tableModel.setValueAt(selectedItem.getUnitPrice(), i, 2);
                                    double itemTotal = selectedItem.getUnitPrice() * selectedItem.getQuantityPurchase();
                                    tableModel.setValueAt(String.format("R$ %.2f", itemTotal), i, 3);
                                    break;
                                }
                            }
                            return true;
                        }).orElse(false);

                if (!exist) {
                    itens.add(selectedItem);
                    Object[] row = new Object[]{
                            selectedItem.getProduct().getName(),
                            selectedItem.getQuantityPurchase(),
                            selectedItem.getUnitPrice(),
                            String.format("R$ %.2f", selectedItem.getUnitPrice() * selectedItem.getQuantityPurchase())
                    };
                    tableModel.addRow(row);
                }

                total = itens.stream()
                        .mapToDouble(item -> item.getUnitPrice() * item.getQuantityPurchase())
                        .sum();

                totalPriceField.setText(String.format("%.2f", total));

            });
            buttonPanel.add(btnAddItem);
        }

        // Botão de salvar
        JButton btnSave = new JButton("Salvar");
        btnSave.addActionListener(e -> {

            double totalPrice = Double.parseDouble(totalPriceField.getText().replace(",", "."));

            try {
                PurchaseDTO newPurchase = new PurchaseDTO(totalPrice, itens);
                createPurchase(newPurchase);
                JOptionPane.showMessageDialog(dialog, "Compra feita com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                tableComponent.loadData(API_URL, PurchaseDTO[].class);
                // Atualizar o total de faturas na Dashboard
                dashboardScreen.updateDashboard();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Erro ao salvar compra.", "Erro", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
        buttonPanel.add(btnSave);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

    private void createPurchase(PurchaseDTO newPurchase) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        // Envia a requisição POST
        HttpEntity<PurchaseDTO> request = new HttpEntity<>(newPurchase);
        ResponseEntity<Purchase> response = restTemplate.exchange(API_URL, HttpMethod.POST, request, Purchase.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new Exception("Erro ao fazer compra: " + response.getStatusCode());
        }
    }

}
