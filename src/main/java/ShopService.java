import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class ShopService {
    private ProductRepo productRepo = new ProductRepo();
    private OrderRepo orderRepo = new OrderMapRepo();

    public Order addOrder(List<String> productIds) throws ProductNotFoundException {
        List<Product> products = new ArrayList<>();
        for (String productId : productIds) {
            Optional<Product> productToOrder = productRepo.getProductById(productId);
            if (productToOrder.isEmpty()) {
                throw new ProductNotFoundException("Produkt mit der ID " + productId + " wurde nicht gefunden!");
            }
            products.add(productToOrder.get());
        }

        // Bestellung erstellen und hinzufügen
        Order newOrder = new Order(UUID.randomUUID().toString(), products, OrderStatus.VERARBEITUNG, Instant.now());
        return orderRepo.addOrder(newOrder);
    }

    public List<Order> getOrdersByStatus(OrderStatus status) {

        List<Order> orders = orderRepo.getOrders();
        return orders.stream()
                .filter(order -> order.status() == status)
                .collect(Collectors.toList());
    }

    public void updateOrder(String orderId, OrderStatus newStatus) {
        Order oldOrder = orderRepo.getOrderById(orderId);
        orderRepo.removeOrder(orderId);
        Order newOrder = oldOrder.withStatus(newStatus);
        orderRepo.addOrder(newOrder);
    }

}

