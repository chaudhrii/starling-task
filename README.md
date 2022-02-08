# starling-task

## Brief
Starling Tech Task Solution

## Task
For a customer, take all the transactions in a given week and round them up to the nearest
pound. For example with spending of £4.35, £5.20 and £0.87, the round-up would be £1.58.
This amount should then be transferred into a savings goal, helping the customer save for
future adventures.

### Assumptions
- Week will be provided as a constraint
  - User provides start and end date in Zulu
  - Defaults to 7 days
    - Externalised
- Customer Account Holder UID is provided as a param

## Implementation Details
- Java 11
- Spring Boot


## How to run
```sh
    $ mvn spring-boot:run
```
