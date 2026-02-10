## Reflection 1

### Clean Code Principles Applied
I applied several Clean Code principles in this exercise, focusing on **Meaningful Names** and **Functions that do one thing**.

* **Meaningful Names:** I ensured that variables and methods have descriptive names. For example, using `productId` instead of just `id` and `ProductService` for the interface makes the code self-documenting.
* **Single Responsibility Principle (SRP):** I separated the concerns into three layers: `Controller` (handling requests), `Service` (business logic), and `Repository` (data access). 

### Secure Coding Practices
One security improvement I applied was switching from manual or empty IDs to **UUIDs** (Universally Unique Identifiers).

* **Secure ID Generation:** Originally, the `create` function didn't assign an ID, which caused issues. By using `UUID.randomUUID()`, I ensure that every product has a unique, hard-to-guess identifier. This helps prevent Insecure Direct Object References (IDOR) because now attackers cannot easily guess the ID of other products (unlike sequential IDs like 1, 2, 3).

### Mistakes and Improvements
During the implementation of the `Edit` feature, I encountered a significant bug.

* **The "Null ID" Mistake:** When I first implemented the edit page, it returned a `404` error. This happened because newly created products had a `null` ID, so the URL generated was `/product/edit/` (missing the ID parameter).
* **The Fix:** I improved the `ProductController` by adding a check inside the `createProductPost` method: if the ID is null, a new UUID is generated. This ensures data integrity and prevents broken links in the application.