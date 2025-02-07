package projectpoo.construction_material_store.screens;


import projectpoo.construction_material_store.dto.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.List;

public class DashboardItensModal extends JDialog {

    public DashboardItensModal() {
        setTitle("Produtos");
        setSize(650, 400);
        setLocationRelativeTo(null);
    }

    public <T> void showItensModal(List<T> itens) {
        if (itens == null || itens.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum item encontrado!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Verifica o tipo de item da lista e cria a tabela de acordo com isso
        Class<?> clazz = itens.get(0).getClass();
        DefaultTableModel model = null;
        String[] columnNames = null;

        if (clazz == ProductDTO.class) {
            // Para ProductDTO
            columnNames = new String[]{"Nome", "Código", "Estoque Total", "Estoque Mínimo", "Preço", "Unidade de venda"};
            model = new DefaultTableModel(columnNames, 0);
            for (T item : itens) {
                ProductDTO product = (ProductDTO) item;
                model.addRow(new Object[]{
                        product.getName(),
                        product.getCodProduct(),
                        product.getTotalStock(),
                        product.getMinStock(),
                        product.getPrice(),
                        product.getSalesUnit(),
                });
            }
        } else if (clazz == InvoiceDTO.class) {
            // Para InvoiceDTO
            columnNames = new String[]{"Selecionar", "Cod. Nota Fiscal", "CPF/CNPJ", "Data da venda", "Total", "Status", "ID"};
            model = new DefaultTableModel(columnNames, 0);
            for (T item : itens) {
                InvoiceDTO invoiceDTO = (InvoiceDTO) item;
                model.addRow(new Object[]{
                        false,
                        invoiceDTO.getCodInvoice(),
                        invoiceDTO.getClient().getCpfCnpj(),
                        invoiceDTO.getSaleDate(),
                        invoiceDTO.getTotalPrice(),
                        invoiceDTO.getStatus(),
                });
            }
        } else if (clazz == PurchaseItemDTO.class) {
            // Para PurchaseDTO
            columnNames = new String[]{"Produto", "Quantidade comprada", "Preço da unidade", "Total"};
            model = new DefaultTableModel(columnNames, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return column == 0 || column == 3; // Permite edição apenas na seleção e no botão
                }
            };
            for (T item : itens) {
                PurchaseItemDTO purchaseItemDTO = (PurchaseItemDTO) item;
                model.addRow(new Object[]{
                        purchaseItemDTO.getProduct().getName(),
                        purchaseItemDTO.getQuantityPurchase(),
                        purchaseItemDTO.getUnitPrice(),
                        purchaseItemDTO.getQuantityPurchase() * purchaseItemDTO.getUnitPrice(),
                });
            }
        }

        // Se o tipo não for nenhum dos conhecidos, você pode retornar
        if (model == null) {
            JOptionPane.showMessageDialog(this, "Tipo de item desconhecido!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Criando a tabela com o modelo dinâmico
        JTable table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setFillsViewportHeight(true);

        // Adicionando a tabela a um painel com barra de rolagem
        JScrollPane scrollPane = new JScrollPane(table);

        // Adicionando o painel ao diálogo
        getContentPane().removeAll();
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Configurando a janela
        setSize(600, 400);
        setLocationRelativeTo(null);
        setModal(true);
        setVisible(true);
    }
}
