package com.soft.mobilele.service;

import com.soft.mobilele.model.entity.ModelEntity;
import com.soft.mobilele.repository.ModelRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ModelService {
    private final ModelRepository modelRepository;

    public ModelService(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }

    public ModelEntity getById(Long modelId) {
        return modelRepository.findById(modelId).orElseThrow(() -> new NoSuchElementException("No such model"));
    }
}
