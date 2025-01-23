package projectpoo.construction_material_store.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "POO_Client")
public class Client {

    @Id
    @SequenceGenerator(
            name = "poo_client_id",
            sequenceName = "poo_client_id",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "poo_client_id")
    private Long id;

    @Column(nullable = false)
    String name;

    @Column(unique = true, nullable = false, length = 20)
    private String cpfCnpj;

    @Column(nullable = false)
    private String address;

    @Column(length = 15)
    private String phoneNumber;

    @Column(unique = true)
    private String email;

    @Column(name = "data_cadastro", nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type clientType;

    public Client() {
        this.createdAt = LocalDateTime.now(); // Define a data de cadastro automaticamente
    }

    public Client(String cpfCnpj, String name, String address, String phoneNumber, String email, Type clientType) {
        this.cpfCnpj = cpfCnpj;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.createdAt = LocalDateTime.now();
        this.clientType = clientType;
    }

    public Long getId() {
        return id;
    }

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Type getClientType() {
        return clientType;
    }

    public void setClientType(Type typeClient) {
        this.clientType = typeClient;
    }

    public enum Type {
        PESSOA_FISICA,
        PESSOA_JURIDICA
    }

    public void updateClient(Client client){

        this.name = client.getName();
        this.cpfCnpj = client.getCpfCnpj();
        this.address = client.getAddress();
        this.phoneNumber = client.getPhoneNumber();
        this.email = client.getEmail();
        this.clientType = client.getClientType();
    }
}
