const { initializeApp, applicationDefault, cert } = require('firebase-admin/app');
const { getFirestore, Timestamp, FieldValue } = require('firebase-admin/firestore');

// Function connects/certificate to firestore
const serviceAccount = require(`../../${process.env.GOOGLE_APPLICATION_CREDENTIALS}`);

initializeApp({
    credential: cert(serviceAccount)
});

module.exports = {
    getFirestore
};
