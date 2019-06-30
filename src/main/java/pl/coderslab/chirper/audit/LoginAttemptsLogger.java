package pl.coderslab.chirper.audit;

import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.listener.AuditApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@Component
public class LoginAttemptsLogger {

    @EventListener
    public void auditEventHappened(AuditApplicationEvent auditApplicationEvent){
        AuditEvent auditEvent = auditApplicationEvent.getAuditEvent();
        System.out.println("Principal " + auditEvent.getPrincipal()
                            + " - " + auditEvent.getType());


        WebAuthenticationDetails details =
                (WebAuthenticationDetails) auditEvent.getData().get("details");
        /*System.out.println("Remote IP address: " + details.getRemoteAddress());
        System.out.println("Session id: " + details.getSessionId());*/
        // from postman throws NullPointerException due to empty Collection of data
//        System.out.println("Request URL: "
//                + auditEvent.getData().get("requestUrl"));
    }

}
