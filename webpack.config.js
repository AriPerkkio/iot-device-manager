const webpack = require('webpack');
const path = require('path');

// Plugins
const CleanWebpackPlugin = require('clean-webpack-plugin');
const UglifyJsPlugin = require('uglifyjs-webpack-plugin');
const ExtractTextPlugin = require("extract-text-webpack-plugin");
const extractSass = new ExtractTextPlugin({
  filename: "style.css",
  disable: process.env.NODE_ENV === "development"
});

// Store the built bundle into src/main/resources/static/bundle.js
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
  devtool: "source-map",
  resolve: {
    extensions: ['.js', '.jsx']
  },
  module : {
    loaders : [
      {
        test : /\.jsx?/,
        include : APP_DIR,
        loader : 'babel-loader'
      },
      {
        test: /\.scss$/,
        use: extractSass.extract({
            use: [
              { loader: "css-loader" , options: { sourceMap: true } },
              { loader: "sass-loader", options: { sourceMap: true } }
            ],
            fallback: "style-loader"
        })
      }
    ]
  },
  plugins: [
    new CleanWebpackPlugin(BUILD_DIR + '/*.*'),
    new UglifyJsPlugin({ sourceMap: true }),
    extractSass
  ],
  devServer: {
    historyApiFallback: true,
    port: 8081,
    contentBase: [ path.join(INDEX_HTML_DIR), path.join(BUILD_DIR)],
    overlay: {
      errors: true,
      warnings: true,
    },
    proxy: {
      "/api/**": {
        target: "http://localhost:8080"
      }
    }
  }
};

module.exports = config;