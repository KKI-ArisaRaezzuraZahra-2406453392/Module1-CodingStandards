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

### Code Quality Issue(s) and Strategy to Fix it
During the exercise, I identified and fixed several high and medium-severity issues detected by the OSSF Scorecard and GitHub Code Scanning.

**Token-Permissions (High Severity)**
- **Issue**: The GITHUB_TOKEN in the CI workflow was granted broad write permissions by default, which posed a security risk if the repository were compromised.
- **Strategy**: I modified the .github/workflows/ci.yml file to include a top-level permissions block explicitly set to contents: read. This follows the principle of least privilege by restricting the token to read-only access.

**Pinned-Dependencies (Medium Severity)**
- **Issue**: Some dependency like actions/checkout, actions/setup-java, and codeql-action/upload-sarif were using version tags (e.g., @v4) instead of specific commit hashes. Tags can be moved, potentially introducing unverified code into the pipeline.
- **Strategy**: I replaced the version tags with full-length SHA commit hashes. This ensures the exact same code is executed every time, protecting the build from "tag jumping" or supply-chain attacks.
- **Problem Encountered**: While I successfully pinned the checkout and upload-sarif actions, attempting to pin setup-java to a specific SHA resulted in a 404 error during the workflow execution. This error indicated that GitHub could not find the specific tarball for the hash provided.
- **Final**: To ensure the CI/CD pipeline remained functional and the application could successfully build and deploy to Render, I chose to keep the @v4 tag for setup-java while maintaining the SHA pins for other actions.

**SonarCloud Coverage Reporting (0.0% Coverage)**
- **Issue**: The initial analysis showed 0% coverage because the JaCoCo XML reports were not being generated or found.
- **Strategy**: I updated the build.gradle.kts to explicitly enable XML report generation in the jacocoTestReport task and configured the SonarCloud block to point to the correct file path.

### CI/CD Implementation
The current implementation successfully meets the definitions of Continuous Integration (CI) and Continuous Deployment (CD).

- First, the CI aspect is fulfilled because every push or pull request triggers an automated workflow that compiles the Java code, runs unit tests, and performs static analysis via SonarCloud to ensure code quality before merging. 
- Second, the CD aspect is met through the integration with AWS Elastic Beanstalk, where the pipeline automatically packages the application into a JAR file and deploys it to the AWS environment whenever changes are pushed to the master branch.
- Finally, this setup ensures a reliable release cycle because it maintains a high standard of quality through automated gates while providing a fully automated, hands-off path from a code commit to a live, publicly accessible URL on AWS.

---

# Module 3

## Explain what principles you apply to your project!
In this project, I applied several SOLID principles to improve the maintainability and structure of the code:

- **Single Responsibility Principle (SRP)**: I separated the CarController from the ProductController. Previously, ProductController was handling two different things (Products and Cars), by moving CarController into its own file, each class now has only one reason to change.
- **Open-Closed Principle (OCP)**: I utilized the Service-Interface pattern by having CarController depend on the CarService interface. This makes the system open to new service implementations without needing to modify the existing controller code.
- **Liskov Substitution Principle (LSP)**: I removed the inheritance where CarController extended ProductController. The original inheritance was inappropriate because a CarController is not a subtype of ProductController in a way that allows them to be substituted interchangeably without causing pathing issues and logic errors.
- **Interface Segregation Principle (ISP)**: My project uses separate, specific interfaces for CarService and ProductService. This ensures that CarServiceImpl is only forced to implement methods it actually needs rather than being bloated with unrelated product logic.
- **Dependency Inversion Principle (DIP)**: I ensured that the controllers depend on the CarService and ProductService interfaces rather than their concrete implementations (like CarServiceImpl). By autowiring the interface, the high-level web layer is decoupled from the specific low-level business logic implementation. 

Note: DIP and ISP were largely structurally present during the debugging phase of the before-solid branch.

## Explain the advantages of applying SOLID principles to your project with examples.
- **Easier Debugging and Navigation (SRP)**: When I encountered the 404 error during the tutorial, having the Car logic isolated in its own CarController made it much easier identifying issues with specific routes like /car/listCar because the code isn't buried within unrelated product logic
- **System Extensibility (OCP)**: Because the controller uses the CarService interface, if needed, I can later create a new version of the service and swap it in via Spring's dependency injection without touching the CarController code at all.
- **Reduced Side Effects (LSP)**: By removing the inheritance between controllers, I eliminated the shadowed request mappings. This ensured that changes to the Product URLs would never accidentally overwrite or break the Car URLs.
- **Leaner Implementations (ISP)**: Because the services are segregated, CarServiceImpl only needs to implement methods relevant to cars. If I need to add a car-specific feature like checkEngineStatus(), I can do so without polluting the Product logic.
- **Flexibility (DIP)**: Because CarController only knows about the CarService interface, I can easily swap out the current CarServiceImpl for a different implementation (like a database-backed service or a mock service for testing) without changing any code in the controller.

## Explain the disadvantages of not applying SOLID principles to your project with examples.
- **Rigid and Fragile Code (Violation of SRP)**: If I had kept CarController inside ProductController, the file would eventually become a massive file that is hard to read. A small change to the Car feature could accidentally break the Product feature because they share the same class scope.
- **High Maintenance Costs (Violation of OCP)**: Without OCP, adding a new type of service would require modifying the CarController to use if-else blocks to decide which service to call, increasing the risk of introducing new bugs into working code.
- **Logical Confusion (Violation of LSP)**: Keeping the inheritance between the two controllers caused significant pathing confusion. This is a prime example of how improper inheritance makes the program behavior unpredictable and difficult to extend.
- **Tight Coupling (Violation of ISP & DIP)**: Currently, while the Controller is decoupled from the Service, the CarServiceImpl still depends directly on the concrete CarRepository class. If I were to switch from an ArrayList to a SQL database, I would be forced to modify the CarServiceImpl code directly. This shows that while the code is improved, remaining tightly coupled at the repository level still limits full modularity.

---

# Module 4

## Tutorial Reflection 

### Reflect whether this TDD flow is useful enough for you or not. 
Applying the self-reflective questions by Percival (2017), I find the flow taught and used in the tutorial useful mainly for a few reasons such as:
- **Correctness**: The [RED] phase helped me think and focus on both happy and unhappy paths, making me prepare for edge cases before writing the production code.
- **Maintainability**: The [REFACTOR] phase moved the hardcoded strings to an enum, this allows me to modify the enum value without fear of breaking the app. The tests also help making sure the behavior remained identical and doesn't affect the functionality.
- **Productive Workflow**: While preparing for the test might need extra planning and time, the immediate feedback it gave made building the production code easier and reduce the time spend on manual debugging.

### Reflect whether your tests have successfully followed F.I.R.S.T. principle or not.
I believe that my tests have followed the F.I.R.S.T. principle due to multiple reason:

- **Fast**: Since the tests cover the unit-level, it doesn't need external database or other system to run. This allows the tests to give feedback almost instantaneously after being run.
- **Isolated/Independent**: Using the `@BeforeEach` before every test make sure that the ArrayList of products are fresh for every test case. This ensures the state of each test doesn't interfere with the other.
- **Repeatable**: Since every test use deterministic data and are isolated from each other, it'll produce similar results every time it's executed regardless of the environment.
- **Self-Validating**: The tests use clear assertion with specific expected values, so there'll be no need to manual check since the test already show a pass or fail clearly.
- **Timely/Thorough**: The tests were written before the production code and build it as more features are being introduced. It covers both the happy and unhappy path, taking account of the possible edge cases.

