# USE CASE: 3 Produce a Report on the Top N number of cities in an area

## CHARACTERISTIC INFORMATION

### Goal in Context

As an analyst I want to be able to find the top N cities in an area so that I can develop a better understand of the growth of areas

### Scope

Company.

### Level

Primary task.

### Preconditions

We know the cities. Database already contains all data needed.

### Success End Condition

A report is available for Analyst to provide information on the size of N cities.

### Failed End Condition

No report is produced.

### Primary Actor

Data Analyst

### Trigger

A request for the top N cities in an area made.

## MAIN SUCCESS SCENARIO

1. Analyst requests report of N cities
2. Analyst extracts information from report

## EXTENSIONS

3. **N does not exist**:
    1. N < 0 so cannot be accessed

## SUB-VARIATIONS

None.

## SCHEDULE

**DUE DATE**: Release 1.0