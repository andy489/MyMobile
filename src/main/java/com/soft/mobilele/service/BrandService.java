package com.soft.mobilele.service;

import com.soft.mobilele.mapper.MapStructMapper;
import com.soft.mobilele.model.dto.BrandDTO;
import com.soft.mobilele.model.entity.BrandEntity;
import com.soft.mobilele.repository.BrandRepository;
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

    public List<BrandDTO> getAllBrands() {

        // return brandRepository.findAll().stream().map(mapper::toDto).toList();

        // solving n + 1 queries problem
        return brandRepository.getAllBrands().stream().map(mapper::toDto).toList();
    }

    public BrandEntity getById(Long brandId) {
        return brandRepository.findById(brandId).orElseThrow(NoSuchElementException::new);
    }
}
