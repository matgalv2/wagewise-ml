# Salary prediction service
Simple REST application for predicting potential salary for programmers based on their features. Dataset which model is trained on comes from [github](https://github.com/itstats/programmers_salaries).

## API
### Endpoints
    /predict-salary 
        Requires request body with fields: 
            1. "value" - array of Programmer type objects
        Responses:
            200 - if the body is valid return array of PredictedSalary type objects
            400 - in other case


### Types
    PredictedSalary:
        - id (UUID)
        - salary_monthly (number)
        - rate_per_hour (number)

    Programmer:
        - id (UUID)
        - date_of_employment (date)
        - sex (enum)
        - country (enum)
        - experience_years_it (integer)
        - languages (enum)
        - speciality (enum)
        - core_programming_language (enum)
        - academic_title (enum)
        - education_towards_it (boolean)
        - company_country (enum)
        - company_type (enum)
        - work_form (enum)
        - team_size (integer)
        - team_type (enum)
        - form_of_employment (enum)
        - full_time (boolean)
        - paid_days_off (boolean)
        - insurance (boolean)
        - training_sessions (boolean)

    
## Attributes description
| Attribute | Domain | Type |
| ---------- | --------- | ---------- |
| id | uuid | string |
| date_of_employment | date | date |
| sex | [M, F] | enum |
| country | [Russia, Greece, France, Germany, Poland, United Kingdom, Spain, Sweden, Italy] | enum |
| experience_years_it | [0-30] | integer |
| languages | [Spanish,English; Spanish; Swedish; French,English; English; Russian; Swedish,English; Italian; Russian,English; Italian,English;German,English; German; Greek; French; Polish; Polish,English; Greek,English] | enum |
| speciality | [Data scientist, Frontend, Web administrator, Applications engineer, Backend, IT Security specialist, Data quality manager, DB Administrator, Computer scientist, Cloud system engineer, Software Engineer, Systems analyst, Other, Tech lead] | enum |
| core_programming_language | [JavaScript, PHP, Java, Go, Cobol, Python, Objective-C, Ruby, Kotlin, Swift, Other, R] | enum |
| academic_title | [Doctorate, Master, No degree, Licence, Bachelor] | enum |
| education_towards_it | [False, True] | boolean |
| company_country | [Russia, Greece, France, Germany, Poland, United Kingdom, Spain, Sweden, Italy] | enum |
| company_type | [Software house, Corporation, Big tech, Startup, Public institution, Company, Other] | enum |
| work_form | [hybrid, remote, stationary] | enum |
| team_size | [3-30] | integer |
| team_type | [local, international] | enum |
| form_of_employment | [contractor, employee] | enum |
| full_time | [False, True] | boolean |
| paid_days_off | [False, True] | boolean |
| insurance | [False, True] | boolean |
| training_sessions | [False, True] | boolean |



