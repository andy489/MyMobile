package com.soft.mobilele.repository;

import com.soft.mobilele.model.dto.OfferSearchDTO;
import com.soft.mobilele.model.entity.OfferEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class OfferSpecification implements Specification<OfferEntity> {

    private final OfferSearchDTO offerSearchDto;

    public OfferSpecification(OfferSearchDTO offerSearchDto) {
        this.offerSearchDto = offerSearchDto;
    }

    @Override
    public Predicate toPredicate(Root<OfferEntity> root,
                                 CriteriaQuery<?> query,
                                 CriteriaBuilder cb) {

        final List<Predicate> predicates = new ArrayList<>();

        if (offerSearchDto.getModel() != null && !offerSearchDto.getModel().isEmpty()) {
            predicates.add(cb.and(cb.equal(root.join("model").get("name"), offerSearchDto.getModel())));
        }

        if (offerSearchDto.getMinPrice() != null) {
            predicates.add(cb.and(cb.greaterThanOrEqualTo(root.get("price"), offerSearchDto.getMinPrice())));
        }

        if (offerSearchDto.getMaxPrice() != null) {
            predicates.add(cb.and(cb.lessThanOrEqualTo(root.get("price"), offerSearchDto.getMaxPrice())));
        }

        if (offerSearchDto.getDescriptionInfix() != null && !offerSearchDto.getDescriptionInfix().isBlank()) {
            predicates.add(cb.and(cb.like(root.get("description"), "%" + offerSearchDto.getDescriptionInfix() + "%")));
        }

        if (offerSearchDto.getYearFrom() != null) {
            predicates.add(cb.and(cb.greaterThanOrEqualTo(root.get("year"), offerSearchDto.getYearFrom())));
        }

        if (offerSearchDto.getYearTo() != null) {
            predicates.add(cb.and(cb.lessThanOrEqualTo(root.get("year"), offerSearchDto.getYearTo())));
        }

        if (offerSearchDto.getMileageFrom() != null) {
            predicates.add(cb.and(cb.greaterThanOrEqualTo(root.get("mileage"), offerSearchDto.getMileageFrom())));
        }

        if (offerSearchDto.getMileageTo() != null) {
            predicates.add(cb.and(cb.lessThanOrEqualTo(root.get("mileage"), offerSearchDto.getMileageTo())));
        }

        if (offerSearchDto.getEngineType() != null) {
            predicates.add(cb.and(cb.equal(root.get("engine"), offerSearchDto.getEngineType())));
        }

        if (offerSearchDto.getTransmissionType() != null) {
            predicates.add(cb.and(cb.equal(root.get("transmission"), offerSearchDto.getTransmissionType())));
        }

        // Additional
        if (offerSearchDto.getAwd() != null && offerSearchDto.getAwd()) {
            predicates.add(cb.and(cb.isTrue(root.get("awd"))));
        }

        if (offerSearchDto.getClimatic() != null && offerSearchDto.getClimatic()) {
            predicates.add(cb.and(cb.isTrue(root.get("climatic"))));
        }

        if (offerSearchDto.getParktronic() != null && offerSearchDto.getParktronic()) {
            predicates.add(cb.and(cb.isTrue(root.get("parktronic"))));
        }

        if (offerSearchDto.getNavi() != null && offerSearchDto.getNavi()) {
            predicates.add(cb.and(cb.isTrue(root.get("navi"))));
        }

        if (offerSearchDto.getLeasing() != null && offerSearchDto.getLeasing()) {
            predicates.add(cb.and(cb.isTrue(root.get("leasing"))));
        }

        if (offerSearchDto.getXenonLights() != null && offerSearchDto.getXenonLights()) {
            predicates.add(cb.and(cb.isTrue(root.get("xenonLights"))));
        }

        if (offerSearchDto.getIsofixSystem() != null && offerSearchDto.getIsofixSystem()) {
            predicates.add(cb.and(cb.isTrue(root.get("isofixSystem"))));
        }

        if (offerSearchDto.getAntiBlockingSystem() != null && offerSearchDto.getAntiBlockingSystem()) {
            predicates.add(cb.and(cb.isTrue(root.get("antiBlockingSystem"))));
        }

        if (offerSearchDto.getCabriolet() != null && offerSearchDto.getCabriolet()) {
            predicates.add(cb.and(cb.isTrue(root.get("cabriolet"))));
        }

        if (offerSearchDto.getBluetooth() != null && offerSearchDto.getBluetooth()) {
            predicates.add(cb.and(cb.isTrue(root.get("bluetooth"))));
        }

        // return predicate
        return cb.and(predicates.toArray(new Predicate[0]));
    }
}