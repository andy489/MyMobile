package com.mymobile.service.aop;

import org.aspectj.lang.annotation.Pointcut;
public class Pointcuts {

    @Pointcut("execution(* com.mymobile.service.OfferService.getOffers(..))")
    public void trackOfferSearch() {}


    @Pointcut("@annotation(WarnIfExecutionExceeds)")
    public void warnIfTimeExceeds() {}

}
