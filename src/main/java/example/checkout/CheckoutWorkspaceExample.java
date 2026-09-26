package example.checkout;

public final class CheckoutWorkspaceExample {
    public static void main(String[] args) {
        WorkspaceEnrollmentService service = new WorkspaceEnrollmentService(new InfraiHttpClient(InfraiConfig.fromEnvironment()));
        WorkspaceEnrollmentService.EnrollmentResult result = service.enroll(new WorkspaceEnrollmentService.EnrollmentRequest(
                "changba.com", "chenhua@changba.com", "Chenhua", "checkout-482", "fulfillment-91", "receipt-17", "Order update: colleague workspace access granted."));
        System.out.println("Joined " + result.workspace() + ". " + result.orderUpdate());
    }
}
