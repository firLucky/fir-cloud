import {JSEncrypt} from "jsencrypt";

const crypto = require('crypto');
const CryptoJS = require('crypto-js');


/** 全局变量配置-start **/

// 全局非对称整体加解密
const RSAKey = true
// 全局对称整体加解密
const AESKey = true
// 防重放
const replay = true;
// 完整性
const reqIntegrity = true;

// url白名单设置
const whiteList = [
    "/k",
    "/cn",
    "/auth/login",
    "/auth/logout",
]

/** 全局变量配置-end **/


export default {


    /**
     * 读取信息
     */
    get(key) {
        return sessionStorage.getItem(key)
    },
    
    
    /**
     * 添加信息
     */
    set(key, value) {
        sessionStorage.setItem(key, value)
    },

    
    /**
     * gateway网关验证信息处理(请求头)
     */
    gatewayRequest(config) {
        let key = true;
        whiteList.find(function (value) {
            if (value === config.url) {
                key = false;
            }
        });

        // 对非白名单请求进行处理
        if (key) {
            // 请求体数据
            let token = this.get("token")

            // 请求中增加token
            if (token) {
                config.headers.Authorization = token;
            }
            let s = this.get("sessionId")
            // 请求中增加会话信息
            if (s) {
                config.headers.s = s;
            }

            // 防重放请求通过信息
            if (replay) {
                this.replayAttack(config);
            }

            // 完整性校验信息
            if (reqIntegrity) {
                config.headers.c = this.calculateChecksum(config.method, config.url, config.params)
            }

            // 请求整体加密
            if (AESKey) {
                const secretKey = this.get("secretKey");
                let date = JSON.stringify(config.params);
                date = this.encryptAES(date, secretKey);
                config.params = {"data": date};
            }
            if (AESKey && RSAKey) {
                const serverPublicKey = this.get("serverPublicKey");
                let date = config.params.data;
                date = this.rsaEncrypt(date, serverPublicKey);
                config.params = {"data": date};
            } else if (RSAKey) {
                const serverPublicKey = this.get("serverPublicKey");
                let date = JSON.stringify(config.params);
                date = this.rsaEncrypt(date, serverPublicKey);
                config.params = {"data": date};
            }
        }

        return config;
    },


    /**
     * gateway网关验证信息处理(响应头)
     */
    gatewayResponse(response) {
        let key = true;

        // 放置业务逻辑代码
        // response是服务器端返回来的数据信息，与Promise获得数据一致
        let data = response.data
        // config包含请求信息
        let config = response.config

        // 判断 data 是否为对象
        if (typeof data === 'object' && data !== null) {
            // 判断 data 是否匹配特定格式
            if (
                Object.prototype.hasOwnProperty.call(data, 'msg') &&
                Object.prototype.hasOwnProperty.call(data, 'code') &&
                typeof data.msg === 'string' &&
                typeof data.code === 'number'
            ) {
                // 数据匹配特定格式
                if (data.code === 401) {
                    // Vue.prototype.$message.error(data.msg)
                    sessionStorage.clear()
                    // router.replace({path: '/login'}).then(() => {})

                }
                return data;
            }
        }


        // 获取当前请求的url
        let url = config.url
        whiteList.find(function (value) {
            if (value === url) {
                key = false;
            }
        });


        // 对非白名单数据进行处理
        if (key) {
            // 获取加密密钥，并传入解密组件进行解密
            if (RSAKey) {
                const privateKey = this.get("privateKey")
                data = this.rsaDecrypt(data, privateKey)
            }
            if (AESKey) {
                let securityKey = this.get("secretKey")
                data = this.decryptAES(data, securityKey)
            }

            if (data != null && data !== "") {
                data = JSON.parse(data);
            }else {
                data =  new Promise(() => {});
            }


        }

        return data;
    },


    //************************************网关通信-start
    // 与后台网关建立连接，需要请求 “/k” 接口， 拿到后端的公钥，存储。
    // 再请求 “/cn” 接口,保存与后端建立通信所需要的请求。

    /**
     * 用于网关请求 “/k” 请求后的处理
     *
     * @returns {{ck: (string|null), k: string}}
     */
    dealValidationMessage(data) {
       this.set("publicKey", data)
    },


    /**
     * 用于网关请求 “/cn” 请求前的处理
     *
     * @returns {{ck: (string|null), k: string}}
     */
    secureConnectionPrepare() {
        const publicKey = this.get("publicKey")
        const publicKeyMd5 = this.strToMd5(publicKey)
        let clientPublicKey = this.communication()
        clientPublicKey = this.rsaEncrypt(clientPublicKey, publicKey)
        return {
            "k": publicKeyMd5,
            "ck": clientPublicKey,
        };
    },


    /**
     * 用于网关请求 “/cn” 请求后的处理
     */
    secureConnection(data) {
        const privateKey = this.get("privateKey")
        data = this.rsaDecrypt(data, privateKey)
        data = JSON.parse(data)
       this.set("secretKey", data.secretKey)
       this.set("sessionId", data.sessionId)
       this.set("serverPublicKey", data.publicKey)
    },

    //************************************网关通信-end

    /**
     * 生成公钥私钥对保存本地，并返回公钥
     *
     * @returns {string}
     */
    communication() {
        const keys = this.rsaGenerateKey();
        const publicKey = keys.publicKey;
        const privateKey = keys.privateKey;
       this.set("privateKey", privateKey)

        return publicKey
    },


    /**
     * 整体加解密-加密数据
     * 并在加密数据之后进行相应的判断提示
     *
     * @param {String} key base64格式的密钥
     * @param {String} data 待加密的数据
     * @returns {String} 加密数据
     */
    encrypt(data, key) {
        let repData = this.decryptAES(data, key);

        if (repData !== null && repData !== "") {
            return JSON.parse(repData);
        } else {
            return ""
        }
    },


    /**
     * 整体加解密-解密数据
     * 并在解密失败之后进行相应的判断提示
     *
     * @param {String} key base64格式的密钥
     * @param {String} data 待解密的数据
     * @returns {String} 解密数据
     */
    decrypt(data, key) {
        let repData = this.decryptAES(data, key);

        if (repData !== null && repData !== "") {
            return JSON.parse(repData);
        } else {
            return ""
        }
    },


    /**
     * 防重放验证
     *
     * @param config Axios请求的配置对象
     */
    replayAttack(config) {
        let params = config.params;
        let reqData = config.params;
        let t = new Date().getTime();
        if (t) {
            config.headers.t = t

            if (reqData == null) {
                params = {
                    't': t,
                }
            } else {
                // params = JSON.parse(reqData)
                params["t"] = t;
            }

            const data = JSON.stringify(params);
            config.headers.n = this.gBase(data);
        }
    },


    /**
     * 防重放-生成请求签名(目前前端可以加密，但是无法解密，暂时弃用)
     *
     * @param data data 待解密的数据
     * @param t 请求时间戳
     */
    generateSignature(data, t) {
        if (data == null) {
            data = {
                'a': t,
                't': t,
            }
        } else {
            data["t"] = t;
            data["a"] = t;
        }

        let secretKey = this.get("secretKey")
        let hmac = crypto.createHmac('sha256', secretKey);
        hmac.update(JSON.stringify(data));
        return hmac.digest('base64');
    },


    /**
     * 完整性签名生成
     *
     * @param method 请求类型
     * @param path 请求地址
     * @param query 请求参数
     * @returns {*} 签名密钥
     */
    calculateChecksum(method, path, query) {
        let param
        if (query === null || query === undefined) {
            param = "{}"
        } else {
            param = JSON.stringify(query)
            param = param.replace(/\[|\]/g, '');
            param = param.replace(/"/g, '');
        }
        // 将请求的方法、路径和查询参数拼接成一个字符串
        // param = JSON.stringify(query)
        let data = `${method.toUpperCase()}${path}${param}`;
        // // 使用 HMAC-SHA256 计算校验值
        // const secretKey = 'your_secret_key'; // 替换为后端提供的密钥
        // const hmac = crypto.createHmac('sha256', secretKey);
        // hmac.update(data);
        // const checksum = hmac.digest('base64');
        return this.gBase(data);
    },


    //************************************公用加密方法-start
    /**
     * base64加密
     * @param data data 待加密数据
     */
    gBase(data) {
        return CryptoJS.enc.Base64.stringify(CryptoJS.enc.Utf8.parse(data));
    },


    /**
     * 将字符串取值MD5
     *
     * @param string 字符串对象
     * @returns {string} 字符串md5数值
     */
    strToMd5(string) {
        // 规定使用哈希算法中的MD5算法
        const hash = crypto.createHash('md5');

        // 可任意多次调用update(),效果相当于多个字符串相加
        hash.update(string);

        // hash.digest('hex')表示输出的格式为16进制
        return hash.digest('hex');
    },
    //************************************公用加密方法-end


    //************************************AES对称加解密-start


    /**
     * AES对称加密数据
     *
     * @param {String} data 待加密的数据
     * @param {String} base64Key base64格式的密钥
     * @returns {String} 加密后的数据
     */
    encryptAES(data, base64Key) {
        let encryptedBytes = null;
        if (data != null && base64Key != null) {
            const key = CryptoJS.enc.Base64.parse(base64Key);
            encryptedBytes = CryptoJS.AES.encrypt(data, key, {mode: CryptoJS.mode.ECB});
            encryptedBytes = encryptedBytes.toString();
        }
        return encryptedBytes;
    },


    /**
     * AES对称-解密数据
     *
     * @param {String} data 待解密的数据
     * @param {String} base64Key base64格式的密钥
     * @returns {String} 解密后的数据
     */
    decryptAES(data, base64Key) {
        let decryptData = null;

        if (data != null && base64Key != null) {
            const key = CryptoJS.enc.Base64.parse(base64Key)
            const decryptBytes = CryptoJS.AES.decrypt(data, key, {mode: CryptoJS.mode.ECB})
            decryptData = CryptoJS.enc.Utf8.stringify(decryptBytes);
        }

        return decryptData
    },
    //************************************AES对称加解密-end


    //************************************RSA非对称加解密-start
    /**
     * 非对称加解密-生成公钥与私钥
     */
    rsaGenerateKey() {
        let keys = {
            "publicKey": "",
            "privateKey": "",
        }

        // 创建 JSEncrypt 实例
        const encrypt = new JSEncrypt();

        // 生成密钥对（公钥和私钥）
        const keyPair = encrypt.getKey();

        // 获取公钥和私钥
        keys.publicKey = keyPair.getPublicBaseKeyB64();
        keys.privateKey = keyPair.getPrivateBaseKeyB64();

        return keys
    },


    /**
     * 非对称加解密-公钥加密信息(分段加密)
     *
     * @param string 内容
     * @param publicKey 非对称私钥
     * @returns {string | null}
     */
    rsaEncrypt(string, publicKey) {
        let encryptData = null;

        if (string != null && publicKey != null) {
            const encryptor = new JSEncrypt();
            encryptor.setPublicKey(publicKey);
            // 根据公钥的长度确定块大小，一般为公钥长度减去一些填充长度
            const blockSize = 117;
            const textLength = string.length;
            let encryptedBlocks = [];

            // 拆分长文本为块并逐个加密
            for (let i = 0; i < textLength; i += blockSize) {
                const block = string.substr(i, blockSize);
                const encryptedBlock = encryptor.encrypt(block);
                encryptedBlocks.push(encryptedBlock);
            }

            // 将加密的块合并为单个字符串
            encryptData = encryptedBlocks.join('');
        }

        return encryptData;
    },


    /**
     * 非对称加解密-私钥解密信息(分段解密)
     *
     * @param string 加密内容
     * @param privateKey 非对称私钥
     * @returns {string | null}
     */
    rsaDecrypt(string, privateKey) {
        let decryptData = null;
        if (string != null && privateKey != null) {

            const encryptor = new JSEncrypt();
            encryptor.setPrivateKey(privateKey);
            // 根据私钥的长度确定块大小，一般为私钥长度减去一些填充长度
            const blockSize = 172;
            const encryptedLength = string.length;
            let decryptedBlocks = [];

            // 拆分加密文本为块并逐个解密
            for (let i = 0; i < encryptedLength; i += blockSize) {
                const block = string.substr(i, blockSize);
                const decryptedBlock = encryptor.decrypt(block);
                decryptedBlocks.push(decryptedBlock);
            }
            decryptData = decryptedBlocks.join('')
        }

        // 将解密的块合并为单个字符串
        return decryptData;
    },
    //************************************RSA非对称加解密-end
}