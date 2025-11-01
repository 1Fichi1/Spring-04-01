package com.example.soratech.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class RedirectHelper {
    
    public static String getEntityRedirect(String entity) {
        return getEntityRedirect(entity, false);
    }
    
    public static String getEntityRedirect(String entity, boolean viewDeleted) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.getAuthorities() != null) {
            boolean isAdmin = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch(role -> role.equals("ROLE_Администратор"));
            
            String panel = isAdmin ? "admin" : "manager";
            String redirect = "redirect:/" + panel + "?entity=" + entity;
            
            if (viewDeleted) {
                redirect += "&view=deleted";
            }
            
            return redirect;
        }
        
        // Default to admin if no authentication found
        return viewDeleted ? 
            "redirect:/admin?entity=" + entity + "&view=deleted" : 
            "redirect:/admin?entity=" + entity;
    }
}




