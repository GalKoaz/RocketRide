const express = require('express');
const router = express.Router();

const {
    getRide,
    getAliveRides,
    getAliveRidesInDate,
    addRide,
    updateRide
} = require('../controllers/ride');

router.route('/').post(addRide).put(updateRide);
router.route('/:ride_id').get(getRide);
router.route('/alive').get(getAliveRides);

// Query url of date, e.g.: /alive?day=x&month=y&year=z
router.route('/alive').get(getAliveRidesInDate);

module.exports = router;