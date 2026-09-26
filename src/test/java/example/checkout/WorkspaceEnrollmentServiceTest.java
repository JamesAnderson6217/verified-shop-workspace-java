package example.checkout;

import java.util.ArrayList;
import java.util.List;

public final class WorkspaceEnrollmentServiceTest {
    public static void main(String[] args) {
        List<String> calls = new ArrayList<>();
        InfraiHttpClient client = new InfraiHttpClient((method, path, body) -> {
            calls.add(method + " " + path + " " + body);
            if (path.startsWith("/v1/dns/domain/get")) return new InfraiHttpClient.Reply(200, "{\"ok\":true,\"data\":{\"zone_id\":\"zone-7\"}}", null);
            return new InfraiHttpClient.Reply(200, "{\"ok\":true,\"data\":{}}", null);
        });
        WorkspaceEnrollmentService service = new WorkspaceEnrollmentService(client);
        WorkspaceEnrollmentService.EnrollmentResult result = service.enroll(new WorkspaceEnrollmentService.EnrollmentRequest("school.example", "teacher@school.example", "Teacher", "checkout-1", "fulfillment-1", "receipt-1", "Order update sent"));
        if (!result.joined() || !calls.equals(List.of(
                "GET /v1/dns/domain/get?domain=school.example ",
                "POST /v1/dns/domain/verify {\"domain\":\"school.example\"}",
                "GET /v1/auth/user/get_by_email?email=teacher%40school.example "))) throw new AssertionError("Enrollment must verify the domain and look up the employee without creating a DNS record.");
        System.out.println("PASS: verified company employee joins the workspace.");
    }
}
