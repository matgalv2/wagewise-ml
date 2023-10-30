db.createUser({
    user: 'root',
    pwd: 'password',
    roles: [
        {
            role: 'readWrite',
            db: 'admin',
        },
    ],
});

db = new Mongo().getDB("wagewise")

db.createCollection('employments');
db.createCollection('model_settings');