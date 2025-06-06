package com.hotproperties.hotproperties.controllers;

import com.hotproperties.hotproperties.entities.Property;
import com.hotproperties.hotproperties.services.PropertyService;
import com.hotproperties.hotproperties.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BuyerController {

    private final PropertyService propertyService;
    private final UserService userService;

    public BuyerController(PropertyService propertyService, UserService userService) {
        this.propertyService = propertyService;
        this.userService = userService;
    }

    @PreAuthorize("hasRole('BUYER')")
    @GetMapping("/properties/list")
    public String viewAllProperties(@RequestParam(required = false) String zip,
                                    @RequestParam(required = false) Integer minSqFt,
                                    @RequestParam(required = false) Integer minPrice,
                                    @RequestParam(required = false) Integer maxPrice,
                                    @RequestParam(required = false) String sort,
                                    Model model) {
        model.addAttribute("properties", propertyService.getAllProperties());
        return "/buyer/browse-properties";
    }

    @PreAuthorize("hasRole('BUYER')")
    @GetMapping("/favorites")
    public String viewFavoriteProperties(Model model) {
        model.addAttribute("properties", propertyService.getFavoriteProperties());
        return "/buyer/saved-favorites";
    }

    @PreAuthorize("hasRole('BUYER')")
    @GetMapping("/properties/view/{property_id}")
    public String viewPropertyDetail(Model model, @PathVariable Long property_id) {
        model.addAttribute("property", propertyService.viewPropertyDetail(property_id));
        boolean isFavorite = propertyService.isPropertyFavoritedByCurrentUser(property_id);
        model.addAttribute("isFavorite", isFavorite);
        return "/buyer/view-property-details";
    }

    @PreAuthorize("hasRole('BUYER')")
    @GetMapping("/favorites/add/{property_id}")
    public String addFavorite(@PathVariable Long property_id) {
        propertyService.addPropertyToFavorites(property_id);
        return "redirect:/properties/view/{property_id}";
    }

    @PreAuthorize("hasRole('BUYER')")
    @GetMapping("/favorites/remove/{property_id}")
    public String removeFavoriteFromPropertyDetailsPage(@PathVariable Long property_id) {
        propertyService.removePropertyFromFavorites(property_id);
        return "redirect:/properties/view/{property_id}";
    }

    @PreAuthorize("hasRole('BUYER')")
    @GetMapping("/favorites/remove/favoritesPage/{property_id}")
    public String removeFavoriteFromFavoritesPage(@PathVariable Long property_id) {
        propertyService.removePropertyFromFavorites(property_id);
        return "redirect:/favorites";
    }


}
