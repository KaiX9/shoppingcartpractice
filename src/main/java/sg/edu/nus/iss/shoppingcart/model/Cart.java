package sg.edu.nus.iss.shoppingcart.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import jakarta.json.JsonObject;

public class Cart implements Serializable {
    
    private List<Item> cart = new LinkedList<Item>();

    public List<Item> getCart() {
        return cart;
    }

    public void setCart(List<Item> cart) {
        this.cart = cart;
    }

    public void addItem(Item item) {
        List<Item> currCart = this.cart.stream()
                .filter(i -> i.getItem().equals(item.getItem()))
                .toList();
        if (currCart.isEmpty()) {
            this.cart.add(item);
        } else {
            currCart.get(0).add(item.getQuantity());
        }
    }

    public static Cart createFromJSON(JsonObject o) {
        Cart cart = new Cart();
        return cart;
    }
}
