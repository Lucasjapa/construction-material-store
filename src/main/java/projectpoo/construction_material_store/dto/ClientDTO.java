package projectpoo.construction_material_store.dto;

import projectpoo.construction_material_store.domain.Client;

public class ClientDTO {

    private long id;
    private String name;
    private String cpfCnpj;
    private String address;
    private String phoneNumber;
    private String email;
    private String clientType;  // Usando String ao invés de enum para facilitar a conversão em APIs

    // Construtores
    public ClientDTO() {}

    public ClientDTO(String name, String cpfCnpj, String address, String phoneNumber, String email, String clientType) {
        this.name = name;
        this.cpfCnpj = cpfCnpj;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.clientType = clientType;
    }

    public ClientDTO(Client client) {

        this.id = client.getId();
        this.name = client.getName();
        this.cpfCnpj = client.getCpfCnpj();
        this.address = client.getAddress();
        this.phoneNumber = client.getPhoneNumber();
        this.email = client.getEmail();
        this.clientType = client.getClientType() != null ? String.valueOf(client.getClientType()) : null;
    }

    public long getId() {
        return id;
    }

    // Getters e Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    // Método para converter de clientDTO para client
    public Client toclient() {
        Client.Type categoryEnum = Client.Type.valueOf(clientType);

        return new Client(
                this.cpfCnpj,
                this.name,
                this.address,
                this.phoneNumber,
                this.email,
                categoryEnum
        );
    }
}
