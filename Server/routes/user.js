const express = require('express');
const router = express.Router();

const {
    getUser,
    addUser
} = require('../controllers/user');

router.route('/').post(addUser);
router.route('/:UID').get(getUser);

module.exports = router;