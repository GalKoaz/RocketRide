// Model functions
const {
    getRideByRideID,
    getAliveRidesModel,
    getAliveRidesInDateModel,
    addRideModel,
    updateRideAttr
} = require('../model/Firestore/rideModel');

const { getUserDetails} = require('../model/Firestore/userModel');
const { getRateModel } = require('../model/Firestore/rateModel');

const getRide = async (req, res) => {
    const {ride_id: rideID} = req.params;

    const rideJSON = await getRideByRideID(rideID);

    res.status(200).json({response: rideJSON});
}

const getAliveRides = async (req, res) => {
    const ridesJsonArray = await getAliveRidesModel();
    console.log(JSON.stringify(ridesJsonArray));
    res.status(200).json({response: ridesJsonArray});
}

const getAliveRidesInDate = async (req, res) => {
    const queryDateJSON = req.query;
    console.log(JSON.stringify(queryDateJSON));
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
    const {ride_id: rideID} = req.params;
    const attrJSON = req.body;
    console.log(`RIDE MODEL\n${JSON.stringify(attrJSON)}`);

    doc_ref = await updateRideAttr(ride_id, attrJSON);
    res.json({response: 'good!', result: attrJSON});
}

const getRideRiderDetails = async (req, res) => {
    const {ride_id: rideID} = req.params;

    const rideJSON = await getRideByRideID(rideID);
    const driverID = await rideJSON['driver-id'];
    let [userDetailsJSON, driverRateJSON] = await Promise.all([getUserDetails(driverID), getRateModel(driverID)]);

    const responseJSON = Object.assign(rideJSON, userDetailsJSON, driverRateJSON);
    res.json({response: responseJSON});
}

module.exports = {
    getRide,
    getAliveRides,
    getAliveRidesInDate,
    addRide,
    updateRide,
    getRideRiderDetails
};