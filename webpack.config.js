const webpack = require('webpack');
const path = require('path');

// Plugins
const CleanWebpackPlugin = require('clean-webpack-plugin');
const ExtractTextPlugin = require("extract-text-webpack-plugin");
const extractSass = new ExtractTextPlugin({
  filename: "style.css"
});

// Store the built bundle into src/main/resources/static/bundle.js
const BUILD_DIR = path.resolve(__dirname, 'src/main/resources/static');
const APP_DIR = path.resolve(__dirname, 'ui');
const TARGET_NAME = 'bundle.js';
const INDEX_HTML_DIR = 'src/main/resources/templates';

module.exports = (env, argv) => ({
  entry: APP_DIR + '/index.jsx',
  output: {
    path: BUILD_DIR,
    filename: TARGET_NAME
  },
  devtool: argv.mode == "production" ? false : "source-map",
  resolve: {
    extensions: ['.js', '.jsx']
  },
  module : {
    rules : [
      {
        test : /\.jsx?/,
        exclude: /node_modules/,
        loader : 'babel-loader'
      },
      {
        test: /\.(scss|css)$/,
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
    extractSass
  ],
  devServer: {
    historyApiFallback: true,
    inline: true,
    port: 8081,
    contentBase: [ path.join(INDEX_HTML_DIR), path.join(BUILD_DIR), path.join(APP_DIR) ],
    overlay: {
      errors: true,
      warnings: true,
    },
    proxy: {
      "/api/**": {
        target: "http://localhost:8080"
      }
    }
  },
  performance: {
    hints: false
  }
});
