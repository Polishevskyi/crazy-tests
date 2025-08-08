# Non-functional Test Cases for check_active_users.sh

## Performance Criteria

### TC-NF-001: Execution time with small dataset

**Description**: Script should process small datasets quickly  
**Data**: 10 users, 10 login records, 2 banned  
**Expected**: Execution <= 1000ms  
**Priority**: High

### TC-NF-002: Execution time with medium dataset

**Description**: Script should efficiently process medium datasets  
**Data**: 100 users, 100 login records, 10 banned  
**Expected**: Execution <= 3000ms  
**Priority**: High

### TC-NF-003: Execution time with large dataset

**Description**: Script should handle large datasets  
**Data**: 1000 users, 1000 login records, 100 banned  
**Expected**: Execution <= 10000ms  
**Priority**: Medium

### TC-NF-004: Memory consumption

**Description**: Script should not consume excessive memory  
**Data**: 1000 users, 1000 login records  
**Expected**: Memory consumption <= 50MB  
**Priority**: Medium

### TC-NF-005: Combined performance test

**Description**: Script should meet both time and memory requirements simultaneously  
**Data**: 1000 users, 1000 login records  
**Expected**: Execution <= 10000ms AND Memory <= 50MB  
**Priority**: High

## Scalability

### TC-NF-006: Execution consistency

**Description**: Multiple runs with same data should produce consistent execution times  
**Data**: Fixed set of users and logins  
**Expected**: Execution time variance < 2000ms between runs  
**Priority**: Medium

### TC-NF-007: Scalability validation

**Description**: Execution time should scale reasonably with data size  
**Data**: 10 vs 100 users  
**Expected**: Scaling factor <= 50x  
**Priority**: Medium

### TC-NF-008: Very large dataset handling

**Description**: Script should complete successfully with very large datasets  
**Data**: 5000 users, 5000 login records  
**Expected**: Execution <= 30000ms, successful completion  
**Priority**: Low

## Resource Usage

### TC-NF-009: CPU utilization

**Description**: Script should not consume excessive CPU resources  
**Data**: 1000 users  
**Expected**: CPU time <= 10000ms  
**Priority**: Low

### TC-NF-010: File system operations

**Description**: Script should handle file I/O efficiently  
**Data**: Various file sizes  
**Expected**: No file system errors, proper cleanup  
**Priority**: Medium

## Reliability

### TC-NF-011: Stress test

**Description**: Script should handle repeated executions without degradation  
**Data**: 10 consecutive runs with 1000 users each  
**Expected**: All runs complete successfully within time limits  
**Priority**: Low

### TC-NF-012: Date processing performance

**Description**: Date calculations should not be a performance bottleneck  
**Data**: 1000 login records with various dates  
**Expected**: Date processing completes within overall time limit  
**Priority**: Medium

## Environment Compatibility

### TC-NF-013: Cross-platform performance

**Description**: Performance should be consistent across different systems  
**Data**: Same dataset on different OS (Linux/macOS)  
**Expected**: Performance within 30% variance  
**Priority**: Low

### TC-NF-014: JSON processing efficiency

**Description**: JSON parsing should be efficient for large ban lists  
**Data**: banned.json with 1000+ entries  
**Expected**: JSON processing <= 1000ms  
**Priority**: Medium

### TC-NF-015: Resource cleanup

**Description**: Script should properly clean up temporary resources  
**Data**: Any dataset  
**Expected**: No temporary files left, memory released  
**Priority**: Medium