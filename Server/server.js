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

server.use('/api/v1/rate_driver', rateDriverRouter);

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