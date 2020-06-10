package com.moneylion.features.service;

// Feature Switch Service representing a business use case

public interface FeatureSwitchService {

    // returns true if user canAccess to feature
    boolean canUserAccessFeature (String email, String featureName);

    // perform an update or insert based on given params
    void upsertFeature (String email, String featureName, Boolean enabled);
}
