package edu.iCET.aspect;

import edu.iCET.annotation.AuditFailure;
import edu.iCET.model.entity.AuditLog;
import edu.iCET.repository.AuditLogRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
public class AuditAspect {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @AfterThrowing(pointcut = "@annotation(auditFailure)", throwing = "ex")
    public void logFailure(JoinPoint joinPoint, AuditFailure auditFailure, Throwable ex) {

        Long userId = null;

        // Try to extract userId from method args
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof Long l) {
                userId = l;  // assumes first Long is userId
                break;
            }
        }

        AuditLog log = new AuditLog();
        log.setAction("BOOKING_FAILED");
        log.setUserId(userId);
        log.setDetails(ex.getMessage());
        log.setTimestamp(LocalDateTime.now());

        auditLogRepository.save(log);

        System.out.println("Audit log saved: " + log);
    }
}
