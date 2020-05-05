const modules = [{
	title: "错误监控",
	iconClass: "iconfont iconyichang1",
	menus: [{
		title: "错误明细",
		viewPath: "/error-log"
	}]
}, {
	title: "访问监控",
	iconClass: "iconfont iconicon-archor",
	menus: [{
		title: "访问统计",
		viewPath: "/metric",
	}]
}, {
	title: "状态监控",
	iconClass: "iconfont iconzhuangtaijiankong",
	menus: [{
		title: "JVM状态监控",
		viewPath: "/jvm",
	}]
}]

export default modules;