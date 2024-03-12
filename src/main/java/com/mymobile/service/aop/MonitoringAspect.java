package com.mymobile.service.aop;

import com.mymobile.service.monitoring.OfferSearchMonitoringService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.lang.reflect.Method;

@Aspect
@Component
public class MonitoringAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonitoringAspect.class);

    private final OfferSearchMonitoringService offerSearchMonitoringService;

    public MonitoringAspect(OfferSearchMonitoringService offerSearchMonitoringService) {

        this.offerSearchMonitoringService = offerSearchMonitoringService;
    }

    @Before("com.mymobile.service.aop.Pointcuts.trackOfferSearch()")
    public void logOfferSearch() {
        offerSearchMonitoringService.logOffersSearch();
    }

    @Around("com.mymobile.service.aop.Pointcuts.warnIfTimeExceeds()")
    public Object logExecutionTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        WarnIfExecutionExceeds annotation = getAnnotation(proceedingJoinPoint);
        long timeout = annotation.timeInMillis();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        // timeout logic
        var returnValue = proceedingJoinPoint.proceed();

        stopWatch.stop();

        if (stopWatch.getLastTaskTimeMillis() > timeout) {
            LOGGER.warn("The method {} ran for {} millis which is more than the " +
                    "expected {} millis.",
                    proceedingJoinPoint.getSignature(),
                    stopWatch.getLastTaskTimeMillis(),
                    timeout);
        }

        return returnValue;
    }

    private static WarnIfExecutionExceeds getAnnotation(ProceedingJoinPoint proceedingJoinPoint) {

        Method method = ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod();

        try {
            return proceedingJoinPoint
                    .getTarget()
                    .getClass()
                    .getMethod(method.getName(), method.getParameterTypes())
                    .getAnnotation(WarnIfExecutionExceeds.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
