require('dotenv').config();

const createError = require('http-errors');
const express = require('express');
const bodyParser = require('body-parser');

const server = express();
const PORT = process.env.PORT;

// Use for body requests
server.use(bodyParser.json());

// Routers
const rateDriverRouter = require('./routes/rateDriver');
const rideRouter = require('./routes/ride');
const userRouter = require('./routes/user');

server.use('/api/v1/rate_driver', rateDriverRouter);
server.use('/api/v1/rides', rideRouter);
server.use('/api/v1/users', userRouter);

const start = async () => {
  try {
    server.listen(PORT, () => {
      console.log(`listening to port ${PORT}...`)
    })
  } catch (error) {
    console.log(error);
  }
};

start();