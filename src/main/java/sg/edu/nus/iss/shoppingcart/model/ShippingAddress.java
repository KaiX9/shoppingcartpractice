package sg.edu.nus.iss.shoppingcart.model;

import java.io.Serializable;

import jakarta.json.JsonObject;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ShippingAddress implements Serializable {
    
    @NotBlank(message="Please state your name")
    @Size(min=2, message="Name has to be at least 2 characters long")
    private String name;

    @NotBlank(message="Please state your address")
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "ShippingAddress [name=" + name + ", address=" + address + "]";
    }

    public static ShippingAddress createFromJSON(JsonObject o) {
        ShippingAddress shippingAddress = new ShippingAddress();
        shippingAddress.setName(o.getString("name"));
        shippingAddress.setAddress(o.getString("address"));

        return shippingAddress;
    }
}
