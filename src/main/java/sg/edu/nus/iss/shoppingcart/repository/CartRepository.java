package sg.edu.nus.iss.shoppingcart.repository;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.shoppingcart.model.Invoice;

@Repository
public class CartRepository {
    
    @Autowired @Qualifier("cart")
    private RedisTemplate<String, String> template;

    public void saveOrder(Invoice invoice) {
        this.template.opsForValue().set(invoice.getInvoiceId(), invoice.toJSON().toString());
    }

    public Optional<Invoice> getInvoiceDetails(String invoiceId) throws IOException {
        String json = template.opsForValue().get(invoiceId);
        if ((json == null || json.trim().length() <= 0)) {
            return Optional.empty();
        }
        return Optional.of(Invoice.createFromJSON(json));
    }
}
