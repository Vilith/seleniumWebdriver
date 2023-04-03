Feature: Create user

  Scenario Outline: Usercreation
    Given i am using "<browser>"
    And that i am on the registrationpage
    When i submit "<username>" "<email>" and "<password>"
    And i click signup button
    Then i should be "<registered>" and get "<message>"

    Examples:
      | browser | username                                                                                         | email                       | password    | registered | message                                                      |
      | chrome  | testExistingUser                                                                                 | thisemailexists@testing.com | Testing123! | no         | Great minds think alike - someone already has this username. |
      | chrome  | testuser                                                                                         |                             | Testing123! | no         | An email address must contain a single @.                    |
      | chrome  | moreThanOnehundredlettersInTheUsernameMightBeTooMuchSoOnehundredLettersInTheUsernameIsTooMuchBro | testmail@testing.com        | Testing123! | no         | Enter a value less than 100 characters long                  |
      | chrome  | testuser                                                                                         | testmail@testing.com        | Testing123! | yes        | Check your email                                             |
      | edge    | testExistingUser                                                                                 | thisemailexists@testing.com | Testing123! | no         | Great minds think alike - someone already has this username. |
      | edge    | testuser                                                                                         |                             | Testing123! | no         | An email address must contain a single @.                    |
      | edge    | moreThanOnehundredlettersInTheUsernameMightBeTooMuchSoOnehundredLettersInTheUsernameIsTooMuchBro | testmail@testing.com        | Testing123! | no         | Enter a value less than 100 characters long                  |
      | edge    | testuser                                                                                         | testmail@testing.com        | Testing123! | yes        | Check your email                                             |