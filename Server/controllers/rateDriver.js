// Model functions
const {
    addRateModel
} = require('../model/Firestore/rateModel')

const getRateModel = async (req, res) => {
    const {driver_id : driverID} = req.params;
    // TODO: complete this - it's just for testing and a part of CRUD API
    // doc_ref = await getRateModel(driverID);
    res.status(200).json({response: 'good!', req_id: driverID});
}

const postRateModel = async (req, res) => {
    //TODO: get post parameters json with req.body
    res.json({response: 'good!'});
}

module.exports = {
    getRateModel,
    postRateModel
};