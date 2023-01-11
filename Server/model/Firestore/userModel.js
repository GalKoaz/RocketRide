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

const addUserModel = async (userModel) => {
    try{
        const ratesRef = db.collection(collectionName);
        return await ratesRef.add(userModel);
    } catch (error) {
        console.error('Error getting document', error);
    }
}

// Gets a JSON of attributes to change with their new value, given a user id.
const updateUserAttr = async (userID, attributesJSON) => {
    try {
        // Add a new document with a generated ID
        const queryRef = db.collection(collectionName).where('UID', '==', userID);
        const querySnapshot = await queryRef.get();

        // Check if there are any results for the query
        if (!querySnapshot.empty) {
            const doc = querySnapshot.docs[0];
            doc_ref = await db.collection(collectionName).doc(doc.id).update(attributesJSON);
            return doc_ref;
        } else {
            return null;
        }
        console.log(`DocumentSnapshot updated with ID: ${docRef.id}`);
    } catch (error) {
        console.error('Error updating document', error);
    }
}

module.exports = {
    getUserDetails,
    addUserModel,
    updateUserAttr
};
