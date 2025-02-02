package projectpoo.construction_material_store.screens;




import projectpoo.construction_material_store.dto.ProductDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DashboardItensModal extends JDialog {

    public DashboardItensModal() {
        setTitle("Produtos com estoque baixo do mínimo");
        setSize(650, 400);
        setLocationRelativeTo(null);
    }

    public void showItensModal(List<ProductDTO> lowStockProducts) {
        // Definição das colunas da tabela
        String[] columnNames = {"ID", "Nome", "Estoque Atual", "Estoque Mínimo"};

        // Criando o modelo da tabela
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        // Adicionando os produtos ao modelo da tabela
        for (ProductDTO product : lowStockProducts) {
            model.addRow(new Object[]{
                    product.getId(),
                    product.getName(),
                    product.getTotalStock(),
                    product.getMinStock()
            });
        }

        // Criando a tabela com o modelo
        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);

        // Adicionando a tabela a um painel com barra de rolagem
        JScrollPane scrollPane = new JScrollPane(table);

        // Adicionando o painel ao diálogo
        getContentPane().removeAll(); // Remove qualquer conteúdo anterior
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Exibindo o diálogo
        setVisible(true);
    }
}
