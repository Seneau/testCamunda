package com.seneau.security.voters;

import org.springframework.stereotype.Component;

@Component
public class CongesCheck {
    public boolean check() {
        String name= "saly";
        return name.equals("saly");
    }
}
