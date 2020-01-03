const path = require('path');
const {CleanWebpackPlugin} = require('clean-webpack-plugin');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const {args2options} = require('peacetrue-js/src/node');
let options = args2options(process.argv, '-pt:');
//生成一个源文件、一个压缩文件和一个source map 文件

let config = {
    mode: 'development',
    entry: {
        'Oauth': './src/oauth.js',
    },
    devtool: 'source-map',
    plugins: [
        new CleanWebpackPlugin(),
    ],
    devServer: {
        contentBase: './'
    },
    module: {
        rules: [{test: /\.css$/, use: ['style-loader', 'css-loader']}]
    },
    output: {
        path: path.resolve(__dirname, 'dist'),
        filename(chunkData) {
            let name = chunkData.chunk.name.replace(/([A-Z])/g, (value) => '_' + value.toLowerCase()).substr(1);
            return `${name}.${options.mode === 'production' ? 'min.' : ''}js`;
        },
        library: '[name]',
        libraryExport: '',
        libraryTarget: 'umd',
        globalObject: 'this',
    },
    externals: {
        'vue': 'vue',
        'iview/dist/iview': 'iview',
        'axios': 'axios',
        'lodash': {
            root: '_',
            commonjs: 'lodash',
            commonjs2: 'lodash',
            amd: 'lodash',
        },
    }
};

/* prefix:--
plugins|p=html,clean  //指定使用的插件
*/
function formatAlias(options, alias) {
    Object.keys(alias).forEach(key => {
        if (key in options) {
            options[alias[key]] = options[key];
        }
    });
}

formatAlias(options, {p: 'plugins'});
if (options.plugins && options.plugins.indexOf('html') > -1) {
    config.plugins.push(new HtmlWebpackPlugin({
            title: 'Test',
            inject: 'head',
            template: 'test/oauth.ejs'
        })
    );
}
module.exports = config;