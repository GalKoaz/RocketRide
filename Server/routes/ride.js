const express = require('express');
const router = express.Router();

const {
    getRide,
    getAliveRides,
    getAliveRidesInDate,
    getExpiredRides,
    addRide,
    updateRide,
    getRideRiderDetails
} = require('../controllers/ride');

router.route('/').post(addRide);

router.route('/alive').get(getAliveRides);

router.route('/expired').get(getExpiredRides);

router.route('/:ride_id/rider_details').get(getRideRiderDetails);

router.route('/:ride_id').get(getRide).put(updateRide);

module.exports = router;