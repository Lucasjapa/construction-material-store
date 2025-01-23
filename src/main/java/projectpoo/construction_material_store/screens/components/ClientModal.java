package projectpoo.construction_material_store.screens.components;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import projectpoo.construction_material_store.domain.Client;
import projectpoo.construction_material_store.dto.ClientDTO;

import javax.swing.*;
import java.awt.*;

public class ClientModal extends JDialog {

    private static final String API_URL = "http://localhost:8080/clients"; // URL da sua API

    public ClientModal() {
    }

    public void clientActionModal(TableComponent tableComponent, ClientDTO clientDto) {
        // Determina se é criação ou edição
        boolean isEdit = clientDto != null;
        String dialogTitle = isEdit ? "Editar Cliente" : "Criar Cliente";

        JDialog dialog = new JDialog(this, dialogTitle, true);
        dialog.setSize(650, 400);
        dialog.setLocationRelativeTo(this);

        // Painel para os campos
        JPanel panel = new JPanel(new GridLayout(13, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Campos de entrada
        panel.add(new JLabel("Nome:"));
        JTextField nameField = new JTextField(isEdit ? clientDto.getName() : "");
        panel.add(nameField);

        panel.add(new JLabel("CPF/CNPJ"));
        JTextField cpfCnpjField = new JTextField(isEdit ? clientDto.getCpfCnpj() : "");
        panel.add(cpfCnpjField);

        panel.add(new JLabel("Endereço:"));
        JTextField addressField = new JTextField(isEdit ? clientDto.getAddress() : "");
        panel.add(addressField);

        panel.add(new JLabel("Telefone:"));
        JTextField phoneNumberField = new JTextField(isEdit ? clientDto.getPhoneNumber() : "");
        panel.add(phoneNumberField);

        panel.add(new JLabel("Email:"));
        JTextField emailField = new JTextField(isEdit ? String.valueOf(clientDto.getEmail()) : "");
        panel.add(emailField);

        panel.add(new JLabel("Tipo pessoa:"));
        JComboBox<String> clientTypeOption = new JComboBox<>(new String[]{
                "PESSOA_FISICA", "PESSOA_JURIDICA"
        });
        // Define a categoria selecionada com base no valor do DTO
        clientTypeOption.setSelectedItem(isEdit ? clientDto.getClientType() : "");
        panel.add(clientTypeOption);

        // Botão de salvar
        JButton btnSave = new JButton(isEdit ? "Atualizar" : "Salvar");
        btnSave.addActionListener(e -> {
            String name = nameField.getText();
            String cpfCnpj = cpfCnpjField.getText();
            String address = addressField.getText();
            String phoneNumber = phoneNumberField.getText();
            String email = emailField.getText();
            String clientType = clientTypeOption.getSelectedItem().toString();

            // Validação dos campos
            if (name.isEmpty() || cpfCnpj.isEmpty() || address.isEmpty() ||
                    phoneNumber.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Todos os campos são obrigatórios.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Criar produto
            btnSave.setEnabled(false); // Desabilita o botão para evitar múltiplos cliques

            try {
                // Criação ou atualização
                if (isEdit) {
                    // Atualizar o produto existente
                    clientDto.setName(name);
                    clientDto.setCpfCnpj(cpfCnpj);
                    clientDto.setAddress(address);
                    clientDto.setPhoneNumber(phoneNumber);
                    clientDto.setEmail(email);
                    clientDto.setClientType(clientType);

                    updateClient(clientDto); // Chame o método de atualização
                    JOptionPane.showMessageDialog(dialog, "Cliente atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // Cria o objeto Product com os novos atributos
                    ClientDTO newClient = new ClientDTO(name, cpfCnpj, address, phoneNumber, email, clientType);

                    // Criar um cliente
                    createClient(newClient);
                    JOptionPane.showMessageDialog(dialog, "Cliente criado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                }

                tableComponent.loadData(API_URL, ClientDTO[].class); // Atualizar lista de produtos
                dialog.dispose(); // Fechar o diálogo
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Erro ao criar cliente", "Erro", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            } finally {
                btnSave.setEnabled(true); // Reativa o botão, mesmo em caso de erro
            }
        });

        panel.add(new JLabel()); // Espaço vazio para alinhar o botão
        panel.add(btnSave);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void createClient(ClientDTO newClient) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        // Envia a requisição POST
        HttpEntity<ClientDTO> request = new HttpEntity<>(newClient);
        ResponseEntity<Client> response = restTemplate.exchange(API_URL, HttpMethod.POST, request, Client.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new Exception("Erro ao criar produto: " + response.getStatusCode());
        }
    }

    private void updateClient(ClientDTO clientDTO) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        // Envia a requisição PUT com o identificador do produto
        String url = API_URL + "/updatecliente/" + clientDTO.getId();  // Supondo que o ID do produto seja parte da URL para atualização
        HttpEntity<ClientDTO> request = new HttpEntity<>(clientDTO);
        ResponseEntity<Client> response = restTemplate.exchange(url, HttpMethod.PUT, request, Client.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new Exception("Erro ao atualizar produto: " + response.getStatusCode());
        }
    }
}
