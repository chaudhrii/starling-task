# starling-task

## Brief
Starling Bank tech challenge. To develop a "round-up" feature for Starling Customers using the public API available to customers and partners. The application is can be referred to as the 'Round-Up' application in this document.

## Details
| Item      | Value                        |
| --------- | ---------------------------- |
| Developer | Imran Chaudhri               |
| Email     | i_chaudhri@yahoo.co.uk       |
| Phone     | 07714262336                  |
| GitHub    | https://github.com/chaudhrii |
| Task      | Ready to submit              |

## Task
For a customer, take all the transactions in a given week and round them up to the nearest pound. For example with pending of £4.35, £5.20 and £0.87, the round-up would be £1.58.

This amount should then be transferred into a savings goal, helping the customer save for future adventures.

## Pre-requisites
To get started with the Round-Up application there are some pre-requisites that must be performed on Starling's Sandbox environment.

- [Starling Bank Sandbox](https://developer.starlingbank.com/sandbox/)
- A customer
  - With an Account Holder Uid `accountUid`
  - A GBP account
    - Currently not tested against other currencies
- An Access/Bearer  Token

- Some 'Auto Simulated' data
  - Note the date these transactions span as it will be used when specifying a 'from time' for the Round-Up Savings Goal creation

## Assumptions
- Only dealing with GBP currency as input data
  - Application itself takes the currency from the provided account's balance, so theoretically it can handle other currencies. It is just not tested against as all test data had been produced in GBP.

- Transaction Feed Constraints
  - The 'to time' `maxTransactionTimestamp` is optional and if omitted defaults to 7 days from the `minTransactionTimestamp`
    - This default can be affected with the `starling.defaultRoundUpPeriod` in the in `application.yml` file

- Rounding up only considers
  - **Spending** transactions i.e. *OUTgoing*
- No checks on existing savings goal with same name required

## Tech

Implemented using the following tech

- Java 11 
- Spring Boot as a RESTful application
- Lombok
- Junit5
- Mockito

## Configuration

### Application Config

Configuration is possible via properties in the `application.yml` file. There are two files by this name, one for the main applicationa and one for testing.

- `src\main\java\resources`
- `src\test\java\resources`

#### Properties

| Property                        | Description                                                  | Example                              |
| ------------------------------- | ------------------------------------------------------------ | ------------------------------------ |
| `starling.defaultRoundUpPeriod` | Default period of transactions considered for Round-Up task if no end time is specified. Described in whole **days**. | 7                                    |
| `starling.starlingBaseUrl`      | The URL to Starling's Sandbox Environment                    | https://api-sandbox.starlingbank.com |
| `starling.bearerToken`          | The Access Token by the Sandbox Environment                  | eyJhbGciOiJQUzI1N....N7VDejsu0       |

#### Test Properties

| Property                    | Description                                                  | Example                              |
| --------------------------- | ------------------------------------------------------------ | ------------------------------------ |
| `logging.level.root`        | Used to affect the logging threshold                         | DEBUG                                |
| `smokeTest.gbpAccountUid`   | The Account Uid that can be used to test the integration against the Starling Sandbox Environment | 1db3f775-36e3-4e9f-bc9b-582cde60017f |
| `smokeTest.gbpFeedFromTime` | The Transaction Feed from time used to specify the time to consider transactions within. Expressed as **UTC** | 2022-02-05T00:00:00Z                 |

## How to Build

- Pre-requisites
  - Maven

```sh
$ cd starling-task\sterling-tech-task
$ mvn clean install
```

## How to Run

```sh
$ mvn spring-boot:run
```

You will see a message similar to:

```txt
 Started SterlingTechTaskApplication in 1.475 seconds (JVM running for 1.851)
```

## How to Use

There are two ways to interact with the Round-Up application

- Postman
- Smoke Test
  - Must be configured in test `application.yml` as a property. Please see property configuration.

### Postman

Postman files have been provided in the `starling-task\postman` directory

- `Starling Env Apple.postman_environment.json`
  - The Postman environment
  - Note: the `apple` is just an arbitrary identifier and nothing to do with the famous company! I tend to use names of fruit instead of '1, 2, 3' suffixes
- `Starling Tech Task.postman_collection.json`
  - The Postman collection

#### Postman Configuration

- Ensure that you set up your bearer token the collection root's `Authorization` tab
  - The `BASE_URL` can also be configured here
    - This is a URL to Starling's Sandbox Environment


#### How to use the Postman Collection

##### Environment Variables

| Variable                  | Description                                                  | Example                              |
| ------------------------- | ------------------------------------------------------------ | ------------------------------------ |
| `accountUid`              | The Account Holder UID as obtained from the Starling Sandbox Environment | 7f38202e-6b48-4894-920a-97c732017370 |
| `minTransactionTimestamp` | The 'from time' to consider outgoing transactions from for the Round-Up Saving's Goal. In UTC. | 2022-02-05T00:00:00.000Z             |
| `maxTransactionTimestamp` | The 'totime' to consider outgoing transactions from for the Round-Up Saving's Goal. In UTC. | 2022-02-12T00:00:00.000Z             |

##### Steps to Create a Round-Up Savings Goal

1. View existing Savings Goals are empty

   1. Navigate to `Saving Goal` folder

   2. Get Saving Goals

   3. Note response

      ```json
      {
          "savingsGoalList": []
      }
      ```

2. Create a new Round-Up Savings Goal

   1. Navigate to `Round Up Saving Goal` folder

   2. Round Up Saving Goal

   3. Ensure that you have specified a `minTransactionTimestamp` in the environment variables

   4. A response will be seen **200 OK**

      ```json
      {
          "savingsGoalUid": "7d8b3416-45dc-437f-bc2d-2f22f7196df8"
      }
      ```

3. Repeat step 1 to view the Savings Goal as part of 'all savings goal'

4. Get the Round-Up Savings Goal

   1. Navigate to the `Saving Goal` folder

   2. Get Savings Goal

   3. The response will look like

      ```json
      {
          "savingsGoalUid": "7d8b3416-45dc-437f-bc2d-2f22f7196df8",
          "name": "Test Goal",
          "target": {
              "currency": "GBP",
              "minorUnits": 177
          },
          "totalSaved": {
              "currency": "GBP",
              "minorUnits": 0
          },
          "savedPercentage": 0
      }
      ```

5. Delete the Round-Up Savings Goal

   1. Navigate to the `Saving Goal` folder
   2. Delete Savings Goal
   3. The response will be **204 No Content**

6. Repeat step 1or 4 to confirm the Round-Up Savings Goal has been deleted 

   1. If requesting Savings Goal by Uid you, will see a 404 Not Found


### Smoke Test

A convenient integration test has been written to make it easier and faster to test against the Starling Sandbox Environment named `RoundUpGoalSmokeTest` . This started as a convenience for me as I did the dev-work which then I refactored as a smoke test as it can be useful to run the Round-Up application with.

The Smoke Tests have been commented out as I did not want them running (and failing) during the compilation of the application. The primary purpose is just to speed up the dev as opposed to a true 'smoke test' in this regard.

The Smoke Test is intended to use the real Starling Sandbox Environment. It can be configured using the test `application.yml` file. Please see the property configuration for more information about the individual properties.


##### What the Smoke Test does

The Smoke Test `whenCreateGbpRoundUpSavingsGoal_thenRoundUpSavingsGoalIsCreated` does the following

- Creates Round-Up Savings Task based on a Transaction 'from time' given in the `application.yml` 
- Deletes that Round-Up Savings Task it created
- The Logging Output can be read - useful if you set to `debug` also in `application.yml`


##### How to Run Smoke Test

- Set appropriate `smokeTest` properties in `application.yml`
- Un-comment test annotation in IntelliJ or chosen IDE
- Run test

## Words from IC

I really enjoyed this task and would-go-so-far-as-to-say this was the best organised and set technical interview task that certainly I've ever been asked to do. I am aware that in some cases I could have done things in a more 'direct' manner to save time instead of expanding out the code as I have done. For example, I could have used ObjectMapper to pull a generic JSON response from Starling instead of unmarshalling it into classes. I made certain choices based on creating a robust and scalable application that can (if we wanted) do more clever things! Otherwise, I had given myself until Sunday (13th Feb 22) to do as much as I could to first answer the main question, then to make it offer an insight into how I was thinking at the time of the implementation.

I found the API documentation and Sandbox environment a pleasure to work with. 

I would like to thank Starling for the opportunity to take this technical exercise.

Imran Chaudhri

