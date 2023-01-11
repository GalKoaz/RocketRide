// Model functions
const {
    getUserDetails,
    addUserModel,
    updateUserAttr
} = require('../model/Firestore/userModel');

const getUser = async (req, res) => {
    const {UID: userID} = req.params;
    const userJSON = await getUserDetails(userID);
    res.status(200).json(userJSON);
}

const addUser = async (req, res) => {
    const userJSON = req.body;
    doc_ref = await addUserModel(userJSON);
    console.log("add user...")
    res.status(201).json({response: "User added successfuly!"});
}

const updateUserDetails = async (req, res) => {
    const {UID: userID} = req.params;
    const attrJSON = req.body;
    doc_ref = await updateUserAttr(userID, attrJSON);

    if (doc_ref == null) {
        res.status(404).json({response: "No such user found!"});
        return;
    }
    res.status(200).json({response: "User attributes changed successfuly!"});
}

module.exports = {
    getUser,
    addUser,
    updateUserDetails
};