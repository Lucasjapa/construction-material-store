package projectpoo.construction_material_store.dto;

import java.util.List;

public class InvoiceDTO {

    private long id;
    private ClientDTO client;
    private String totalPrice;
    private String saleDate;
    private List<InvoiceDTO> invoiceItens;

    public InvoiceDTO() {
    }

    public InvoiceDTO(String totalPrice, ClientDTO client, String saleDate, List<InvoiceDTO> invoiceItens) {
        this.totalPrice = totalPrice;
        this.client = client;
        this.saleDate = saleDate;
        this.invoiceItens = invoiceItens;
    }

    public InvoiceDTO(InvoiceDTO invoiceDTO) {
        this.id = invoiceDTO.getId();
        this.client = invoiceDTO.getClient();
        this.totalPrice = invoiceDTO.getTotalPrice();
        this.saleDate = invoiceDTO.getSaleDate();
        this.invoiceItens = invoiceDTO.getInvoiceItens();
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

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(String saleDate) {
        this.saleDate = saleDate;
    }

    public List<InvoiceDTO> getInvoiceItens() {
        return invoiceItens;
    }

    public void setInvoiceItens(List<InvoiceDTO> invoiceItens) {
        this.invoiceItens = invoiceItens;
    }
}
