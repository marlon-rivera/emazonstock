package com.emazon.stock.adapters.driven.jpa.mysql.adapter;

import com.emazon.stock.adapters.driven.jpa.mysql.entity.CategoryEntity;
import com.emazon.stock.adapters.driven.jpa.mysql.mapper.ICategoryEntityMapper;
import com.emazon.stock.adapters.driven.jpa.mysql.repository.ICategoryRepository;
import com.emazon.stock.domain.model.Category;
import com.emazon.stock.domain.model.PaginationInfo;
import com.emazon.stock.domain.spi.ICategoryPersistencePort;
import com.emazon.stock.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@RequiredArgsConstructor
public class CategoryAdapter implements ICategoryPersistencePort {
    private final ICategoryRepository repository;
    private final ICategoryEntityMapper mapper;

    @Override
    public void saveCategory(Category category) {
        repository.save(mapper.toEntity(category));
    }

    @Override
    public Optional<Category> findCategoryByName(String name) {
        return mapper.toCategoryOptional(repository.findByName(name));
    }

    @Override
    public PaginationInfo<Category> getAllCategories(int page, int size, String order) {
        Pageable pagination = PageRequest.of(page, size);
        Page<CategoryEntity> categories;
        if(order.equals(Constants.ORDER_ASC)){
            categories = repository.findAllByOrderByNameAsc(pagination);
        } else{
            categories = repository.findAllByOrderByNameDesc(pagination);
        }
        return new PaginationInfo<>(mapper.toCategoryList(categories.getContent()),
                categories.getNumber(),
                categories.getSize(),
                categories.getTotalElements(),
                categories.getTotalPages(),
                categories.hasNext(),
                categories.hasPrevious()
        );
    }


}
