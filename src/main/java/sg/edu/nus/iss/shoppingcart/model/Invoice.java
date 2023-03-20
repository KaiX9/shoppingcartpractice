package sg.edu.nus.iss.shoppingcart.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class Invoice implements Serializable {
    
    private String invoiceId;

    private float totalCost;

    private Cart cart;

    private ShippingAddress shippingAddress;

    public Invoice(Cart cart, ShippingAddress shippingAddress) {
        this.cart = cart;
        this.shippingAddress = shippingAddress;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public float getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(float totalCost) {
        this.totalCost = totalCost;
    }

    public ShippingAddress getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(ShippingAddress shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
            .add("invoiceId", this.getInvoiceId())
            .add("name", this.getShippingAddress().getName())
            .add("address", this.getShippingAddress().getAddress())
            .add("total", this.getTotalCost())
            .build();
    }

    public static Invoice createFromJSON(String json) throws IOException {
        InputStream is = new ByteArrayInputStream(json.getBytes());
        JsonReader r = Json.createReader(is);
        JsonObject o = r.readObject();
        Cart cart = Cart.createFromJSON(o);
        ShippingAddress shippingAddress = ShippingAddress.createFromJSON(o);
        Invoice invoice = new Invoice(cart, shippingAddress);
        invoice.setInvoiceId(o.getString("invoiceId"));
        invoice.setTotalCost((float)o.getJsonNumber("total").doubleValue());

        return invoice;
    }
}
