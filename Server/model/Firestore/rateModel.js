// Firestore db instance
const connectDB = require('./connect')
const collectionName = 'rates';

// Database reference
let db;

const start = async () => {
    db = await connectDB();
}
// start connecting to firestore
start();

// Adds the rate model (json of rate) to Firestore.
const addRateModel = async (rateModel) => {
    try {
        // Add a new document with a generated ID
        const docRef = await db.collection(collectionName).add(rateModel);
        console.log(`DocumentSnapshot added with ID: ${docRef.id}`);
    } catch (error) {
        console.error('Error adding document', error);
    }
}

// Get the rate model (json of rate) from Firestore by driver's id as the key.
const getRateModel = async (driverID) => {
    try{
        const ratesRef = db.collection(collectionName);
        const queryRef = ratesRef.where('driver-id', '==', driverID);
        const querySnapshot = await queryRef.get();

        // Check if there are any results for the query
        if (!querySnapshot.empty) {
            const doc = querySnapshot.docs[0];
            return doc.data();
        } else {
            return null;
        }
    } catch (error) {
        console.error('Error getting document', error);
    }
}

// Update rate model (json of rate) for Firestore.
const updateRateModel = async (rateModel) =>{
    try {
        // Update a new document with a generated ID
        const docRef = await db.collection(collectionName).doc(rateModel['driver-id']).set(rateModel);
        console.log(`DocumentSnapshot added with ID: ${docRef.id}`);
    } catch (error) {
        console.error('Error updating document', error);
    }
}

module.exports = {
    getRateModel,
    addRateModel,
    updateRateModel
};


