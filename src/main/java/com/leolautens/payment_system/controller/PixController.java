package com.leolautens.payment_system.controller;

import com.leolautens.payment_system.dto.PixChargeRequest;
import com.leolautens.payment_system.service.PixService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pix")

public class PixController {

    @Autowired
    private PixService pixService;

    @GetMapping("/createKey")
    public ResponseEntity pixCreateEvp() {
        JSONObject response = this.pixService.pixCreateEvp();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response.toString());
    }

    @PostMapping("/createPix")
    public ResponseEntity pixCreateCharge(@RequestBody PixChargeRequest pixChargeRequest) {
        String response = this.pixService.pixCreateCharge(pixChargeRequest);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/listEvp")
    public ResponseEntity pixListEvp() {
        JSONObject response = this.pixService.listEVP();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response.toString());
    }

}
