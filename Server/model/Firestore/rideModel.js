// Firestore db instance
const {getFirestore} = require('./connect')
const collectionName = 'drives';

// Database reference
let db;

const start = async () => {
    db = await getFirestore();
}
// start connecting to firestore
start();

// Get the ride JSON given a ride ID (docmuent's id)
const getRideByRideID = async (rideID) => {
    try{
        const drivesRef = db.collection(collectionName);
        const queryRef = drivesRef.where('_id', '==', rideID);
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

// Gets a JSON of attributes to change with their new value, given a ride id.
const updateRideAttr = async (rideID, attributesJSON) => {
    try {
        // Add a new document with a generated ID
        const docRef = await db.collection(collectionName).doc(rideID).update(attributesJSON);
        console.log(`DocumentSnapshot updated with ID: ${docRef.id}`);
    } catch (error) {
        console.error('Error updating document', error);
    }
}

// Adds a ride document given a JSON of ride attributes.
const addRideModel = async (rideModel) => {
    try {
        // Add a new document with a generated ID
        const docRef = await db.collection(collectionName).add(rideModel);
        console.log(`DocumentSnapshot added with ID: ${docRef.id}`);
    } catch (error) {
        console.error('Error adding document', error);
    }
}

// Get all the alive rides
const getAliveRidesModel = async () => {
    const drivesRef = db.collection(collectionName);
    const queryRef = drivesRef.where('alive', '==', true);
    try {
        const querySnapshots = await queryRef.get();
        // Check if there are any results for the query
        if (!querySnapshot.empty) {
            return querySnapshot.docs.map(doc => doc.data());
        } else {
            return [];
        }
    } catch (error) {
        console.error('Error getting document', error);
    }
}

// Get alive rides documents given a date (as a JSON).
const getAliveRidesInDateModel = async (dateJSON) => {
    const drivesRef = db.collection(collectionName);
    const queryRef = drivesRef.where('alive', '==', true)
        .where('date-d', '==', dateJSON.day)
        .where('date-m', '==', dateJSON.month)
        .where('date-y', '==', dateJSON.year);
    try {
        const querySnapshots = await queryRef.get();
        // Check if there are any results for the query
        if (!querySnapshot.empty) {
            return querySnapshot.docs.map(doc => doc.data());
        } else {
            return [];
        }
    } catch (error) {
        console.error('Error getting document', error);
    }
}

module.exports = {
    getRideByRideID,
    updateRideAttr,
    addRideModel,
    getAliveRidesModel,
    getAliveRidesInDateModel
};