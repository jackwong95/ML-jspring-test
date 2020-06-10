package com.moneylion.features.model;

// a class representing composite primary key of feature and user

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserFeatureEmbeddedId implements Serializable {

    public UserFeatureEmbeddedId() {}

    public UserFeatureEmbeddedId(Integer userId, Integer featureId) {
        this.userId = userId;
        this.featureId = featureId;
    }

    private static final long serialVersionUID = 1L;

    @Column(name = "u_id", nullable = false)
    private Integer userId;

    @Column(name = "f_id", nullable = false)
    private Integer featureId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getFeatureId() { return featureId; }

    public void setFeatureId(Integer featureId) { this.featureId = featureId; }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserFeatureEmbeddedId other = (UserFeatureEmbeddedId) o;
        return Objects.equals(userId, other.userId) &&
                Objects.equals(featureId, other.featureId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, featureId);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CompositeKey {");
        sb.append("userId=").append(userId);
        sb.append(", featureId='").append(featureId).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
