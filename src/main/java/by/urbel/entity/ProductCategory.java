package by.urbel.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ProductCategory {
    private Long id;
    private String name;

    public ProductCategory(String name) {
        this.name = name;
    }
}
