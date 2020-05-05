import Vue from "vue";
import Pager from "@/components/Pager";
import Menu from "@/components/Menu";
import PageHeader from "@/components/PageHeader";
import _ from "lodash";
import moment from "moment";
import VeLine from 'v-charts/lib/line.common'


_.templateSettings.interpolate = /{{([\s\S]+?)}}/g;

Vue.component("pager", Pager);
Vue.component("index-menu", Menu);
Vue.component("page-header", PageHeader);

Vue.filter("tsformat", function (ts) {
    return moment(ts).format("YYYY-MM-DD h:mm:ss");
});

Vue.component(VeLine.name, VeLine)