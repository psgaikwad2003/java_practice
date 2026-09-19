package DesignPatterns;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Demonstrates the Template Method Design Pattern (Behavioral Pattern).
 * 
 * Intent: Define the skeleton of an algorithm in an operation, deferring some
 * steps to subclasses. Template Method lets subclasses redefine certain steps of
 * an algorithm without changing the algorithm's overarching structure.
 * 
 * Follows the "Hollywood Principle": "Don't call us, we'll call you."
 * 
 * Included Examples:
 * 1. ETL Data Processing Pipeline: Skeleton for connecting, extracting,
 *    validating, transforming, loading, and auditing datasets (CSV vs. JSON API).
 * 2. CI/CD Build and Deployment Workflow: Skeleton for checking out code,
 *    resolving dependencies, compiling, running test suites, and deploying.
 */
public class TemplateMethodPatternDemo {

    // =========================================================================
    // Example 1: ETL (Extract, Transform, Load) Data Processing Framework
    // =========================================================================

    /**
     * Abstract Template defining the invariant ETL execution lifecycle.
     */
    public abstract static class DataProcessingPipeline {

        /**
         * The Template Method. Marked 'final' so subclasses cannot alter
         * the fundamental algorithm structure.
         */
        public final void processData(String sourceUri, String targetWarehouse) {
            System.out.printf("%n=== Starting ETL Pipeline for [%s] -> [%s] ===%n", sourceUri, targetWarehouse);
            openDataSource(sourceUri);
            List<String> rawData = extractRecords();

            List<String> validatedData;
            if (isValidationEnabled()) {
                validatedData = validateRecords(rawData);
            } else {
                System.out.println("  [Hook] Validation step bypassed per configuration.");
                validatedData = rawData;
            }

            List<String> transformed = transformRecords(validatedData);
            loadRecords(targetWarehouse, transformed);

            if (shouldGenerateAuditReport()) {
                generateAuditLog(sourceUri, transformed.size());
            }

            closeDataSource();
            System.out.println("=== ETL Pipeline Finished Successfully ===\n");
        }

        // --- Abstract Steps: Subclasses MUST implement these ---
        protected abstract void openDataSource(String uri);
        protected abstract List<String> extractRecords();
        protected abstract List<String> transformRecords(List<String> records);
        protected abstract void loadRecords(String targetWarehouse, List<String> transformedRecords);

        // --- Concrete Default / Clean-up Steps ---
        protected void closeDataSource() {
            System.out.println("  [Base] Connection to data source closed cleanly.");
        }

        // --- Hook Methods: Subclasses CAN override these to tweak behavior ---
        protected boolean isValidationEnabled() {
            return true; // default: validate records
        }

        protected List<String> validateRecords(List<String> records) {
            System.out.println("  [Base Validation] Filtering out null or empty records...");
            List<String> valid = new ArrayList<>();
            for (String r : records) {
                if (r != null && !r.trim().isEmpty()) {
                    valid.add(r.trim());
                }
            }
            System.out.printf("  [Base Validation] Kept %d valid records from %d raw inputs.%n",
                    valid.size(), records.size());
            return valid;
        }

        protected boolean shouldGenerateAuditReport() {
            return true; // default: audit on
        }

        protected void generateAuditLog(String source, int recordCount) {
            System.out.printf("  [Audit] Telemetry logged: Processed %d records from source '%s'.%n",
                    recordCount, source);
        }
    }

    /**
     * Concrete Pipeline 1: CSV File Ingestion Pipeline.
     */
    public static class CsvDataPipeline extends DataProcessingPipeline {
        private String filePath;

        @Override
        protected void openDataSource(String uri) {
            this.filePath = uri;
            System.out.printf("  [CSV] Opening disk file descriptor for '%s'...%n", filePath);
        }

        @Override
        protected List<String> extractRecords() {
            System.out.println("  [CSV] Reading line-delimited records from file stream...");
            return Arrays.asList("101,John Doe,45000", "102,Jane Smith,62000", "   ", "103,Alex Roy,58000");
        }

        @Override
        protected List<String> transformRecords(List<String> records) {
            System.out.println("  [CSV] Parsing comma-separated columns and capitalizing names...");
            List<String> formatted = new ArrayList<>();
            for (String rec : records) {
                String[] parts = rec.split(",");
                if (parts.length == 3) {
                    formatted.add(String.format("ID=%s, NAME=%s, SALARY=$%s",
                            parts[0], parts[1].toUpperCase(), parts[2]));
                }
            }
            return formatted;
        }

        @Override
        protected void loadRecords(String targetWarehouse, List<String> transformedRecords) {
            System.out.printf("  [CSV] Appending %d SQL statements to database warehouse '%s'.%n",
                    transformedRecords.size(), targetWarehouse);
            for (String item : transformedRecords) {
                System.out.printf("    -> INSERT INTO EMPLOYEES: %s%n", item);
            }
        }
    }

    /**
     * Concrete Pipeline 2: JSON REST API Ingestion Pipeline.
     */
    public static class JsonApiDataPipeline extends DataProcessingPipeline {
        private String endpointUrl;

        @Override
        protected void openDataSource(String uri) {
            this.endpointUrl = uri;
            System.out.printf("  [REST API] Establishing HTTPS session and handshaking with '%s'...%n", endpointUrl);
        }

        @Override
        protected List<String> extractRecords() {
            System.out.println("  [REST API] Fetching paginated JSON response payload...");
            return Arrays.asList(
                    "{\"order_id\": 9001, \"sku\": \"LAPTOP-X\", \"status\": \"PAID\"}",
                    "{\"order_id\": 9002, \"sku\": \"HEADPHONES\", \"status\": \"PENDING\"}",
                    "{\"order_id\": 9003, \"sku\": \"MOUSE-BT\", \"status\": \"PAID\"}"
            );
        }

        @Override
        protected List<String> transformRecords(List<String> records) {
            System.out.println("  [REST API] Unmarshaling JSON documents and generating parquet events...");
            List<String> events = new ArrayList<>();
            for (String json : records) {
                events.add("EVENT_STORE::" + json.replace("\"", "").replace("{", "").replace("}", ""));
            }
            return events;
        }

        @Override
        protected void loadRecords(String targetWarehouse, List<String> transformedRecords) {
            System.out.printf("  [REST API] Streaming %d events to Kafka cloud topic '%s'.%n",
                    transformedRecords.size(), targetWarehouse);
            for (String event : transformedRecords) {
                System.out.printf("    -> PUBLISH: %s%n", event);
            }
        }

        // Custom hook override: skip audit for lightweight API stream
        @Override
        protected boolean shouldGenerateAuditReport() {
            return false;
        }
    }

    // =========================================================================
    // Example 2: CI/CD Build and Deployment Workflow
    // =========================================================================

    public abstract static class BuildPipeline {

        /**
         * The Template Method for building and deploying software.
         */
        public final void runPipeline() {
            System.out.println("\n--- [CI/CD] Starting Automated Build Pipeline ---");
            checkoutCode();
            installDependencies();
            compile();
            runTests();

            if (isDeployApproved()) {
                packageArtifact();
                deploy();
            } else {
                System.out.println("  [CI/CD Hook] Skipping deployment: Approval gate not satisfied.");
            }
            System.out.println("--- [CI/CD] Pipeline Completed ---");
        }

        protected void checkoutCode() {
            System.out.println("  [Git] Pulling latest changes from remote branch 'main'.");
        }

        protected abstract void installDependencies();
        protected abstract void compile();
        protected abstract void runTests();
        protected abstract void packageArtifact();
        protected abstract void deploy();

        // Hook method
        protected boolean isDeployApproved() {
            return true;
        }
    }

    public static class JavaMicroservicePipeline extends BuildPipeline {
        @Override
        protected void installDependencies() {
            System.out.println("  [Maven] Resolving pom.xml dependencies and downloading JARs.");
        }

        @Override
        protected void compile() {
            System.out.println("  [Javac] Compiling Java classes with JDK 21.");
        }

        @Override
        protected void runTests() {
            System.out.println("  [JUnit 5] Executing 84 unit and integration tests: All passed (100%).");
        }

        @Override
        protected void packageArtifact() {
            System.out.println("  [Docker] Building microservice container image 'api-service:v2.1'.");
        }

        @Override
        protected void deploy() {
            System.out.println("  [Kubernetes] Rolling deployment updated across 3 pods in cluster.");
        }
    }

    // =========================================================================
    // Demonstration and Verification
    // =========================================================================

    public static void main(String[] args) {
        System.out.println("=========================================================");
        System.out.println("       TEMPLATE METHOD DESIGN PATTERN DEMONSTRATION       ");
        System.out.println("=========================================================");

        // --- Demo 1: ETL Pipeline Ingestion ---
        DataProcessingPipeline csvPipeline = new CsvDataPipeline();
        csvPipeline.processData("/var/data/employees.csv", "Enterprise_DB.Employees");

        DataProcessingPipeline apiPipeline = new JsonApiDataPipeline();
        apiPipeline.processData("https://api.store.internal/v1/orders", "Kafka_Order_Topic");

        // --- Demo 2: CI/CD Build Workflow ---
        BuildPipeline javaPipeline = new JavaMicroservicePipeline();
        javaPipeline.runPipeline();

        System.out.println("\n[TemplateMethodPatternDemo] Execution finished successfully!");
    }
}
