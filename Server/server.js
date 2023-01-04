require('dotenv').config();

const createError = require('http-errors');
const express = require('express');

const server = express();
const PORT = process.env.PORT;

// catch 404 and forward to error handler
server.use(function(req, res, next) {
  next(createError(404));
});

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