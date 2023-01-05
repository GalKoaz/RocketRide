const express = require('express');
const router = express.Router();

const {
    getUser
} = require('../controllers/user');

router.route('/:UID').get(getUser);

module.exports = router;