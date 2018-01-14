const webpack = require('webpack');
const path = require('path');

// Plugins
const CleanWebpackPlugin = require('clean-webpack-plugin');
const UglifyJsPlugin = require('uglifyjs-webpack-plugin')

// Store built bundle into src/main/resources/static/bundle.js
const BUILD_DIR = path.resolve(__dirname, 'src/main/resources/static');
const APP_DIR = path.resolve(__dirname, 'src/main/js');
const TARGET_NAME = 'bundle.js';
const INDEX_HTML_DIR = 'src/main/resources/templates';

const config = {
  entry: APP_DIR + '/index.jsx',
  output: {
    path: BUILD_DIR,
    filename: TARGET_NAME
  },
  module : {
    loaders : [
      {
        test : /\.jsx?/,
        include : APP_DIR,
        loader : 'babel-loader'
      }
    ]
  },
  plugins: [
    new CleanWebpackPlugin([BUILD_DIR + '/' + TARGET_NAME]),
    new UglifyJsPlugin()
  ],
  devServer: {
    port: 8081,
    contentBase: path.join(INDEX_HTML_DIR),
    overlay: {
      errors: true,
      warnings: true,
    },
    proxy: {
      "/rest": {
        target: "http://localhost:8080"
      }
    }
  }
};

module.exports = config;