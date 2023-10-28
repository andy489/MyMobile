package com.soft.mobilele.repository;

import com.soft.mobilele.model.dto.OfferSearchDTO;
import com.soft.mobilele.model.entity.OfferEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class OfferSpecification implements Specification<OfferEntity> {

    private final OfferSearchDTO offerSearchDto;

    public OfferSpecification(OfferSearchDTO offerSearchDto) {
        this.offerSearchDto = offerSearchDto;
    }

    @Override
    public Predicate toPredicate(Root<OfferEntity> root,
                                 CriteriaQuery<?> query,
                                 CriteriaBuilder cb) {

        Predicate p = cb.conjunction();

        if (offerSearchDto.getModel() != null && !offerSearchDto.getModel().isEmpty()) {
            p.getExpressions().add(
                    cb.and(cb.equal(root.join("model").get("name"), offerSearchDto.getModel()))
            );
        }

        if (offerSearchDto.getMinPrice() != null) {
            p.getExpressions().add(
                    cb.and(cb.greaterThanOrEqualTo(root.get("price"), offerSearchDto.getMinPrice()))
            );
        }

        if (offerSearchDto.getMaxPrice() != null) {
            p.getExpressions().add(cb.and(cb.lessThanOrEqualTo(root.get("price"), offerSearchDto.getMaxPrice())));
        }

        if (offerSearchDto.getDescriptionInfix() != null && !offerSearchDto.getDescriptionInfix().isBlank()) {
            p.getExpressions().add(
                    cb.and(cb.like(root.get("description"), "%" + offerSearchDto.getDescriptionInfix() + "%")));
        }

        if (offerSearchDto.getYearFrom() != null) {
            p.getExpressions().add(cb.and(cb.greaterThanOrEqualTo(root.get("year"), offerSearchDto.getYearFrom())));
        }

        if (offerSearchDto.getYearTo() != null) {
            p.getExpressions().add(cb.and(cb.lessThanOrEqualTo(root.get("year"), offerSearchDto.getYearTo())));
        }

        if (offerSearchDto.getMileageFrom() != null) {
            p.getExpressions().add(
                    cb.and(cb.greaterThanOrEqualTo(root.get("mileage"), offerSearchDto.getMileageFrom())));
        }

        if (offerSearchDto.getMileageTo() != null) {
            p.getExpressions().add(cb.and(cb.lessThanOrEqualTo(root.get("mileage"), offerSearchDto.getMileageTo())));
        }

        if (offerSearchDto.getEngineType() != null) {
            p.getExpressions().add(cb.and(cb.equal(root.get("engine"), offerSearchDto.getEngineType())));
        }

        if (offerSearchDto.getTransmissionType() != null) {
            p.getExpressions().add(cb.and(cb.equal(root.get("transmission"), offerSearchDto.getTransmissionType())));
        }

        // Additional
        if (offerSearchDto.getAwd() != null && offerSearchDto.getAwd()) {
            p.getExpressions().add(cb.and(cb.isTrue(root.get("awd"))));
        }

        if (offerSearchDto.getClimatic() != null && offerSearchDto.getClimatic()) {
            p.getExpressions().add(cb.and(cb.isTrue(root.get("climatic"))));
        }

        if (offerSearchDto.getParktronic() != null && offerSearchDto.getParktronic()) {
            p.getExpressions().add(cb.and(cb.isTrue(root.get("parktronic"))));
        }

        if (offerSearchDto.getNavi() != null && offerSearchDto.getNavi()) {
            p.getExpressions().add(cb.and(cb.isTrue(root.get("navi"))));
        }

        if (offerSearchDto.getLeasing() != null && offerSearchDto.getLeasing()) {
            p.getExpressions().add(cb.and(cb.isTrue(root.get("leasing"))));
        }

        if (offerSearchDto.getXenonLights() != null && offerSearchDto.getXenonLights()) {
            p.getExpressions().add(cb.and(cb.isTrue(root.get("xenonLights"))));
        }

        if (offerSearchDto.getIsofixSystem() != null && offerSearchDto.getIsofixSystem()) {
            p.getExpressions().add(cb.and(cb.isTrue(root.get("isofixSystem"))));
        }

        if (offerSearchDto.getAntiBlockingSystem() != null && offerSearchDto.getAntiBlockingSystem()) {
            p.getExpressions().add(cb.and(cb.isTrue(root.get("antiBlockingSystem"))));
        }

        if (offerSearchDto.getCabriolet() != null && offerSearchDto.getCabriolet()) {
            p.getExpressions().add(cb.and(cb.isTrue(root.get("cabriolet"))));
        }

        if (offerSearchDto.getBluetooth() != null && offerSearchDto.getBluetooth()) {
            p.getExpressions().add(cb.and(cb.isTrue(root.get("bluetooth"))));
        }

        // return predicate
        return p;
    }
}