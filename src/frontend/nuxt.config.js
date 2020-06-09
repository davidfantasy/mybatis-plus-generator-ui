const pkg = require("./package");
const path = require("path");

const contextPath = process.env.NODE_ENV === "production" ? "" : "";

const bindQuery2Props = (route) => {
  let props = _.assign({}, route.query, route.params);
  return props;
};

const setProps = (router) => {
  router.props = bindQuery2Props;
  if (router.children) {
    for (let i = 0; i < router.children.length; i++) {
      let child = router.children[i];
      setProps(child);
    }
  }
};

module.exports = {
  env: {
    contextPath: contextPath, //资源引用路径前缀
  },
  mode: "spa",
  srcDir: "src/",
  head: {
    script: [
      {
        src: contextPath + "/libs/jquery.min.js",
      },
      {
        src: contextPath + "/libs/bootstrap/js/bootstrap.min.js",
      },
      {
        src: contextPath + "/libs/adminlte/adminlte.min.js",
      },
      {
        src: contextPath + "/libs/jquery.fullscreen-min.js",
      },
    ],
    link: [
      {
        rel: "stylesheet",
        href: "//at.alicdn.com/t/font_1617194_0ttjsrqnukiq.css",
      },
    ],
  },
  router: {
    extendRoutes(routes, resolve) {
      for (let i = 0; i < routes.length; i++) {
        let element = routes[i];
        setProps(element);
      }
    },
    base: contextPath,
    scrollBehavior: function(to, from, savedPosition) {
      return {
        x: 0,
        y: 0,
      };
    },
  },
  /*
   ** Customize the progress-bar color
   */
  loading: {
    color: "#fff",
  },
  /*
   ** Global CSS
   */
  css: [
    //"@/assets/css/global.css",
    "@/static/libs/bootstrap/css/bootstrap.min.css",
    "@/static/libs/font-awesome/css/font-awesome.min.css",
    "@/static/libs/adminlte/AdminLTE.min.css",
  ],
  /*
   ** Plugins to load before mounting the App
   */
  plugins: ["@/plugins/element-ui", "@/plugins/axios", "@/plugins/components"],
  generate: {
    /*
     ** 指定编译后文件的输出目录
     */
    dir: path.resolve("../main/resources/generator-ui/"),
  },
  /*
   ** Build configuration
   */
  build: {
    babel: {
      presets({ isServer }) {
        const targets = {
          ie: "11",
        };
        return [
          [
            require.resolve("@nuxt/babel-preset-app"),
            {
              targets,
            },
          ],
        ];
      },
      plugins: [
        [
          "component",
          {
            libraryName: "element-ui",
            styleLibraryName: "theme-chalk",
          },
        ],
      ],
    },
    extend(config, { isDev, isClient }) {
      //source map设置
      if (isDev) {
        config.devtool = "cheap-module-eval-source-map";
      } else {
        config.devtool = "cheap-module-source-map";
      }
    },
  },
  //开发服务器代理设置
  proxy: {
    "/api": {
      target: "http://localhost:8068/",
      changeOrigin: true,
    },
  },
};
