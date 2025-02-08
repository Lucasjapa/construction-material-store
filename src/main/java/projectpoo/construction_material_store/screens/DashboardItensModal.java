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
        setSize(850, 400);
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
            columnNames = new String[]{"Nome", "Código", "Estoque Total", "Estoque Mínimo", "Preço da unidade", "Unidade de venda"};
            model = new DefaultTableModel(columnNames, 0);
            for (T item : itens) {
                ProductDTO productDTO = (ProductDTO) item;
                model.addRow(new Object[]{
                        productDTO.getName(),
                        productDTO.getCodProduct(),
                        productDTO.getTotalStock(),
                        productDTO.getMinStock(),
                        String.format("R$ %.2f", productDTO.getPrice()),
                        productDTO.getSalesUnit(),
                });
            }
        } else if (clazz == InvoiceItemDTO.class) {
            // Para InvoiceDTO
            columnNames = new String[]{"Produto", "Quantidade vendida", "Preço da unidade", "Total"};
            model = new DefaultTableModel(columnNames, 0);
            for (T item : itens) {
                InvoiceItemDTO invoiceItemDTO = (InvoiceItemDTO) item;
                model.addRow(new Object[]{
                        invoiceItemDTO.getProduct().getName(),
                        invoiceItemDTO.getQuantitySale(),
                        String.format("R$ %.2f", invoiceItemDTO.getUnitPrice()),
                        String.format("R$ %.2f", invoiceItemDTO.getUnitPrice() * invoiceItemDTO.getQuantitySale())
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
                        String.format("R$ %.2f", purchaseItemDTO.getUnitPrice()),
                        String.format("R$ %.2f", purchaseItemDTO.getQuantityPurchase() * purchaseItemDTO.getUnitPrice()),
                });
            }
        }

        // Se o tipo não for nenhum dos conhecidos, você pode retornar
        if (model == null) {
            JOptionPane.showMessageDialog(this, "Tipo de item desconhecido!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTable table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(table);

        getContentPane().removeAll();
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        setModal(true);
        setVisible(true);
    }
}
