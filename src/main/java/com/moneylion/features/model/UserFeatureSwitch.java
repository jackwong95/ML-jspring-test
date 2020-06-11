package com.moneylion.features.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table
public class UserFeatureSwitch implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private UserFeatureEmbeddedId id;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    public UserFeatureSwitch() {
    }

    public UserFeatureSwitch(UserFeatureEmbeddedId id, Boolean enabled) {
        this.id = id;
        this.enabled = enabled;
    }

    public UserFeatureEmbeddedId getId() {
        return id;
    }

    public void setId(UserFeatureEmbeddedId id) {
        this.id = id;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserFeatureSwitch other = (UserFeatureSwitch) o;
        return Objects.equals(id, other.id) &&
                Objects.equals(enabled, other.enabled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, enabled);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserFeatureSwitch{");
        sb.append("id=").append(id.toString());
        sb.append(", enabled='").append(enabled).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
