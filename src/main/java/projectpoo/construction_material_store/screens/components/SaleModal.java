package projectpoo.construction_material_store.screens.components;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import projectpoo.construction_material_store.domain.Client;
import projectpoo.construction_material_store.domain.Invoice;
import projectpoo.construction_material_store.domain.InvoiceItem;
import projectpoo.construction_material_store.dto.ClientDTO;
import projectpoo.construction_material_store.dto.InvoiceDTO;
import projectpoo.construction_material_store.dto.InvoiceItemDTO;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SaleModal extends JDialog {

    private static final String API_URL = "http://localhost:8080/invoices"; // URL da sua API

    public SaleModal() {
    }

    //TODO:Lucas Cavalcante - Fazer a tela de criar venda.
    public void saleActionModal(TableComponent tableComponent, InvoiceDTO invoiceDTO) {
        // Determina se é criação ou edição
        boolean isEdit = invoiceDTO != null;
        String dialogTitle = isEdit ? "Editar venda" : "Criar venda";

        JDialog dialog = new JDialog(this, dialogTitle, true);
        dialog.setSize(650, 400);
        dialog.setLocationRelativeTo(this);

        // Painel para os campos
        JPanel panel = new JPanel(new GridLayout(11, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Campos de entrada
        panel.add(new JLabel("CPF/CNPJ:"));
        JTextField clientField = new JTextField(isEdit ? invoiceDTO.getClient().getCpfCnpj() : "");
        // Associa o DTO ao JTextField
        clientField.putClientProperty("clientDTO", isEdit ? invoiceDTO.getClient() : null);
        panel.add(clientField);

        panel.add(new JLabel("TOTAL"));
        JTextField totalPriceField = new JTextField(String.valueOf(isEdit ? invoiceDTO.getTotalPrice() : ""));
        panel.add(totalPriceField);

        panel.add(new JLabel("DATA DA VENDA"));
        JTextField saleDateField = new JTextField(isEdit ? invoiceDTO.getSaleDate() : "");
        panel.add(saleDateField);

        panel.add(new JLabel("STATUS:"));
        JTextField statusField = new JTextField(isEdit ? invoiceDTO.getStatus() : "");
        panel.add(statusField);

        panel.add(new JLabel("ITENS:"));
//        // Obter as categorias únicas dos itens do InvoiceDTO
//        Set<String> categories = isEdit ? invoiceDTO.getInvoiceItens().stream()
//                .map(invoiceItem -> invoiceItem.getProduct().getName()) // Ajuste conforme o modelo do DTO
//                .collect(Collectors.toSet()) : new HashSet<>();
//
//        // Converter o conjunto de categorias para um array e criar o JList
//        JList<String> categoryList = new JList<>(categories.toArray(new String[0]));
//        categoryList.putClientProperty("invoiceItensDTO", isEdit ? invoiceDTO.getInvoiceItens() : null);
//        // Definir a altura e largura preferidas
//        categoryList.setVisibleRowCount(categories.size());
//        categoryList.setFixedCellWidth(200); // Ajuste a largura conforme necessário
//        // Adicionar o JList ao painel
//        panel.add(new JScrollPane(categoryList));

        // Painel para lista de itens
        JPanel itemListPanel = new JPanel();
        itemListPanel.setLayout(new BoxLayout(itemListPanel, BoxLayout.Y_AXIS));
        itemListPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        JScrollPane scrollPane = new JScrollPane(itemListPanel);
        scrollPane.setPreferredSize(new Dimension(200, 150));
        panel.add(scrollPane);

        // Painel para botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10)); // Alinhamento à direita

        SaleItemModal saleItemModal = new SaleItemModal();
        // Botão Adicionar Itens
        JButton btnAddItem = new JButton("Adicionar Itens");
        btnAddItem.addActionListener(e -> saleItemModal.saleItemActionModal());
        buttonPanel.add(btnAddItem);

        // Botão de salvar
        JButton btnSave = new JButton(isEdit ? "Atualizar" : "Salvar");
        btnSave.addActionListener(e -> {
            ClientDTO clientDTO = (ClientDTO) clientField.getClientProperty("clientDTO");
            String totalPriceStr = totalPriceField.getText();
            String saleDate = saleDateField.getText();
            String status = statusField.getText();
            List<InvoiceItemDTO> itens = new ArrayList<>(); ;

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

//        panel.add(new JLabel()); // Espaço vazio para alinhar o botão
//        panel.add(btnSave);
//
//        dialog.add(panel);
//        dialog.setVisible(true);

        // Adiciona o painel de botões ao diálogo
        dialog.add(panel, BorderLayout.CENTER); // Campos no centro
        dialog.add(buttonPanel, BorderLayout.SOUTH); // Botões no rodapé

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
