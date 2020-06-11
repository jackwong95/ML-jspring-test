package com.moneylion.features.repository;

// An abstraction layer which handles only business objects/concept
// encapsulates the model / table entities underlying

public interface FeatureSwitchRepository {

    // returns true if user canAccess to feature
    boolean canUserAccessFeature(String email, String featureName);

    void upsertUserAccessFeature(String email, String featureName, Boolean enabled);
}
