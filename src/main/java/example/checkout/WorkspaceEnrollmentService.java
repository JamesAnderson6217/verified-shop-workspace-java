package example.checkout;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public final class WorkspaceEnrollmentService {
    public record EnrollmentRequest(String companyDomain, String employeeEmail, String employeeName, String checkoutId, String fulfillmentId, String receiptId, String customerOrderUpdate) { }
    public record EnrollmentResult(boolean joined, String workspace, String orderUpdate) { }
    private final InfraiHttpClient infrai;

    public WorkspaceEnrollmentService(InfraiHttpClient infrai) { this.infrai = infrai; }

    public EnrollmentResult enroll(EnrollmentRequest request) {
        String domain = request.companyDomain();
        infrai.call("GET", "/v1/dns/domain/get?domain=" + query(domain), "");
        infrai.call("POST", "/v1/dns/domain/verify", "{\"domain\":\"" + domain + "\"}");
        infrai.call("GET", "/v1/auth/user/get_by_email?email=" + query(request.employeeEmail()), "");
        return new EnrollmentResult(true, domain, request.customerOrderUpdate());
    }
    private static String query(String value) { return URLEncoder.encode(value, StandardCharsets.UTF_8); }
}
