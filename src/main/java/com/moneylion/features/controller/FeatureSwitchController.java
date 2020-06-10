package com.moneylion.features.controller;

import com.moneylion.features.exception.InvalidFeatureException;
import com.moneylion.features.exception.InvalidUserException;
import com.moneylion.features.service.FeatureSwitchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class FeatureSwitchController {

    @Autowired
    FeatureSwitchService featureSwitchService;

    @GetMapping(value = "/feature", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> canUserAccessFeature (
            @RequestParam("email") String email,
            @RequestParam("featureName") String featureName
    ) {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("canAccess", featureSwitchService.canUserAccessFeature(email, featureName));

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (InvalidUserException e) {
            return new ResponseEntity<>("Email cannot be found", HttpStatus.BAD_REQUEST);
        } catch (InvalidFeatureException e) {
            return new ResponseEntity<>("Feature cannot be found", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/feature", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> upsertUserAccessFeature (
            @RequestBody Map<String, Object> payload
    ) {
        try {
            String email = (String) payload.get("email");
            String featureName = (String) payload.get("featureName");
            Boolean isEnabled = (Boolean) payload.get("enable");

            featureSwitchService.upsertFeature(email, featureName, isEnabled);

        } catch (ClassCastException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        } catch (InvalidUserException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        } catch (InvalidFeatureException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
