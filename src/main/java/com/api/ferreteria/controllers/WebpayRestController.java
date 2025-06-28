package com.api.ferreteria.controllers;

import com.api.ferreteria.services.WebpayRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/webpay-rest")
@CrossOrigin(origins = "*")
public class WebpayRestController {

    @Autowired
    private WebpayRestService webpayRestService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestParam int amount) {
        String returnUrl = "http://localhost:8080/api/webpay-rest/commit";
        try {
            Object result = webpayRestService.createTransaction(amount, returnUrl);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error en la transacción: " + e.getMessage());
        }
    }

    @GetMapping("/commit")
    public ResponseEntity<?> commit(@RequestParam String token_ws) {
        return ResponseEntity.ok(webpayRestService.commitTransaction(token_ws));
    }
}