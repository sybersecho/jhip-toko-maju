package com.toko.maju.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the CustomerProduct entity.
 */
public class CustomerProductDTO implements Serializable {

    private Long id;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CustomerProductDTO customerProductDTO = (CustomerProductDTO) o;
        if (customerProductDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), customerProductDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CustomerProductDTO{" +
            "id=" + getId() +
            "}";
    }
}
