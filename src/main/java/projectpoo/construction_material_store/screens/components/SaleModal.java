package projectpoo.construction_material_store.screens.components;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import projectpoo.construction_material_store.domain.Invoice;
import projectpoo.construction_material_store.dto.ClientDTO;
import projectpoo.construction_material_store.dto.InvoiceDTO;
import projectpoo.construction_material_store.dto.InvoiceItemDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class SaleModal extends JDialog {

    private static final String API_URL = "http://localhost:8080/invoices"; // URL da sua API
    private ClientDTO selectedClient;

    public SaleModal() {
    }

    public void saleActionModal(TableComponent tableComponent, InvoiceDTO invoiceDTO) {
        // Lista para armazenar os itens selecionados
        List<InvoiceItemDTO> itens = new ArrayList<>();

        // Determina se é criação ou edição
        boolean isEdit = invoiceDTO != null;
        String dialogTitle = isEdit ? "Editar venda" : "Criar venda";

        JDialog dialog = new JDialog(this, dialogTitle, true);
        dialog.setSize(650, 600);
        dialog.setLocationRelativeTo(this);

        // Painel principal com BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Painel para os campos alinhados horizontalmente
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new GridLayout(3, 2, 10, 10)); // 3 linhas, 2 colunas, espaçamento de 10px

        // Campo CPF/CNPJ
        JLabel clientLabel = new JLabel("CPF/CNPJ:");
        JTextField clientField = new JTextField(isEdit ? invoiceDTO.getClient().getCpfCnpj() : "");
        clientField.setEditable(false);
        fieldsPanel.add(clientLabel);
        fieldsPanel.add(clientField);

        // Campo Total
        JLabel totalLabel = new JLabel("TOTAL:");
        JTextField totalPriceField = new JTextField(String.valueOf(0.0));
        totalPriceField.setEditable(false);
        fieldsPanel.add(totalLabel);
        fieldsPanel.add(totalPriceField);

        // Campo STATUS
        JLabel statusLabel = new JLabel("STATUS:");
        JComboBox<String> statusList = new JComboBox<>(new String[]{"FINALIZADO", "CANCELADO", "ESTORNADO"});
        fieldsPanel.add(statusLabel);
        fieldsPanel.add(statusList);

        mainPanel.add(fieldsPanel, BorderLayout.NORTH);

        // Painel para a tabela
        JPanel tablePanel = new JPanel(new BorderLayout());
        JLabel tableLabel = new JLabel("Itens:");
        tablePanel.add(tableLabel, BorderLayout.NORTH);

        // Tabela de itens
        String[] colunas = {"Produto", "Quantidade", "Preço", "Total"};
        DefaultTableModel tableModel = new DefaultTableModel(colunas, 0);
        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(600, 200));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // Painel para os botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        SaleItemModal saleItemModal = new SaleItemModal();
        ClientItemModal clientItemModal = new ClientItemModal();

        // Botão para adicionar cliente
        JButton btnAddClient = new JButton("Adicionar Cliente");
        btnAddClient.addActionListener(e -> {
            clientItemModal.clientItemActionModal();
            ClientDTO selectClient = clientItemModal.getSelectedItem();
            if (selectClient != null) {
                clientField.putClientProperty("clientDTO", selectClient);
                clientField.setText(selectClient.getCpfCnpj());
            }
        });
        buttonPanel.add(btnAddClient);

        // Botão para adicionar itens
        JButton btnAddItem = new JButton("Adicionar Itens");
        btnAddItem.addActionListener(e -> {
            saleItemModal.saleItemActionModal();
            InvoiceItemDTO selectedItem = saleItemModal.getSelectedItem();
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
                                tableModel.setValueAt(selectedItem.getQuantitySale(), i, 1);
                                tableModel.setValueAt(selectedItem.getProduct().getPrice(), i, 2);
                                double itemTotal = selectedItem.getProduct().getPrice() * selectedItem.getQuantitySale();
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
                        selectedItem.getQuantitySale(),
                        selectedItem.getProduct().getPrice(),
                        String.format("R$ %.2f", selectedItem.getProduct().getPrice() * selectedItem.getQuantitySale())
                };
                tableModel.addRow(row);
            }

            total = itens.stream()
                    .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantitySale())
                    .sum();

            totalPriceField.setText(String.format("%.2f", total));

        });
        buttonPanel.add(btnAddItem);

        // Botão de salvar
        JButton btnSave = new JButton(isEdit ? "Atualizar" : "Salvar");
        btnSave.addActionListener(e -> {
            ClientDTO clientDTO = (ClientDTO) clientField.getClientProperty("clientDTO");
            double totalPrice = Double.parseDouble(totalPriceField.getText().replace(",", "."));
            String status = statusList.getSelectedItem().toString();

            try {
                if (isEdit) {
                    invoiceDTO.setStatus(status);
                    JOptionPane.showMessageDialog(dialog, "Venda atualizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    InvoiceDTO newInvoice = new InvoiceDTO(clientDTO, totalPrice, itens, status);
                    createInvoice(newInvoice);
                    JOptionPane.showMessageDialog(dialog, "Venda criada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                }
                tableComponent.loadData(API_URL, InvoiceDTO[].class);
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Erro ao salvar venda.", "Erro", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
        buttonPanel.add(btnSave);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

    private void createInvoice(InvoiceDTO newInvoice) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        // Envia a requisição POST
        HttpEntity<InvoiceDTO> request = new HttpEntity<>(newInvoice);
        ResponseEntity<Invoice> response = restTemplate.exchange(API_URL, HttpMethod.POST, request, Invoice.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new Exception("Erro ao criar venda: " + response.getStatusCode());
        }
    }
}
