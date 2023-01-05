// Model functions
const {
    getUserDetails
} = require('../model/Firestore/userModel');

const getUser = async (req, res) => {
    const {UID: userID} = req.params;
    const userJSON = await getUserDetails(userID);
    res.status(200).json({response: userJSON});
}

module.exports = {
    getUser
};