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

public class SaleModal extends JDialog {

    private static final String API_URL = "http://localhost:8080/invoices"; // URL da sua API

    public SaleModal() {
    }

    public void saleActionModal(TableComponent tableComponent, InvoiceDTO invoiceDTO) {
        // Determina se é criação ou edição
        boolean isEdit = invoiceDTO != null;
        String dialogTitle = isEdit ? "Editar venda" : "Criar venda";

        JDialog dialog = new JDialog(this, dialogTitle, true);
        dialog.setSize(650, 600);
        dialog.setLocationRelativeTo(this);

        // Painel para os campos
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Usando BoxLayout para empilhar os componentes verticalmente
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Painel para o campo "CPF/CNPJ"
        JPanel clientPanel = new JPanel();
        clientPanel.setLayout(new BoxLayout(clientPanel, BoxLayout.Y_AXIS)); // Usando BoxLayout para empilhar a label e o campo
        clientPanel.add(new JLabel("CPF/CNPJ:"));
        JTextField clientField = new JTextField(isEdit ? invoiceDTO.getClient().getCpfCnpj() : "", 15);
        clientField.setMaximumSize(new Dimension(Integer.MAX_VALUE, clientField.getPreferredSize().height));
        clientPanel.add(clientField);
        panel.add(clientPanel); // Adiciona o painel ao painel principal

        // Painel para o campo "TOTAL"
        JPanel totalPanel = new JPanel();
        totalPanel.setLayout(new BoxLayout(totalPanel, BoxLayout.Y_AXIS));
        totalPanel.add(new JLabel("TOTAL:"));
        JTextField totalPriceField = new JTextField(String.valueOf(0.0), 15);
        totalPriceField.setMaximumSize(new Dimension(Integer.MAX_VALUE, totalPriceField.getPreferredSize().height));
        totalPanel.add(totalPriceField);
        panel.add(totalPanel); // Adiciona o painel ao painel principal

        // Painel para o campo "DATA DA VENDA"
        JPanel saleDatePanel = new JPanel();
        saleDatePanel.setLayout(new BoxLayout(saleDatePanel, BoxLayout.Y_AXIS));
        saleDatePanel.add(new JLabel("DATA DA VENDA:"));
        JTextField saleDateField = new JTextField(isEdit ? invoiceDTO.getSaleDate() : "", 15);
        saleDateField.setMaximumSize(new Dimension(Integer.MAX_VALUE, saleDateField.getPreferredSize().height));
        saleDatePanel.add(saleDateField);
        panel.add(saleDatePanel); // Adiciona o painel ao painel principal

        // Painel para o campo "STATUS"
        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));
        statusPanel.add(new JLabel("STATUS:"));
        JTextField statusField = new JTextField(isEdit ? invoiceDTO.getStatus() : "", 15);
        statusField.setMaximumSize(new Dimension(Integer.MAX_VALUE, statusField.getPreferredSize().height));
        statusPanel.add(statusField);
        panel.add(statusPanel); // Adiciona o painel ao painel principal

        // Painel principal
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Definindo o modelo de dados para a tabela
        String[] colunas = {"Produto", "Quantidade", "Preço", "Total"};
        DefaultTableModel tableModel = new DefaultTableModel(colunas, 0);  // Inicializa com zero itens
        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);

        // Adiciona a tabela a um JScrollPane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(600, 200)); // Ajuste a altura da tabela
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);  // Adiciona a barra de rolagem

        panel.add(new JLabel("ITENS:"));
        panel.add(scrollPane);

        // Lista para armazenar os itens selecionados
        List<InvoiceItemDTO> itens = new ArrayList<>();

        // Painel para os botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10)); // Alinhamento à direita

        SaleItemModal saleItemModal = new SaleItemModal();

        // Botão para adicionar itens
        JButton btnAddItem = new JButton("Adicionar Itens");
        btnAddItem.addActionListener(e -> {
            saleItemModal.saleItemActionModal();
            InvoiceItemDTO selectedItem = saleItemModal.getSelectedItem();
            double total = Double.parseDouble(totalPriceField.getText().replace(",", "."));

            if (selectedItem != null) {
                // Adiciona o item à lista
                itens.add(selectedItem);

                // Calcula o total
                double itemTotal = selectedItem.getProduct().getPrice() * selectedItem.getQuantitySale();
                total += itemTotal;

                // Adiciona o item à tabela
                Object[] row = new Object[]{
                        selectedItem.getProduct().getName(),
                        selectedItem.getQuantitySale(),
                        selectedItem.getProduct().getPrice(),
                        String.format("R$ %.2f", itemTotal)
                };
                tableModel.addRow(row);  // Atualiza a tabela com o novo item

                // Atualiza o campo TOTAL com o valor acumulado
                totalPriceField.setText(String.format("%.2f", total));
            }
        });
        buttonPanel.add(btnAddItem);

        // Botão de salvar
        JButton btnSave = new JButton(isEdit ? "Atualizar" : "Salvar");
        btnSave.addActionListener(e -> {
            ClientDTO clientDTO = (ClientDTO) clientField.getClientProperty("clientDTO");
            String totalPriceStr = totalPriceField.getText();
            String saleDate = saleDateField.getText();
            String status = statusField.getText();

            double totalPrice = Double.parseDouble(totalPriceStr);

            // Criar ou atualizar o cliente
            btnSave.setEnabled(false); // Desabilita o botão para evitar múltiplos cliques

            try {
                if (isEdit) {
                    invoiceDTO.setStatus(status);

//                    updateClient(clientDto);
                    JOptionPane.showMessageDialog(dialog, "Venda atualizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    InvoiceDTO newInvoice = new InvoiceDTO(clientDTO, totalPrice, saleDate, itens, status);
                    createInvoice(newInvoice);
                    JOptionPane.showMessageDialog(dialog, "Venda criada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                }

                tableComponent.loadData(API_URL, InvoiceDTO[].class); // Atualizar a lista de clientes
                dialog.dispose(); // Fechar o diálogo
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Erro ao salvar venda.", "Erro", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            } finally {
                btnSave.setEnabled(true); // Reativa o botão, mesmo em caso de erro
            }
        });
        buttonPanel.add(btnSave);

        // Adiciona o painel de botões ao diálogo
        dialog.add(panel, BorderLayout.CENTER); // Campos no centro
        dialog.add(buttonPanel, BorderLayout.SOUTH); // Botões no rodapé

        // Define o tamanho máximo do modal
        dialog.setMaximumSize(new Dimension(650, 600));  // Limita a altura máxima

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
