# starling-task

## Brief
Starling Tech Task Solution

## Details
| Item      | Value          |
|-----------|----------------|
| Developer | Imran Chaudhri |

## Task
For a customer, take all the transactions in a given week and round them up to the nearest
pound. For example with spending of £4.35, £5.20 and £0.87, the round-up would be £1.58.
This amount should then be transferred into a savings goal, helping the customer save for
future adventures.

## Pre-requisites
- A valid Starling Bearer Token must be configured in `starling.bearerToken` in `application.yml` or other means i.e. command line arg etc
- A pre-configured account on Starling's developer sandbox
- A set of test data already available to work with

### Assumptions
- Transaction Feed Constraints
  - User provides start and optional end date in UTC
  - Optional end date can be set to any valid UTC time
  - If no end date is provided defaults to 7 days
- Rounding up only considers
  - 'Spending' transactions i.e. *OUTgoing*
  - Currencies that match the currency of the Account's 'account' balance
- No checks on existing savings goal with same name necessary

## How to run
### Start Spring Boot Application
```sh
    $ mvn spring-boot:run
```

### Use Postman
- Ensure you have your bearer token set up in authorisations
#### Headers
| Header Key | Value            |
|------------|------------------|
| Accept     | application/json |

#### Payload
```json
{
    "savingsGoalName": "Test Goal",
    "minTransactionTimestamp": "2022-02-05T00:00:00.000Z"
}
```
or
```json
{
    "savingsGoalName": "Test Goal",
    "minTransactionTimestamp": "2022-02-05T00:00:00.000Z",
    "maxTransactionTimestamp": "2022-02-12T00:00:00.000Z"
}
```

## Implementation Details
- Java 11
- Spring Boot