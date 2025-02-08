package projectpoo.construction_material_store.screens.components;

import projectpoo.construction_material_store.dto.ClientDTO;
import projectpoo.construction_material_store.dto.InvoiceDTO;
import projectpoo.construction_material_store.dto.ProductDTO;
import projectpoo.construction_material_store.dto.PurchaseDTO;

import javax.swing.*;
import java.awt.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class SearchBar {

    public <T> JPanel getSearchPanel(TableComponent tableComponent, String API_URL, String API_URL_SEARCH, Class<T[]> clazz) {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Layout alinhado à esquerda

        if (ProductDTO[].class.isAssignableFrom(clazz)) {
            searchPanel.add(new JLabel("Informe o nome do produto:")); // Adiciona o painel de título ao painel principal
        } else if (ClientDTO[].class.isAssignableFrom(clazz)) {
            searchPanel.add(new JLabel("Informe o CPF/CNPJ:")); // Adiciona o painel de título ao painel principal
        } else if (InvoiceDTO[].class.isAssignableFrom(clazz)) {
            searchPanel.add(new JLabel("Informe o cod. da nota fiscal:"));
        } else if (PurchaseDTO[].class.isAssignableFrom(clazz)) {
            searchPanel.add(new JLabel("Informe a data da compra(dd-mm-aaaa):"));
        }

        // Campo de busca
        JTextField searchField = new JTextField(20); // Tamanho do campo
        searchPanel.add(searchField);

        // Botão de busca
        JButton searchButton = new JButton("Buscar");
        searchPanel.add(searchButton);

        // Botão para listar todos os produtos
        JButton listButton = new JButton("Listar todos");
        searchPanel.add(listButton);

        // Ação do botão de busca
        searchButton.addActionListener(e -> {
            String searchParam = searchField.getText().trim();
            if (!searchParam.isEmpty()) {
                try {
                    String searchUrl = API_URL_SEARCH + URLEncoder.encode(searchParam, StandardCharsets.UTF_8);
                    // Carrega os dados da API usando o TableComponent
                    tableComponent.loadData(searchUrl, clazz);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(searchPanel, "Erro ao buscar clientes.", "Erro", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(searchPanel, "Digite um termo para buscar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        listButton.addActionListener(e -> tableComponent.loadData(API_URL, clazz));

        return searchPanel;
    }
}
