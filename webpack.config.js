const path = require("path");

module.exports = {
    entry: {
        app: [
            './src/js/index.js',
        ]
    },
    output: {
        path: path.resolve(__dirname,'build/resources/main/META-INF/resources/assets/'),
        // contenthash: The hash is based on the content of the file. If it stays the same -> Same hash.
        //filename: '[name].js?id=[contenthash]',
        //publicPath: publicPath,
        // Webpack has one global function. It is better to change it to avoid name clashes
        // If you plan to deploy multiple apps build with webpack, each needs a unique name here
        jsonpFunction: 'videochat'
    },
    module: {
        rules: [
            {
                test: /\.(js|jsx)$/,
                use: {
                    loader: "babel-loader"
                }
            }
        ]
    }
};