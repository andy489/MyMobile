package com.mymobile.service;

import com.mymobile.model.entity.ModelEntity;
import com.mymobile.repository.ModelRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

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
