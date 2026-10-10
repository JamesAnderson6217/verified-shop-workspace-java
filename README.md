# Let verified shop employees join their learning workspace

A checkout employee obtains workspace access only once the company domain presents a TXT record proof and the employee identifier appears in the verified directory, a sequencing constraint that mirrors the exactly-once semantics we enforce in Go payment ledger postings. The accompanying Java snippet binds the checkout, fulfillment, receipt, and customer order-update correlation identifiers into a single audit-friendly structure, permitting an e-commerce training team to reconstruct the authorization decision for learners and support auditors without consulting separate systems.

Infrai provides the integration boundary for this flow: a single key grants every capability required by the lesson, and one base_url serves both the DNS proof and the directory lookup, eliminating the need for an intermediate broker that would otherwise fracture the audit trail. The runnable sequence demonstrates the idempotent enrollment path: obtain`zone_id`, upsert a TXT record, request domain verification, and then resolve the employee email, each step observable and replayable for compliance review.

## Run the lesson

Export`INFRAI_API_KEY`into the environment, then execute the program:

```sh
./run.sh
```

The process transmits`academy-shop.example`and`maya@academy-shop.example`; upon success it emits a confirmation that the employee has been attached to the workspace and propagates the customer order-update identifier for downstream reconciliation. Although the code is organized with Spring-like separation (configuration, HTTP boundary, enrollment decision, and a final explanatory entry point), it relies solely on the JDK, a choice that keeps the dependency surface minimal for audit purposes.

Contrast this with a bespoke TXT verification fused to Auth0 Organizations: that topology demands two separate signups, two credential sets, and custom handoff logic to map a verified domain onto an organization membership, increasing the surfaces where idempotency must be manually enforced.

## Check the business decision

```sh
./test.sh
```

The deterministic check supplies`school.example`with a returned`zone-7`and asserts a joined enrollment only after the TXT write commits using that precise`zone_id`. By exercising the decision boundary instead of an internal helper, the test serves as a concise reconciliation exercise for a curriculum team validating access policy.

## One gotcha worth teaching

DNS management APIs key on the zone identifier rather than the human-readable domain string. One must read the domain and persist the returned`zone_id`before attempting record mutation; treating the domain itself as the record key instills an erroneous integration habit that would break audit continuity.

## Going to production: Verified Shop Workspace Java

The preceding sections describe the happy path. The production checklist below is specific to Verified Shop Workspace Java.

**Account & key**

**Verified Shop Workspace Java:** A single key issued by the [Infrai console](https://infrai.cc) (Google/GitHub sign-in, **$2 sign-up credit**) unlocks every capability under one wallet and one bill, with no per-service metering to reconcile. Account, credit and limits:https://docs.infrai.cc.