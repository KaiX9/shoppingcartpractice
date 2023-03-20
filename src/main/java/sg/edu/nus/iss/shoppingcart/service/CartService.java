package sg.edu.nus.iss.shoppingcart.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import sg.edu.nus.iss.shoppingcart.model.Cart;
import sg.edu.nus.iss.shoppingcart.model.Invoice;
import sg.edu.nus.iss.shoppingcart.model.Item;
import sg.edu.nus.iss.shoppingcart.model.ShippingAddress;
import sg.edu.nus.iss.shoppingcart.repository.CartRepository;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepo;
    
    public static final String[] ITEM_CHOICE = {"apple", "orange"
        , "bread", "cheese", "chicken", "mineral_water", "instant_noodles"};

    private final Set<String> itemChoice;

    public CartService() {
        itemChoice = new HashSet<String>(Arrays.asList(ITEM_CHOICE));
    }

    public List<ObjectError> validateItem(Item item) {
        List<ObjectError> errors = new LinkedList<>();

        if (!itemChoice.contains(item.getItem().toLowerCase())) {
            FieldError e = new FieldError("cart", "item", "We do not stock %s".formatted(item.getItem()));
            errors.add(e);
        }
        return errors;
    }

    public Invoice createInvoice(Cart cart, ShippingAddress shippingAddress) {
        String invoiceId = UUID.randomUUID().toString().substring(0, 4);
        Invoice invoice = new Invoice(cart, shippingAddress);
        invoice.setInvoiceId(invoiceId);
        float totalCost = 0.00f;
        for (Item item : cart.getCart()) {
            if (item.getItem().contains("apple") 
            || item.getItem().contains("orange") 
            || item.getItem().contains("bread") 
            || item.getItem().contains("cheese") 
            || item.getItem().contains("chicken")) {
                totalCost += (1 * item.getQuantity());
            }
            
            if (item.getItem().contains("mineral_water") 
            || item.getItem().contains("instant_noodles")) {
                totalCost += (2 * item.getQuantity());
            }
        }

        invoice.setTotalCost(totalCost);
        invoice.setShippingAddress(shippingAddress);

        return invoice;
    }

    public Invoice saveOrder(Cart cart, ShippingAddress shippingAddress) {
        Invoice invoice = createInvoice(cart, shippingAddress);
        cartRepo.saveOrder(invoice);
        return invoice;
    }

    public Optional<Invoice> getInvoiceByInvoiceId(String invoiceId) throws IOException {
        return cartRepo.getInvoiceDetails(invoiceId);
    }

}
