Feature: Home Page Management

  Background:
    Given The app is open on the Homepage

  Scenario: Display Date Range for Selected Month
    When I select the records for the month "OCTOBER"
    Then The text of the date range displayed is "October <current_year>"

  Scenario: Display Date Range for Selected Year
    When I select the records for the year "2020"
    Then The text of the date range displayed is "Year 2020"

#  Scenario: Display Date Range for Custom Date Range
#    When I select the records for a custom date range from 2 months and 3 days ago to 4 days ago
#    Then The text of the date range displayed is correct for that range
#
#  Scenario: Add a new expense record
#    Given I add a new expense record with the following details:
#      | Name      | Amount | Category |
#      | Groceries | 50.25  | Food     |
#    Then The expense record "Groceries" for "50.25" is displayed in the list
#    And I delete the record "Groceries" to clean up
#
#  Scenario: Add a new income record
#    Given I add a new income record with the following details:
#      | Name   | Amount | Category | Repeat        |
#      | Salary | 1500.0 | Salary   | Every month   |
#    Then The income record "Salary" for "1500.0" is displayed in the list
#    And I delete the record "Salary" to clean up
#
#  Scenario: Delete a record
#    Given I add a new income record with the following details:
#      | Name   | Amount | Category |
#      | Salary | 1500.0 | Salary   |
#    When I delete the record "Salary"
#    Then The record "Salary" is not displayed in the list
#
#  Scenario: Filter records to display only for the selected month
#    Given A record "Groceries" for the current month exists
#    And A record "Rent" for a different month exists
#    When I filter the records by the current month
#    Then The record "Groceries" is displayed
#    And The record "Rent" is not displayed
#
#  Scenario: Filter records to display only for the selected year
#    Given A record "Salary" for the current year exists
#    And A record "Bonus" for a different year exists
#    When I filter the records by the current year
#    Then The record "Salary" is displayed
#    And The record "Bonus" is not displayed
#
#  Scenario: Filter records to display only for a custom date range
#    Given A record "Table and chairs" within the last 3 weeks exists
#    And A record "Train ticket" outside the last 3 weeks exists
#    When I filter the records from 3 weeks ago to 1 week ago
#    Then The record "Table and chairs" is displayed
#    And The record "Train ticket" is not displayed
