package ru.gb.maintanence.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.springframework.stereotype.Component;

@Component
@Aspect
public class TimerAspect {

    /**
     * Метод, измеряющий время выполнения метода в package 'ru.gb.maintanence.controllers'
     *      для всех методов всех классов.
     * @param proceedingJoinPoint место (целевой метод), где внедряется аспект
     * @return  result - целевой объект
     * @throws Throwable
     */
    @Around("execution(* ru.gb.maintanence.controllers.*.*(..))")
    public Object timeMeasuring(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startedTime = System.currentTimeMillis();

        try {
            Object result = proceedingJoinPoint.proceed();

            long finishedTime = System.currentTimeMillis() - startedTime;
            System.out.println("Class name: " + proceedingJoinPoint.getSignature().getDeclaringType() +
                    "\nMethod name: " + proceedingJoinPoint.getSignature().getName() +
                    "\nExecution time: " + finishedTime + " ms");

            return result;
        } catch (Throwable e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
