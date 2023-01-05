
// Firestore db instance
const connectDB = require('./connect')
const collectionName = 'rates';

// Adds the rate model (json of rate) to Firestore.
const addRateModel = async (rateModel) => {
    try {
        // Add a new document with a generated ID
        const db = await connectDB();
        const docRef = await db.collection(collectionName).add(rateModel);
        console.log(`DocumentSnapshot added with ID: ${docRef.id}`);
    } catch (error) {
        console.error('Error adding document', error);
    }
}

// Get the rate model (json of rate) from Firestore by driver's id as the key.
const getRateModel = async (driverID) => {
    try{
        const db = await connectDB();
        const ratesRef = db.collection(collectionName);
        const queryRef = ratesRef.where('driver-id', '==', driverID);
        return await queryRef.get();
    } catch (error) {
        console.error('Error getting document', error);
    }
}

// Update rate model (json of rate) for Firestore.
const updateRateModel = async (rateModel) =>{
    try {
        const db = await connectDB();
        // Update a new document with a generated ID
        const docRef = await db.collection(collectionName).set(rateModel);
        console.log(`DocumentSnapshot added with ID: ${docRef.id}`);
    } catch (error) {
        console.error('Error adding document', error);
    }
}

module.exports = {
    getRateModel,
    addRateModel,
    updateRateModel
};


