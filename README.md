# Module 1

## Reflection 1

### Clean Code Principles Applied
I applied several Clean Code principles in this exercise, focusing on **Meaningful Names** and **Functions that do one thing**.
* **Meaningful Names:** I ensured that variables and methods have descriptive names. For example, using `productId` instead of just `id` and `ProductService` for the interface makes the code self-documenting.
* **Single Responsibility Principle (SRP):** I separated the concerns into three layers: `Controller` (handling requests), `Service` (business logic), and `Repository` (data access). This ensures that each class has only one reason to change.

### Secure Coding Practices
One security improvement I applied was switching from manual or empty IDs to **UUIDs (Universally Unique Identifiers)**.
* **Secure ID Generation:** Originally, the `create` function didn't assign an ID, which caused issues. By using `UUID.randomUUID()`, I ensure that every product has a unique, hard-to-guess identifier. This helps prevent Insecure Direct Object References (IDOR) because now attackers cannot easily guess the ID of other products (unlike sequential IDs like 1, 2, 3).

### Mistakes and Improvements
During the implementation of the `Edit` feature, I encountered a significant bug.
* **The "Null ID" Mistake:** When I first implemented the edit page, it returned a `404` error. This happened because newly created products had a `null` ID, so the URL generated was `/product/edit/` (missing the ID parameter).
* **The Fix:** I improved the `ProductController` by adding a check inside the `createProductPost` method: if the ID is null, a new UUID is generated. This ensures data integrity and prevents broken links in the application.


## Reflection 2

### Unit Testing and Code Coverage
* **Feeling on Unit Tests:** While writing tests for simple getters and setters felt repetitive, testing the `edit` and `delete` logic was genuinely useful. It allowed me to verify that my repository handled edge cases (like editing or deleting a non-existent product) correctly without needing to launch the application and manually test every scenario.
* **Number of Tests:** There is no fixed number of unit tests can be made for one class. The number of tests depends heavily on the complexity of the methods itself. 
* **Verifying Sufficiency:** To make sure the unit tests made are enough, we can use code coverage tools to calculate the percentage of code lines executed during testing. However, we must also manually review the tests to ensure we have covered both positive scenarios (happy path) and negative scenarios (error handling).
* **Code Coverage Limitation:** Having 100% code coverage does not guarantee that the code is bug-free. Coverage only measures which lines of code were executed during tests. It does not verify that the logic is correct for every possible input value. For example, a method might handle positive integers correctly (checking the box for coverage) but fail on negative numbers or null values if those specific cases aren't in the test suite.

### Code Quality Analysis of the New Functional Test Suite
If I were to create a new functional test suite to verify the number of items in the product list using the same setup procedures and instance variables as the previous test suites, it would lead to a decline in code quality.
* **Potential Issue:** The primary issue here is Code Duplication. By creating a new class and copying the setup logic (instance variables for `port`, `baseUrl`, and the `@BeforeEach` setup method), I would be violating the **DRY (Don't Repeat Yourself)** principle. The setup code is essentially "boilerplate" code that does not differ between test suites.
* **Why it matters:** If the setup logic changes in the future (e.g., we switch from Chrome to Firefox, or the way the port is retrieved changes), I would have to manually update every single test class and the test class becomes cluttered with setup details that are not relevant to the specific test case (counting items), making it harder to focus on the actual testing logic.
* **Suggested Improvements:** I would create a base class (e.g., `BaseFunctionalTest`) that handles the common setup procedures (Driver initialization, `baseUrl` configuration) which then both the `CreateProductFunctionalTest` and the new `ProductListFunctionalTest` would extend this base class.
* **Result:** The specific test classes would only contain the logic relevant to their specific tasks (creating a product or counting items), keeping them clean, focused, and easy to maintain.
---

# Module 2

## Deployment Link
[Eshop-env.eba-ub3ivqhc.us-east-1.elasticbeanstalk.com](Eshop-env.eba-ub3ivqhc.us-east-1.elasticbeanstalk.com ) 

## Reflection
During the exercise, I identified and fixed several high and medium-severity issues detected by the OSSF Scorecard and GitHub Code Scanning.

**Token-Permissions (High Severity)**
- Issue: The GITHUB_TOKEN in the CI workflow was granted broad write permissions by default, which posed a security risk if the repository were compromised.
- Strategy: I modified the .github/workflows/ci.yml file to include a top-level permissions block explicitly set to contents: read. This follows the principle of least privilege by restricting the token to read-only access.

**Pinned-Dependencies (Medium Severity)**
- Issue: Some dependency like actions/checkout, actions/setup-java, and codeql-action/upload-sarif were using version tags (e.g., @v4) instead of specific commit hashes. Tags can be moved, potentially introducing unverified code into the pipeline.
- Strategy: I replaced the version tags with full-length SHA commit hashes. This ensures the exact same code is executed every time, protecting the build from "tag jumping" or supply-chain attacks.
- Problem Encountered: While I successfully pinned the checkout and upload-sarif actions, attempting to pin setup-java to a specific SHA resulted in a 404 error during the workflow execution. This error indicated that GitHub could not find the specific tarball for the hash provided.
- Final: To ensure the CI/CD pipeline remained functional and the application could successfully build and deploy to Render, I chose to keep the @v4 tag for setup-java while maintaining the SHA pins for other actions.

**SonarCloud Coverage Reporting (0.0% Coverage)**
- Issue: The initial analysis showed 0% coverage because the JaCoCo XML reports were not being generated or found.
- Strategy: I updated the build.gradle.kts to explicitly enable XML report generation in the jacocoTestReport task and configured the sonarqube block to point to the correct file path.