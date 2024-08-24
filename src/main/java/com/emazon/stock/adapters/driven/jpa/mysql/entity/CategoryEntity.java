package com.emazon.stock.adapters.driven.jpa.mysql.entity;

import com.emazon.stock.utils.Constants;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false, length = Constants.MAX_CHARACTERS_NAME_CATEGORY)
    private String name;
    @Column(nullable = false, length = Constants.MAX_CHARACTERS_DESCRIPTION_CATEGORY)
    private String description;

}
