// Model functions
const {
    getRateModel,
    addRateModel,
    updateRateModel,
} = require('../model/Firestore/rateModel');

const getRate = async (req, res) => {
    const {driver_id : driverID} = req.params;
    // TODO: complete this - it's just for testing and a part of CRUD API
    doc_ref = await getRateModel(driverID);

    res.status(200).json({response: 'good!', req_id: driverID});
}

const postRate = async (req, res) => {
    const rateJSON = req.body;
    console.log(`RATE MODEL\n${JSON.stringify(rateJSON)}`);

    doc_ref = await addRateModel(rateJSON);

    //TODO: get post parameters json with req.body
    res.json({response: 'good!', result: rateJSON});
}

const updateRate = async (req, res) => {
    res.json({response: 'good!'});
}

module.exports = {
    getRate,
    postRate,
    updateRate
};