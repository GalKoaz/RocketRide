const { initializeApp, applicationDefault, cert } = require('firebase-admin/app');
const { getFirestore, Timestamp, FieldValue } = require('firebase-admin/firestore');

// Function connects to firestore
const connectDB = async () => {
    const serviceAccount = require(process.env.GOOGLE_APPLICATION_CREDENTIALS);

    initializeApp({
        credential: cert(serviceAccount)
    });

    return getFirestore();
}
module.exports = connectDB