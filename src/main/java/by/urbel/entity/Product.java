package by.urbel.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Product {
    private Long id;
    private String name;
    private ProductCategory category;
    private String description;
    private long price;
    private int quantity;
}
