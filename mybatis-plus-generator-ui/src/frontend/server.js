const {
  Nuxt,
  Builder
} = require('nuxt')
const app = require('express')()
const port = process.env.PORT || 3000
var proxyMiddleware = require('http-proxy-middleware')
// 传入配置初始化 Nuxt.js 实例
let config = require('./nuxt.config.js')
const nuxt = new Nuxt(config)

// proxy api requests这里就是添加的proxyTable中间价的设置了
var proxyTable = config.proxy
Object.keys(proxyTable).forEach(function (context) {
  var options = proxyTable[context]
  if (typeof options === 'string') {
    options = {
      target: options
    }
  }
  app.use(proxyMiddleware(options.filter || context, options))
})
app.use(nuxt.render) //这里是添加nuxt渲染层服务的中间件

// 在开发模式下进行编译

new Builder(nuxt).build()
  .catch((error) => {
    console.error(error)
    process.exit(1)
  })


// 监听指定端口
app.listen(port, '0.0.0.0')
console.log('服务器运行于 localhost:' + port)