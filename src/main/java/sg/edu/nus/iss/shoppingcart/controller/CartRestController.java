package sg.edu.nus.iss.shoppingcart.controller;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import sg.edu.nus.iss.shoppingcart.model.Invoice;
import sg.edu.nus.iss.shoppingcart.service.CartService;

@RestController
@RequestMapping(path="/cart")
public class CartRestController {
    
    @Autowired
    private CartService cartSvc;

    @GetMapping(path="{invoiceId}")
    public ResponseEntity<String> getInvoiceDetails(@PathVariable String invoiceId) throws IOException {
        Optional<Invoice> invoice = cartSvc.getInvoiceByInvoiceId(invoiceId);
        if(invoice.isEmpty()) {
            JsonObject error = Json.createObjectBuilder()
                .add("message", "Invoice %s not found".formatted(invoiceId))
                .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error.toString());
        }
        return ResponseEntity.ok(invoice.get().toJSON().toString());
    }
}
