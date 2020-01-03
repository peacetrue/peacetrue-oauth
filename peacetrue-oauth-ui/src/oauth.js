let {Card, Button} = require('iview/dist/iview');
let Axios = require('axios');

module.exports = {
    name: 'Oauth',
    template: `
    <div>
        <Card v-if="accessToken">
            <p slot="title">
                <Icon type="ios-film-outline"></Icon>
                访问令牌
            </p>
            <div slot="extra">
                <a href="#"  @click.prevent="refreshAccessToken">
                    <Icon type="ios-loop-strong"></Icon>
                    立即刷新
                </a>
<!--
                <a href="#"  @click.prevent="enableAutoRefreshAccessToken">
                    <Icon type="ios-loop-strong"></Icon>
                    启用自动刷新
                </a>
-->
            </div>
            <ul>
                <li>访问令牌：{{accessToken.accessToken}}</li>
                <li>过期时间：{{accessToken.expiresIn/1000/60}}分</li>
                <li>刷新令牌：{{accessToken.refreshToken}}</li>
                <li>作用域：{{accessToken.scope}}</li>
                <li>授权标识：{{accessToken.openId}}</li>
                <li>本地创建时间：{{accessToken.createdTime}}</li>
                <li>剩余时间：{{accessToken.remainedTime/1000/60}}分</li>
            </ul>
        </Card>
        <Button v-else @click="openAuthorizationPage">获取访问令牌</Button>    
    </div>
    `,
    props: {
        host: {type: String, required: false, default: 'http://mbib9i.natappfree.cc'},
        url4GenerateAuthorizationUrl: {type: String, required: false, default: '/oauth/authorization/url?scope=snsapi_base'},
        accessTokenUrl: {type: String, required: false, default: '/oauth/access-token'},
        refreshAccessTokenUrl: {type: String, required: false, default: '/oauth/access-token/refresh'},
        // enableAutoRefreshAccessTokenUrl: {type: String, required: false, default: '/oauth/access-token/enable/auto/refresh'},
    },
    data() {
        return {
            accessToken: null,
            authorizationUrl: null,
        };
    },
    methods: {
        setAuthorizationUrl() {
            let url = this.host + this.url4GenerateAuthorizationUrl + `&targetUrl=${encodeURIComponent(window.location.href)}`;
            return Axios.get(url).then(t => this.authorizationUrl = t.data);
        },
        openAuthorizationPage() {
            window.open(this.authorizationUrl, "_self");
        },
        setAccessToken() {
            return Axios.get(this.host + this.accessTokenUrl).then(t => this.accessToken = t.data);
        },
        refreshAccessToken() {
            return Axios.post(this.host + this.refreshAccessTokenUrl).then(t => this.accessToken = t.data);
        },
        enableAutoRefreshAccessToken() {
            return Axios.post(this.host + this.enableAutoRefreshAccessTokenUrl);
        }
    },
    created() {
        //没有同时调用，防止生成不同的JSESSIONID
        this.setAuthorizationUrl().finally(t => this.setAccessToken());
    },
    components: {
        Card, Button
    }
};

