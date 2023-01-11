const express = require('express');
const router = express.Router();

const {
    getUser,
    addUser,
    updateUserDetails
} = require('../controllers/user');

router.route('/').post(addUser);
router.route('/:UID').get(getUser).put(updateUserDetails);

module.exports = router;