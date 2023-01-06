// Firestore db instance
const {getFirestore} = require('./connect')
const collectionName = 'users';

// Database reference
let db;

const start = async () => {
    db = await getFirestore();
}
// start connecting to firestore
start();

// Get the user details by his user id
const getUserDetails = async (userID) => {
    try{
        const ratesRef = db.collection(collectionName);
        const queryRef = ratesRef.where('UID', '==', userID);
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

module.exports = {
    getUserDetails
};
