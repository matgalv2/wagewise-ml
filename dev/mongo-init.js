db.createUser({
    user: 'user',
    pwd: 'password',
    roles: [
        {
            role: 'readWrite',
            db: 'wagewise',
        },
        {
            role: 'readWrite',
            db: 'admin',
        }
    ],
});

db = new Mongo().getDB("wagewise")
db.employments.insert({name: "test document"})
