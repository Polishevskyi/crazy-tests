# Test Framework Results

## Overview

This project implements comprehensive test frameworks for three shell scripts, covering both functional and non-functional requirements using a TDD (Test-Driven Development) approach.

## Scripts Tested

### 1. check_active_users.sh
**Purpose**: Filters active users based on login activity and ban status
- **Framework**: Built from scratch
- **Total Tests**: 18
- **Status**: ✅ All tests passing

#### Test Categories:
- **Functional Tests**: 8 tests
  - Basic active user filtering
  - Banned user exclusion
  - Date-based filtering (30-day window)
  - Edge cases (empty files, missing data)
  - Output format validation

- **Non-functional Tests**: 7 tests
  - Performance (small: ≤1s, medium: ≤3s, large: ≤10s datasets)
  - Memory usage (≤50MB)
  - Scalability testing
  - Consistency across multiple runs

- **Error Handling Tests**: 3 tests
  - Missing input files
  - Invalid file formats
  - Graceful error reporting

### 2. calculate_averages.sh
**Purpose**: Calculates average spending per user from transaction data
- **Framework**: Extended existing framework with non-functional capabilities
- **Total Tests**: 11
- **Status**: ✅ All tests passing

#### Test Categories:
- **Functional Tests**: 3 tests (existing)
  - Basic average calculation
  - Invalid data handling
  - File not found scenarios

- **Non-functional Tests**: 8 tests (newly added)
  - Performance (small: ≤1s, medium: ≤2s, large: ≤5s datasets)
  - Memory usage (≤30MB)
  - Scalability validation
  - Execution consistency
  - Large dataset handling (5000+ records)

### 3. merge_users.sh
**Purpose**: Merges user data from multiple sources (TXT, JSON, CSV)
- **Framework**: Built from scratch
- **Total Tests**: 17
- **Status**: ✅ All tests passing

#### Test Categories:
- **Functional Tests**: 6 tests
  - Basic user data merging
  - Missing data handling (name/email)
  - Duplicate user processing
  - Output format validation
  - Data completeness verification

- **Non-functional Tests**: 8 tests
  - Performance (small: ≤1s, medium: ≤2s, large: ≤10s datasets)
  - Memory usage (≤40MB)
  - Scalability testing
  - Execution consistency
  - Large dataset processing (2000+ users)

- **Error Handling Tests**: 3 tests
  - Missing input files
  - Invalid JSON/CSV formats
  - Error message validation

## Framework Components

Each test framework consists of:

### Core Classes:
1. **DataGenerator**: Creates test data files with configurable scenarios
2. **ScriptRunner**: Executes scripts with performance monitoring
3. **ResultAnalyser**: Validates output files and data integrity
4. **TestScenario**: Fluent API for writing readable tests

### Features:
- **Performance Monitoring**: Execution time, memory usage, CPU time tracking
- **Data Validation**: CSV format, data completeness, duplicate detection
- **Error Handling**: Comprehensive error scenario testing
- **Scalability Testing**: Linear performance validation
- **Test Data Management**: Automatic cleanup and isolation

## Performance Benchmarks

| Script | Small Dataset | Medium Dataset | Large Dataset | Memory Limit |
|--------|---------------|----------------|---------------|--------------|
| check_active_users.sh | ≤1000ms (10 users) | ≤3000ms (100 users) | ≤10000ms (1000 users) | ≤50MB |
| calculate_averages.sh | ≤1000ms (10 records) | ≤2000ms (100 records) | ≤5000ms (1000 records) | ≤30MB |
| merge_users.sh | ≤1000ms (10 users) | ≤2000ms (100 users) | ≤10000ms (1000 users) | ≤40MB |

## Test Execution Results

### Summary Statistics:
- **Total Tests**: 46
- **Passed**: 46 (100%)
- **Failed**: 0 (0%)
- **Coverage**: Functional + Non-functional + Error Handling

### Test Categories Distribution:
- **Functional Tests**: 17 tests (37%)
- **Non-functional Tests**: 23 tests (50%)
- **Error Handling Tests**: 6 tests (13%)

## Architecture

### Project Structure:
```
crazy-tests/
├── check_active_users/           # Framework from scratch
│   ├── src/main/java/helpers/    # DataGenerator, ScriptRunner, ResultAnalyser
│   ├── src/test/java/            # CheckActiveUsersTest
│   ├── test_design/              # Functional & non-functional test cases
│   └── check_active_users.sh     # Target script
├── calculate_average_app/         # Extended existing framework
│   ├── src/main/java/helpers/    # Enhanced with performance monitoring
│   ├── src/test/java/            # CalculateAverageTest (extended)
│   ├── test_design/              # Added non-functional test cases
│   └── script/calculate_averages.sh
├── merge_users/                   # Framework from scratch
│   ├── src/main/java/helpers/    # Complete framework implementation
│   ├── src/test/java/            # MergeUsersTest
│   ├── test_design/              # Comprehensive test design
│   └── merge_users.sh            # Target script
└── pom.xml                       # Maven configuration
```

### Technology Stack:
- **Testing Framework**: JUnit 5
- **Build Tool**: Maven
- **Data Processing**: Jackson (JSON), CSV parsing
- **Performance Monitoring**: JVM MXBeans
- **Scripting**: Bash with cross-platform compatibility

## Key Achievements

1. **100% Test Success Rate**: All 46 tests pass consistently
2. **Comprehensive Coverage**: Functional, non-functional, and error scenarios
3. **Performance Validation**: All scripts meet defined performance benchmarks
4. **Scalability Proven**: Linear performance scaling validated
5. **Error Resilience**: Robust error handling and reporting
6. **Code Quality**: Clean, maintainable test frameworks with fluent APIs
7. **Documentation**: Complete test design documentation for all scenarios

## Execution Instructions

### Prerequisites:
- Java 17+
- Maven 3.6+
- Bash 4+ (for script execution)
- jq (for JSON processing)

### Running Tests:

#### All tests:
```bash
# From project root
mvn test

# Individual modules
cd check_active_users && mvn test
cd calculate_average_app && mvn test  
cd merge_users && mvn test
```

#### Specific test categories:
```bash
# Non-functional tests only
mvn test -Dtest="*NonFunctionalTests"

# Error handling tests only  
mvn test -Dtest="*ErrorHandlingTests"
```

## Conclusion

The implemented test frameworks successfully provide comprehensive coverage for all three shell scripts, ensuring both functional correctness and non-functional performance requirements are met. The TDD approach has resulted in robust, maintainable, and scalable test suites that can serve as a foundation for future script testing needs.