package org.keepcode;

import java.io.Serializable;
import java.util.Objects;

public class OfferedService implements Serializable {
    private String name;
    private Double price;

    public OfferedService(String name, Double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OfferedService offeredService = (OfferedService) o;

        if (!Objects.equals(name, offeredService.name)) return false;
        return Objects.equals(price, offeredService.price);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (price != null ? price.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Service{" +
                "name='" + name + '\'' +
                ", price='" + price + '\'' +
                '}';
    }


}
