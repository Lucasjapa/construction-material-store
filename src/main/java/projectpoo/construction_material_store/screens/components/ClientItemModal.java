package projectpoo.construction_material_store.screens.components;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import projectpoo.construction_material_store.dto.ClientDTO;
import projectpoo.construction_material_store.dto.InvoiceItemDTO;
import projectpoo.construction_material_store.dto.ProductDTO;

import javax.swing.*;
import java.awt.*;

public class ClientItemModal extends JDialog {

    private static final String API_URL = "http://localhost:8080/clients";
    private ClientDTO selectedClient;

    public ClientItemModal() {
    }

    public void clientItemActionModal() {
        // Determina se é criação ou edição
        String dialogTitle = "Selecione o cliente";

        JDialog dialog = new JDialog(this, dialogTitle, true);
        dialog.setLayout(new BorderLayout()); // Configura o layout do diálogo

        // Painel para os campos
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Usando BoxLayout para empilhar os componentes verticalmente
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Adiciona o título "Produtos"
        JPanel clientsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        clientsPanel.add(new JLabel("Clientes:"));
        panel.add(clientsPanel); // Adiciona o painel de título ao painel principal

        // Painel da tabela
        JPanel tablePanel = new JPanel(new BorderLayout());

        // Instancia o componente de tabela
        TableComponent tableComponent = new TableComponent(new JPanel());

        try {
            // Carrega os dados antes de criar a tabela
            tableComponent.loadData(API_URL, ClientDTO[].class);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(dialog, "Erro ao carregar os clientes!", "Erro", JOptionPane.ERROR_MESSAGE);
        }

        // Cria o JScrollPane para a tabela e adiciona ao painel
        JScrollPane tableScrollPane = new JScrollPane(tableComponent.getClientTable());
        tablePanel.add(tableScrollPane, BorderLayout.CENTER); // Tabela ocupa o centro
        panel.add(tablePanel); // Adiciona o painel da tabela ao painel principal

        // Painel para o botão "Adicionar"
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Alinha o botão à direita
        JButton btnSave = new JButton("Selecionar cliente");
        btnSave.addActionListener(e -> {
            btnSave.setEnabled(false);

            int selectedRow = tableComponent.getClientTable().getSelectedRow();

            if (selectedRow != -1) { // Verifica se uma linha foi selecionada
                // Recupera o ID do produto selecionado (coluna "ID" - índice 7)
                Long productId = (Long) tableComponent.getClientTable().getValueAt(selectedRow, 3);

                // Aqui você pode fazer a consulta ao banco de dados usando o ID
                ClientDTO selecClient = addSelectedClient(productId);

                if (selecClient != null) {
                    // Aqui você pode usar os dados: selectedProduct (cliente)
                    System.out.println("Cliente selecionado: " + selecClient);

                    selectedClient = selecClient;

                    // Fechar o diálogo e passar os dados
                    dialog.dispose();

                } else {
                    JOptionPane.showMessageDialog(dialog, "Cliente não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
                }

            } else {
                JOptionPane.showMessageDialog(dialog, "Por favor, selecione um cliente.", "Erro", JOptionPane.ERROR_MESSAGE);
                btnSave.setEnabled(true); // Reativa o botão se não houver seleção
            }

            btnSave.setEnabled(true);
        });

        buttonPanel.add(btnSave); // Adiciona o botão ao painel

        // Adiciona o painel de botão ao painel principal
        panel.add(buttonPanel);

        // Adiciona o painel principal ao diálogo
        dialog.add(panel, BorderLayout.CENTER);

        // Ajusta o tamanho do diálogo com base no conteúdo
        dialog.pack();

        // Centraliza o diálogo
        dialog.setLocationRelativeTo(this);

        // Torna o diálogo visível
        dialog.setVisible(true);
    }

    private ClientDTO addSelectedClient(Long clientId) {
        // Criar uma instância de RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // URL da API para buscar o produto pelo ID
        String getUrl = API_URL + "/" + clientId;

        // Fazendo a requisição GET para buscar o produto
        ResponseEntity<ClientDTO> response = restTemplate.exchange(getUrl, HttpMethod.GET, null, ClientDTO.class);

        // Retorna o produto encontrado ou null caso não tenha encontrado
        return response.getBody();
    }

    public ClientDTO getSelectedItem() {
        return selectedClient;
    }
}
