import Vue from 'vue'

import {
	popover,
	Pagination,
	Dialog,
	Input,
	InputNumber,
	Switch,
	Select,
	DatePicker,
	TimeSelect,
	TimePicker,
	Option,
	OptionGroup,
	Button,
	ButtonGroup,
	Table,
	Radio,
	RadioButton,
	RadioGroup,
	TableColumn,
	Form,
	FormItem,
	Collapse,
	Tabs,
	TabPane,
	CollapseItem,
	Alert,
	Loading,
	MessageBox,
	Message,
	Notification,
	Cascader,
	Tree,
	Steps,
	Step,
	Row,
	Col,
	Upload,
	Checkbox,
	CheckboxGroup,
	CheckboxButton,
	Card,
	Autocomplete,
	Tooltip,
	Tag
} from 'element-ui';

Vue.use(Collapse);
Vue.use(CollapseItem);
Vue.use(Dialog);
Vue.use(Input);
Vue.use(Switch);
Vue.use(Select);
Vue.use(Option);
Vue.use(OptionGroup);
Vue.use(Button);
Vue.use(ButtonGroup);
Vue.use(Alert);
Vue.use(Radio);
Vue.use(RadioButton);
Vue.use(TimePicker);
Vue.use(TimeSelect);
Vue.use(DatePicker);
Vue.use(Cascader);
Vue.use(InputNumber);
Vue.use(Form);
Vue.use(FormItem);
Vue.use(Loading.directive);
Vue.use(Table);
Vue.use(Tabs);
Vue.use(TabPane);
Vue.use(TableColumn);
Vue.use(Tree);
Vue.use(Steps);
Vue.use(Step);
Vue.use(Row);
Vue.use(Col);
Vue.use(Upload);
Vue.use(Pagination);
Vue.use(Card);
Vue.use(popover);
Vue.use(Checkbox);
Vue.use(CheckboxGroup);
Vue.use(CheckboxButton);
Vue.use(Tooltip);
Vue.use(Autocomplete);
Vue.use(Tag);
Vue.use(RadioGroup);
Vue.prototype.$loading = Loading.service;
Vue.prototype.$msgbox = MessageBox;
Vue.prototype.$alert = MessageBox.alert;
Vue.prototype.$confirm = MessageBox.confirm;
Vue.prototype.$prompt = MessageBox.prompt;
Vue.prototype.$notify = Notification;
Vue.prototype.$message = Message;

import "@/assets/scss/element-variables.scss";