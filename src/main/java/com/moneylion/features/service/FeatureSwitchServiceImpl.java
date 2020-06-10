package com.moneylion.features.service;

import com.moneylion.features.repository.FeatureSwitchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeatureSwitchServiceImpl implements FeatureSwitchService {

    @Autowired
    private FeatureSwitchRepository featureSwitchRepository;

    @Override
    public boolean canUserAccessFeature(String email, String featureName) {
        return featureSwitchRepository.canUserAccessFeature(email, featureName);
    }

    @Override
    public void upsertFeature(String email, String featureName, Boolean enabled) {
        featureSwitchRepository.upsertFeature(email, featureName, enabled);
    }
}
