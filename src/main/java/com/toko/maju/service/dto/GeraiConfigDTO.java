package com.toko.maju.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the GeraiConfig entity.
 */
public class GeraiConfigDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 4, max = 10)
    private String codeGerai;

    @NotNull
    private String nameGerai;

    @NotNull
    @Size(min = 5)
    private String password;

    @NotNull
    private String urlToko;

    @NotNull
    private Boolean activated;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeGerai() {
        return codeGerai;
    }

    public void setCodeGerai(String codeGerai) {
        this.codeGerai = codeGerai;
    }

    public String getNameGerai() {
        return nameGerai;
    }

    public void setNameGerai(String nameGerai) {
        this.nameGerai = nameGerai;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrlToko() {
        return urlToko;
    }

    public void setUrlToko(String urlToko) {
        this.urlToko = urlToko;
    }

    public Boolean isActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GeraiConfigDTO geraiConfigDTO = (GeraiConfigDTO) o;
        if (geraiConfigDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), geraiConfigDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GeraiConfigDTO{" +
            "id=" + getId() +
            ", codeGerai='" + getCodeGerai() + "'" +
            ", nameGerai='" + getNameGerai() + "'" +
            ", password='" + getPassword() + "'" +
            ", urlToko='" + getUrlToko() + "'" +
            ", activated='" + isActivated() + "'" +
            "}";
    }
}
