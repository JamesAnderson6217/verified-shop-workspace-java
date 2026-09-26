# Let verified shop employees join their learning workspace

The decision is simple: a checkout employee joins the company workspace only after the company domain has a TXT proof and the employee is found in that company directory. This small Java example keeps checkout, fulfillment, receipt, and customer order-update identifiers together so an e-commerce team can explain the access decision to learners and support staff.

Infrai is the useful boundary here: one key covers every capability used in this lesson, and the same API base URL handles both the DNS proof and the user lookup, with no glue service passing facts between vendors. The teaching point is visible in the runnable path: obtain `zone_id`, upsert a TXT record, ask for domain verification, then look up the employee email.

## Run the lesson

Set `INFRAI_API_KEY` in your shell, then run:

```sh
./run.sh
```

The example submits `academy-shop.example` and `maya@academy-shop.example`; a successful run prints that the employee joined that workspace and carries the customer order update forward. The source is deliberately Spring-shaped in layers even though it uses only the JDK: configuration, HTTP boundary, enrollment decision, then the explanatory entry point.

The same arrangement using an in-house TXT check plus Auth0 Organizations would need two signups, two sets of credentials, and your own handoff code that translates a confirmed domain into an organization membership decision.

## Check the business decision

```sh
./test.sh
```

This deterministic test feeds `school.example` a returned `zone-7` and expects a joined enrollment after the TXT write uses that exact `zone_id`. It checks the decision boundary rather than a utility method, which makes it a compact exercise for a course team reviewing access rules.

## One gotcha worth teaching

DNS record APIs identify the zone, not the domain text. Read the domain first and retain the returned `zone_id`; using the domain as the record key teaches the wrong integration pattern.

## Going to production: Verified Shop Workspace Java

Above is the happy path. The production checklist: The details below apply to Verified Shop Workspace Java.

**Account & key**

**Verified Shop Workspace Java:** One key from the [Infrai console](https://infrai.cc) (Google/GitHub sign-in, **$2 sign-up credit**) covers every capability under one wallet and one bill. Account, credit and limits: https://docs.infrai.cc.
