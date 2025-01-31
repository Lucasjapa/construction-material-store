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
    // Determine if it is creation or editing
    boolean isEdit = clientDto != null;
    String dialogTitle = isEdit ? "Editar Cliente" : "Criar Cliente";

    JDialog dialog = new JDialog(this, dialogTitle, true);
    dialog.setSize(650, 300);
    dialog.setLocationRelativeTo(this);

    // Main panel with BorderLayout
    JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
    mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    // Panel for the fields
    JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    Dimension fieldSize = new Dimension(200, 30); // Define preferred size for fields

    // Input fields
    panel.add(new JLabel("Nome:"));
    JTextField nameField = new JTextField(isEdit ? clientDto.getName() : "");
    nameField.setPreferredSize(fieldSize);
    panel.add(nameField);

    panel.add(new JLabel("CPF/CNPJ:"));
    JTextField cpfCnpjField = new JTextField(isEdit ? clientDto.getCpfCnpj() : "");
    cpfCnpjField.setPreferredSize(fieldSize);
    panel.add(cpfCnpjField);

    panel.add(new JLabel("Endereço: (Rua, Número, Bairro, CEP):"));
    JTextField addressField = new JTextField(isEdit ? clientDto.getAddress() : "");
    addressField.setPreferredSize(fieldSize);
    panel.add(addressField);

    panel.add(new JLabel("Telefone:"));
    JTextField phoneNumberField = new JTextField(isEdit ? clientDto.getPhoneNumber() : "");
    phoneNumberField.setPreferredSize(fieldSize);
    panel.add(phoneNumberField);

    panel.add(new JLabel("Email:"));
    JTextField emailField = new JTextField(isEdit ? String.valueOf(clientDto.getEmail()) : "");
    emailField.setPreferredSize(fieldSize);
    panel.add(emailField);

    // Panel for the save button
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton btnSave = new JButton(isEdit ? "Atualizar" : "Salvar");
    btnSave.addActionListener(e -> {
        String name = nameField.getText();
        String cpfCnpj = cpfCnpjField.getText();
        String address = addressField.getText();
        String phoneNumber = phoneNumberField.getText();
        String email = emailField.getText();

        // Define the client type automatically based on isValidCpfCnpj
        String clientType;
        if (isValidCpfCnpj(cpfCnpj)) {
            clientType = (cpfCnpj.length() == 11) ? "PESSOA_FISICA" : "PESSOA_JURIDICA";
        } else {
            JOptionPane.showMessageDialog(dialog, "CPF/CNPJ inválido. Insira 11 dígitos para CPF ou 14 dígitos para CNPJ.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Field validation
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

        // Create or update client
        btnSave.setEnabled(false); // Disable the button to avoid multiple clicks

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

            tableComponent.loadData(API_URL, ClientDTO[].class); // Update client list
            dialog.dispose(); // Close the dialog
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dialog, "Erro ao salvar cliente.", "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            btnSave.setEnabled(true); // Re-enable the button, even in case of error
        }
    });

    buttonPanel.add(btnSave);
    mainPanel.add(panel, BorderLayout.CENTER);
    mainPanel.add(buttonPanel, BorderLayout.SOUTH);

    dialog.add(mainPanel);
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
