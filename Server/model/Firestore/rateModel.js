
// Firestore db instance
const db = require('./connect')
const collectionName = 'rates';

// Adds the rate model (json of rate) to Firestore
const addRateModel = async (rateModel) => {
    try {
        // Add a new document with a generated ID
        const docRef = await db.collection(collectionName).add(rateModel);
        console.log(`DocumentSnapshot added with ID: ${docRef.id}`);
    } catch (error) {
        console.error('Error adding document', error);
    }
}

module.exports = {
  addRateModel
};


