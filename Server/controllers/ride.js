// Model functions
const {
    getRideByRideID,
    getAliveRidesModel,
    getAliveRidesInDateModel,
    addRideModel,
    updateRideAttr
} = require('../model/Firestore/rideModel');


const getRide = async (req, res) => {
    const {ride_id: rideID} = req.params;

    const rideJSON = await getRideByRideID(rideID);

    res.status(200).json({response: rideJSON});
}

const getAliveRides = async (req, res) => {
    const ridesJsonArray = await getAliveRides();
    res.status(200).json({response: ridesJsonArray});
}

const getAliveRidesInDate = async (req, res) => {
    const queryDateJSON = req.query;

    const ridesJsonArray = await getAliveRidesInDateModel(queryDateJSON);

    res.status(200).json({response: ridesJsonArray});
}

const addRide = async (req, res) => {
    const rideJSON = req.body;
    console.log(`RIDE MODEL\n${JSON.stringify(rideJSON)}`);

    doc_ref = await addRideModel(rideJSON);

    res.json({response: 'good!', result: rideJSON});
}

const updateRide = async (req, res) => {
    const attrJSON = req.body;
    console.log(`RIDE MODEL\n${JSON.stringify(attrJSON)}`);

    doc_ref = await updateRideAttr(attrJSON);
    res.json({response: 'good!', result: attrJSON});
}

module.exports = {
    getRide,
    getAliveRides,
    getAliveRidesInDate,
    addRide,
    updateRide
};