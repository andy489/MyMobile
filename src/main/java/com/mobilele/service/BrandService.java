package com.mobilele.service;

import com.mobilele.mapper.MapStructMapper;
import com.mobilele.model.dto.BrandDto;
import com.mobilele.model.entity.BrandEntity;
import com.mobilele.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BrandService {

    private final BrandRepository brandRepository;

    private final MapStructMapper mapper;

    @Autowired
    public BrandService(
            BrandRepository brandRepository,
            MapStructMapper mapper
    ) {
        this.brandRepository = brandRepository;
        this.mapper = mapper;
    }

    public List<BrandDto> getAllBrands() {

        // return brandRepository.findAll().stream().map(mapper::toDto).toList();

        // solving n + 1 queries problem
        return brandRepository.getAllBrands().stream().map(mapper::toDto).toList();
    }

    public BrandEntity getById(Long brandId) {
        return brandRepository.findById(brandId).orElseThrow(NoSuchElementException::new);
    }
}
