db = new Mongo().getDB("product")


db.users.insert(
    {
        username: 'jeden',
        password: '$2a$07$wtfgSDSno6IaISFlsQn/ouh5NqShnvVuqfeiDrtdXvhDzrNrSX6Fi',
    }
)

db.workers.insert(
    {
        "_id": "95fe99be-29c7-4a7c-880f-fb31d92ce368",
        "name": "Mateusz",
        "surname": "Galicki",
        "email": "mateusz.galicki@wagewise.io",
        "avail": true,
        "spokenLang": [
            "Polish", "English"
        ],
        "specialities": [
            "Backend",
        ],
        "progLang": [
            "Java"
        ],
        "nrPhone": "7392837185",
        "sex": "M",
        "country": "Poland",
        "academicTitle": "No degree",
        "edInIT": true,
        "yearsExp": 5,
        "formWork": "remote"
    }
)

db.workers.insert(
    {
        "_id": "95fe99be-29c7-4a7c-880f-fb31d92ce201",
        "name": "Kuba",
        "surname": "Maciążek",
        "email": "kuba.maciazek@wagewise.io",
        "avail": true,
        "spokenLang": [
            "Polish", "English"
        ],
        "specialities": [
            "Frontend",
        ],
        "progLang": [
            "Swift"
        ],
        "nrPhone": "686187654",
        "sex": "M",
        "country": "Poland",
        "academicTitle": "Licence",
        "edInIT": true,
        "yearsExp": 10,
        "formWork": "hybrid"
    }
)

db.workers.insert(
    {
        "_id": "95fe99be-29c7-4a7c-880f-fb31d52ce3a9",
        "name": "Piotr",
        "surname": "Krakowiak",
        "email": "piotr.krakowiak@wagewise.io",
        "avail": true,
        "spokenLang": [
            "Polish", "English"
        ],
        "specialities": [
            "Computer scientist",
        ],
        "progLang": [
            "Go"
        ],
        "nrPhone": "740212369",
        "sex": "M",
        "country": "Poland",
        "academicTitle": "Bachelor",
        "edInIT": true,
        "yearsExp": 7,
        "formWork": "stationary"
    }
)

db.workers.insert(
    {
        "_id": "f2baf5e9-b1a7-4365-a105-17509765e919",
        "name": "Tomasz",
        "surname": "Jasiński",
        "email": "tomasz.jasinski@wagewise.io",
        "avail": true,
        "spokenLang": [
            "Spanish"
        ],
        "specialities": [
            "IT Security specialist",
        ],
        "progLang": [
            "Ruby"
        ],
        "nrPhone": "740212369",
        "sex": "M",
        "country": "Poland",
        "academicTitle": "Master",
        "edInIT": true,
        "yearsExp": 13,
        "formWork": "hybrid"
    }
)

db.orders.insert(
    {
        company: "Google",
        status: "New",
        requirements: [
            "Junior Software Engineer Specialist",
            "Mid Backend Developer",
            "Senior Data Scientist "
        ]
    }
)

db.orders.insert(
    {
        company: "Amazon",
        status: "New",
        requirements: [
            "Junior Frontend Developer",
            "Mid Cobol Developer",
            "Senior Objective-C Developer",
        ]
    }
)

db.orders.insert(
    {
        company: "Nothing",
        status: "Pending",
        requirements: [
            "Junior Tech lead",
            "Senior Web Administrator Specialist",
            "Mid IT Security Specialist"
        ]
    }
)
