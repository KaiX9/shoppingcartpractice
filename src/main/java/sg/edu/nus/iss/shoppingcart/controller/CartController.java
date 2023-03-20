package sg.edu.nus.iss.shoppingcart.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import sg.edu.nus.iss.shoppingcart.model.Cart;
import sg.edu.nus.iss.shoppingcart.model.Invoice;
import sg.edu.nus.iss.shoppingcart.model.Item;
import sg.edu.nus.iss.shoppingcart.model.ShippingAddress;
import sg.edu.nus.iss.shoppingcart.service.CartService;

@Controller
@RequestMapping
public class CartController {
    
    @Autowired
    private CartService cartSvc;

    @GetMapping(path={"/", "/index.html"})
    public String cartForm(Model m, HttpSession s) {
        Cart cart = (Cart) s.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            s.setAttribute("cart", cart);
        }
        m.addAttribute("item", new Item());
        m.addAttribute("cart", cart);
        return "view1";
    }

    @PostMapping(path="/item")
    public String validateForm(Model m, HttpSession s, @ModelAttribute @Valid Item item, BindingResult binding) {
        Cart cart = (Cart) s.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            s.setAttribute("cart", cart);
        }
        m.addAttribute("cart", cart);
        
        if (binding.hasErrors()) {
            return "view1";
        }

        List<ObjectError> errors = cartSvc.validateItem(item);
        if (!errors.isEmpty()) {
            for (ObjectError e : errors) {
                binding.addError(e);
            }
            return "view1";
        }

        cart.addItem(item);
        m.addAttribute("item", new Item());

        return "view1";
    }

    @GetMapping(path="/shippingaddress")
    public String getShippingAddress(Model m, HttpSession s) {
        Cart cart = (Cart) s.getAttribute("cart");
        if (cart == null) {
            return "view1";
        }

        m.addAttribute("shippingAddress", new ShippingAddress());

        return "view2";
    }

    @PostMapping(path="/checkout")
    public String checkOut(Model m, HttpSession s
        , @ModelAttribute @Valid ShippingAddress shippingAddress, BindingResult binding) {
        Cart cart = (Cart) s.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            s.setAttribute("cart", cart);

            return "view1";
        }

        if (binding.hasErrors()) {
            return "view2";
        }

        Invoice invoice = cartSvc.saveOrder(cart, shippingAddress);
        m.addAttribute("invoice", invoice);
        s.invalidate();
        return "view3";
    }
    
}
