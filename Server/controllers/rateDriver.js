// Model functions
const {
    addRateModel
} = require('../model/Firestore/rateModel')

const getRateModel = async (req, res) => {
    // TODO: complete this - it's just for testing and a part of CRUD API
    res.status(200).json({response: 'good!'});
}

const postRateModel = async (req, res) => {
    //TODO: get post parameters json with req.body
    res.json({response: 'good!'});
}

module.exports = {
    getRateModel,
    postRateModel
};