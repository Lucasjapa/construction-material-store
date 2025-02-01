package projectpoo.construction_material_store.dto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DashboardDTO {

    private long countSales;
    private List<Map<String, Object>> topProducts;


    public DashboardDTO() {
    }

    // Construtor para aceitar topProducts como List<Object[]> e convertê-lo para List<Map<String, Object>>
    public DashboardDTO(List<Object[]> topProducts, long countSales) {
        this.countSales = countSales;

        // Convertendo List<Object[]> para List<Map<String, Object>>
        this.topProducts = topProducts.stream()
                .map(obj -> Map.of(
                        "name", obj[0],     // O nome do produto está em obj[0]
                        "quantity", obj[1]  // A quantidade de vendas está em obj[1]
                ))
                .collect(Collectors.toList());
    }

    public long getCountSales() {
        return countSales;
    }

    public void setCountSales(long countSales) {
        this.countSales = countSales;
    }

    public List<Map<String, Object>> getTopProducts() {
        return topProducts;
    }

    public void setTopProducts(List<Map<String, Object>> topProducts) {
        this.topProducts = topProducts;
    }
}
