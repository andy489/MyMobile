package com.mobilele.service.aop;

import org.aspectj.lang.annotation.Pointcut;
public class Pointcuts {

    @Pointcut("execution(* com.mobilele.service.OfferService.getOffers(..))")
    public void trackOfferSearch() {}


    @Pointcut("@annotation(WarnIfExecutionExceeds)")
    public void warnIfTimeExceeds() {}

}
