package projectpoo.construction_material_store.dto;

import projectpoo.construction_material_store.domain.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class PurchaseDTO {

    private long id;
    private Double totalPrice;
    private String purchaseDate;
    private List<PurchaseItemDTO> purchaseItens;

    public PurchaseDTO() {
    }

    public PurchaseDTO(Double totalPrice, List<PurchaseItemDTO> purchaseItens) {
        this.totalPrice = totalPrice;
        this.purchaseItens = purchaseItens;
    }

    public PurchaseDTO(Purchase purchase) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        this.id = purchase.getId();
        this.totalPrice = purchase.getTotalPrice();
        this.purchaseDate = purchase.getPurchaseDate().format(formatter);
        this.purchaseItens = purchase.getPurchaseItems().stream().map(PurchaseItemDTO::new).collect(Collectors.toList());
    }

    public long getId() {
        return id;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public List<PurchaseItemDTO> getPurchaseItens() {
        return purchaseItens;
    }

    public void setPurchaseItens(List<PurchaseItemDTO> purchaseItens) {
        this.purchaseItens = purchaseItens;
    }

    public Purchase toInvoice(List<PurchaseItem> purchaseItems) {

        return new Purchase(
                this.totalPrice,
                purchaseItems
        );
    }
}
