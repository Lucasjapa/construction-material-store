package projectpoo.construction_material_store.dto;

import projectpoo.construction_material_store.domain.Client;
import projectpoo.construction_material_store.domain.Invoice;
import projectpoo.construction_material_store.domain.InvoiceItem;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class InvoiceDTO {

    private long id;
    private ClientDTO client;
    private Double totalPrice;
    private String saleDate;
    private List<InvoiceItemDTO> invoiceItens;
    private String status;

    public InvoiceDTO() {
    }

    public InvoiceDTO( ClientDTO client, Double totalPrice, List<InvoiceItemDTO> invoiceItens, String status) {
        this.client = client;
        this.totalPrice = totalPrice;
        this.invoiceItens = invoiceItens;
        this.status = status;
    }

    public InvoiceDTO(Invoice invoice) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        this.id = invoice.getId();
        this.client = invoice.getClient().toDTO();
        this.totalPrice = invoice.getTotalPrice();
        this.saleDate = invoice.getSaleDate().format(formatter);
        this.invoiceItens = invoice.getInvoiceItems().stream().map(InvoiceItemDTO::new).collect(Collectors.toList());
        this.status = invoice.getStatus().toString();
    }

    public long getId() {
        return id;
    }

    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(String saleDate) {
        this.saleDate = saleDate;
    }

    public List<InvoiceItemDTO> getInvoiceItens() {
        return invoiceItens;
    }

    public void setInvoiceItens(List<InvoiceItemDTO> invoiceItens) {
        this.invoiceItens = invoiceItens;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Método para converter de ProductDTO para Product
    public Invoice toInvoice(Client client, List<InvoiceItem> invoiceItems) {
        Invoice.Status statusEnum = Invoice.Status.valueOf(this.status);

        return new Invoice(
                client,
                this.totalPrice,
                invoiceItems,
                statusEnum
        );
    }
}
