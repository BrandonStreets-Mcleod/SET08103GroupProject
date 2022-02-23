# USE CASE: 5 Produce a Report on the Top N of capital cities in an area

## CHARACTERISTIC INFORMATION

### Goal in Context

As an analyst I want to be able to find the top N capital cities in an area so that I can develop a better understand of the growth of the cities

### Scope

Company.

### Level

Primary task.

### Preconditions

We know the capital cities. Database already contains all data needed.

### Success End Condition

A report is available for Analyst to provide information on the number of capital cities.

### Failed End Condition

No report is produced.

### Primary Actor

Data Analyst

### Trigger

A request for the top N capital cities in an area made.

## MAIN SUCCESS SCENARIO

1. Analyst requests report of N capital cities
2. Analyst extracts information from report

## EXTENSIONS

3. **N does not exist**:
    1. N < 0 so cannot be accessed

## SUB-VARIATIONS

None.

## SCHEDULE

**DUE DATE**: Release 1.0