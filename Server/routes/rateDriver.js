const express = require('express');
const router = express.Router();

const {
    getRateModel,
    postRateModel
} = require('../controllers/rateDriver');

router.route('/').post(postRateModel);
router.route('/').get(getRateModel);

module.exports = router;