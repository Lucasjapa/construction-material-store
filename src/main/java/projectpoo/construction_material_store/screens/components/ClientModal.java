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
        JPanel panel = new JPanel(new GridLayout(11, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Campos de entrada
        panel.add(new JLabel("Nome:"));
        JTextField nameField = new JTextField(isEdit ? clientDto.getName() : "");
        panel.add(nameField);

        panel.add(new JLabel("CPF/CNPJ"));
        JTextField cpfCnpjField = new JTextField(isEdit ? clientDto.getCpfCnpj() : "");
        panel.add(cpfCnpjField);

        panel.add(new JLabel("Endereço: (CEP, Rua, Número, Bairro"));
        JTextField addressField = new JTextField(isEdit ? clientDto.getAddress() : "");
        panel.add(addressField);

        panel.add(new JLabel("Telefone:"));
        JTextField phoneNumberField = new JTextField(isEdit ? clientDto.getPhoneNumber() : "");
        panel.add(phoneNumberField);

        panel.add(new JLabel("Email:"));
        JTextField emailField = new JTextField(isEdit ? String.valueOf(clientDto.getEmail()) : "");
        panel.add(emailField);

        // Botão de salvar
        JButton btnSave = new JButton(isEdit ? "Atualizar" : "Salvar");
        btnSave.addActionListener(e -> {
            String name = nameField.getText();
            String cpfCnpj = cpfCnpjField.getText();
            String address = addressField.getText();
            String phoneNumber = phoneNumberField.getText();
            String email = emailField.getText();

            // Definir o tipo de cliente automaticamente com base no isValidCpfCnpj
            String clientType;
            if (isValidCpfCnpj(cpfCnpj)) {
                clientType = (cpfCnpj.length() == 11) ? "PESSOA_FISICA" : "PESSOA_JURIDICA";
            } else {
                JOptionPane.showMessageDialog(dialog, "CPF/CNPJ inválido. Insira 11 dígitos para CPF ou 14 dígitos para CNPJ.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validação dos campos
            if (name.isEmpty() || cpfCnpj.isEmpty() || address.isEmpty() || phoneNumber.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Todos os campos são obrigatórios.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!isValidName(name)) {
                JOptionPane.showMessageDialog(dialog, "Nome inválido (Somente letras são permitidas).", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!isValidPhoneNumber(phoneNumber)) {
                JOptionPane.showMessageDialog(dialog, "Número de telefone inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!isValidEmail(email)) {
                JOptionPane.showMessageDialog(dialog, "Email inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Criar ou atualizar o cliente
            btnSave.setEnabled(false); // Desabilita o botão para evitar múltiplos cliques

            try {
                if (isEdit) {
                    clientDto.setName(name);
                    clientDto.setCpfCnpj(cpfCnpj);
                    clientDto.setAddress(address);
                    clientDto.setPhoneNumber(phoneNumber);
                    clientDto.setEmail(email);
                    clientDto.setClientType(clientType);

                    updateClient(clientDto);
                    JOptionPane.showMessageDialog(dialog, "Cliente atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    ClientDTO newClient = new ClientDTO(name, cpfCnpj, address, phoneNumber, email, clientType);

                    createClient(newClient);
                    JOptionPane.showMessageDialog(dialog, "Cliente criado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                }

                tableComponent.loadData(API_URL, ClientDTO[].class); // Atualizar a lista de clientes
                dialog.dispose(); // Fechar o diálogo
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Erro ao salvar cliente.", "Erro", JOptionPane.ERROR_MESSAGE);
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


    // Metodo para validar nome
    private boolean isValidName(String name){
        return name.matches("^[a-zA-ZÀ-ÿ\\s]+$");
    }

    // Metodo para validar CPF ou CNPJ
    private boolean isValidCpfCnpj(String cpfCnpj) {
        if (cpfCnpj.matches("^\\d{11}$")) {
            return true;
        } else if (cpfCnpj.matches("^\\d{14}$")) {
            return true;
        } else {
            // Caso não seja nem CPF nem CNPJ
            return false;
        }
    }

    // Metodo para validar número de telefone
    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("(\\+\\d{1,3})?\\d{10,11}");
    }

    // Metodo para validar email
    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
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
