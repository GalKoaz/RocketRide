const express = require('express');
const router = express.Router();

const {
    getRateModel,
    postRateModel
} = require('../controllers/rateDriver');

router.route('/').post(postRateModel).get(getRateModel);
router.route('/:driver_id').get(getRateModel);

module.exports = router;