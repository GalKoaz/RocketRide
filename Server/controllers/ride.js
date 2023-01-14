// Model functions
const {
    getRideByRideID,
    getAliveRidesModel,
    getAliveRidesInDateModel,
    getExpiredRidesModel,
    addRideModel,
    updateRideAttr
} = require('../model/Firestore/rideModel');

const { getUserDetails} = require('../model/Firestore/userModel');
const { getRateModel } = require('../model/Firestore/rateModel');

const getRide = async (req, res) => {
    const {ride_id: rideID} = req.params;

    const rideJSON = await getRideByRideID(rideID);

    res.status(200).json(rideJSON);
}

const getAliveRides = async (req, res) => {
    // Query url of date, e.g.: /alive?day=x&month=y&year=z
    if(req.query.day && req.query.month && req.query.year) {
        await getAliveRidesInDate(req, res);
        return;
    }
    // Regular request: /alive
    const ridesJsonArray = await getAliveRidesModel();
    console.log(JSON.stringify(ridesJsonArray));
    res.status(200).json(ridesJsonArray);
}

const getAliveRidesInDate = async (req, res) => {
    const queryDateJSON = req.query;

    // Convert the json values from string to integers
    Object.keys(queryDateJSON).map(function(key, index) {
        queryDateJSON[key] = parseInt(queryDateJSON[key]);
    });
    const ridesJsonArray = await getAliveRidesInDateModel(queryDateJSON);
    console.log(ridesJsonArray)
    res.status(200).json(ridesJsonArray);
}

const getExpiredRides = async (req, res) => {
    const ridesJsonArray = await getExpiredRidesModel();
    console.log(JSON.stringify(ridesJsonArray));
    res.status(200).json(ridesJsonArray);
}

const addRide = async (req, res) => {
    const rideJSON = req.body;
    console.log(`RIDE MODEL\n${JSON.stringify(rideJSON)}`);

    doc_ref = await addRideModel(rideJSON);

    res.status(201).json({response: 'good!'});
}

const updateRide = async (req, res) => {
    const {ride_id: rideID} = req.params;
    const attrJSON = req.body;
    console.log(`RIDE MODEL\n${JSON.stringify(attrJSON)}`);

    doc_ref = await updateRideAttr(ride_id, attrJSON);
    res.status(200).json({response: 'good!'});
}

const getRideRiderDetails = async (req, res) => {
    const {ride_id: rideID} = req.params;

    const rideJSON = await getRideByRideID(rideID);
    const driverID = await rideJSON['driver-id'];
    let [userDetailsJSON, driverRateJSON] = await Promise.all([getUserDetails(driverID), getRateModel(driverID)]);

    const responseJSON = Object.assign(rideJSON, userDetailsJSON, driverRateJSON);
    res.status(200).json(responseJSON);
}

module.exports = {
    getRide,
    getAliveRides,
    getAliveRidesInDate,
    getExpiredRides,
    addRide,
    updateRide,
    getRideRiderDetails
};