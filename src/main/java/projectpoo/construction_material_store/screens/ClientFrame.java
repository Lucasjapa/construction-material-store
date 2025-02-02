package projectpoo.construction_material_store.screens;

import org.springframework.web.client.RestTemplate;
import projectpoo.construction_material_store.dto.ClientDTO;
import projectpoo.construction_material_store.screens.components.BackButton;
import projectpoo.construction_material_store.screens.components.SearchBar;
import projectpoo.construction_material_store.screens.components.TableComponent;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ClientFrame extends JFrame {

    private static final String API_URL = "http://localhost:8080/clients"; // URL da sua API
    private final MainScreen mainScreen;

    public ClientFrame(MainScreen mainScreen, DashboardScreen dashboardScreen) {
        this.mainScreen = mainScreen;
        setTitle("Clientes");
        setSize(900, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Configura o layout da tela
        setLayout(new BorderLayout());

        // JLabel com o título
        JLabel label = new JLabel("Lista de Clientes");
        add(label, BorderLayout.NORTH);

        // Inicializa o painel e adiciona ao layout
        JPanel clientPanel = new JPanel();
        clientPanel.setLayout(new BoxLayout(clientPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(clientPanel);// Alinha verticalmente os componentes

        // Cria a instância do TableComponent
        TableComponent tableComponent = new TableComponent(clientPanel);
        SearchBar searchBar = new SearchBar();

        // Adiciona o painel de busca acima da tabela
        JPanel searchPanel = searchBar.getSearchPanel(tableComponent,API_URL, API_URL + "/search-clients/", ClientDTO[].class);
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(searchPanel, BorderLayout.NORTH); // Barra de busca acima
        centerPanel.add(scrollPane, BorderLayout.CENTER); // Tabela no centro
        add(centerPanel, BorderLayout.CENTER);

        // Adiciona o painel de botões ao layout no local apropriado (parte inferior)
        JPanel buttonPanel = getButtonPanel(tableComponent, dashboardScreen);
        add(buttonPanel, BorderLayout.SOUTH);
        
        try {
            tableComponent.loadData(API_URL, ClientDTO[].class);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private JPanel getButtonPanel(TableComponent tableComponent, DashboardScreen dashboardScreen) {
        ClientModal clientModal = new ClientModal();
        // Painel principal de botões
        JPanel buttonPanel = new JPanel(new BorderLayout());

        // Adiciona o botão "Voltar"
        BackButton btnBack = new BackButton(this, mainScreen, dashboardScreen);
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.add(btnBack);

        // Painel para os outros botões no lado direito
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnCreateProduct = new JButton("Criar Cliente");
        btnCreateProduct.addActionListener(e -> clientModal.clientActionModal(tableComponent, null));
        rightPanel.add(btnCreateProduct);

        JButton btnUpdateProduct = new JButton("Editar Cliente");
        btnUpdateProduct.addActionListener(e -> updateSelectedClient(tableComponent, clientModal));
        rightPanel.add(btnUpdateProduct);

        JButton btnDeleteSelected = new JButton("Deletar Selecionados");
        btnDeleteSelected.addActionListener(e -> deleteSelectedClients(tableComponent));
        rightPanel.add(btnDeleteSelected);

        // Adiciona os painéis ao painel principal
        buttonPanel.add(leftPanel, BorderLayout.WEST);  // Botão "Voltar" à esquerda
        buttonPanel.add(rightPanel, BorderLayout.EAST); // Outros botões à direita

        return buttonPanel;
    }

    private void updateSelectedClient(TableComponent tableComponent, ClientModal clientModal) {
        Long clientId = null;

        // Obtém o modelo da tabela
        DefaultTableModel tableModel = (DefaultTableModel) tableComponent.getClientTable().getModel();

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Boolean isSelected = (Boolean) tableModel.getValueAt(i, 0);
            if (isSelected) {
                clientId = (Long) tableModel.getValueAt(i, 5);
            }
        }

        // Verifica se a lista de produtos selecionados não está vazia
        if (clientId != null) {
            // Chame a API para deletar os produtos com os IDs armazenados
            ClientDTO clientDTO = getClientById(clientId);
            clientModal.clientActionModal(tableComponent, clientDTO);
        } else {
            JOptionPane.showMessageDialog(this, "Nenhum cliente selecionado para edição.", "Erro", JOptionPane.ERROR_MESSAGE);
        }

    }

    private ClientDTO getClientById(Long ClientId) {
        // Criar uma instância de RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Chama a API para obter o produto
        String getUrl = API_URL + "/" + ClientId;
        return restTemplate.getForObject(getUrl, ClientDTO.class);
    }

    private void deleteSelectedClients(TableComponent tableComponent) {
        // Lista dos IDs dos produtos a serem deletados
        List<Long> clientsToDelete = new ArrayList<>();

        // Obtém o modelo da tabela
        DefaultTableModel tableModel = (DefaultTableModel) tableComponent.getClientTable().getModel();

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Boolean isSelected = (Boolean) tableModel.getValueAt(i, 0);
            if (isSelected) {
                Long clientId = (Long) tableModel.getValueAt(i, 4); // Obtém o id do produto
                if (clientId != null) {
                    clientsToDelete.add(clientId); // Adiciona o id à lista
                }
            }
        }

        // Verifica se a lista de produtos selecionados não está vazia
        if (!clientsToDelete.isEmpty()) {
            // Chame a API para deletar os produtos com os IDs armazenados
            deleteClientsFromApi(clientsToDelete);
            tableComponent.loadData(API_URL, ClientDTO[].class);
        } else {
            JOptionPane.showMessageDialog(this, "Nenhum cliente selecionado para deletar.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para deletar os produtos da API
    private void deleteClientsFromApi(List<Long> clientsToDelete) {
        // Criar uma instância de RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Chama a API para deletar os produtos
        for (Long clientId : clientsToDelete) {
            String deleteUrl = API_URL + "/" + clientId;
            restTemplate.delete(deleteUrl);
        }

    }
}
