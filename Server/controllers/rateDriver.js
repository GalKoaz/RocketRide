// Model functions
const {
    getRateModel,
    addRateModel,
    updateRateModel,
} = require('../model/Firestore/rateModel');

const getRate = async (req, res) => {
    const {driver_id : driverID} = req.params;

    rateJSON = await getRateModel(driverID);
    console.log(JSON.stringify(rateJSON))

    res.status(200).json(rateJSON);
}

const postRate = async (req, res) => {
    const rateJSON = req.body;
    console.log(`RATE MODEL\n${JSON.stringify(rateJSON)}`);

    doc_ref = await addRateModel(rateJSON);

    res.status(201).json({response: 'good!', result: rateJSON});
}

const updateRate = async (req, res) => {
    const rateJSON = req.body;
    console.log(`RATE MODEL\n${JSON.stringify(rateJSON)}`);

    doc_ref = await updateRateModel(rateJSON);
    res.status(204).json({response: 'good!', result: rateJSON});
}

module.exports = {
    getRate,
    postRate,
    updateRate
};