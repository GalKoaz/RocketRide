const express = require('express');
const router = express.Router();

const {
    getRate,
    postRate,
    updateRate
} = require('../controllers/rateDriver');

router.route('/').post(postRate).get(getRate).put(updateRate);
router.route('/:driver_id').get(getRate);

module.exports = router;