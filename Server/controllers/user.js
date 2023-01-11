// Model functions
const {
    getUserDetails,
    addUserModel
} = require('../model/Firestore/userModel');

const getUser = async (req, res) => {
    const {UID: userID} = req.params;
    const userJSON = await getUserDetails(userID);
    res.status(200).json({response: userJSON});
}

const addUser = async (req, res) => {
    const userJSON = req.body;
    await addUserModel(userJSON);
    res.status(201).json(userJSON);
}

module.exports = {
    getUser,
    addUser
};