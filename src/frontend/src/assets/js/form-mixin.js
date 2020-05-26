import Vue from 'vue'
const formMixin = {
    data: function () {
        return {
            submitingFlag: false
        }
    },
    methods: {
        submit() {
            if (this.submitingFlag) {
                return;
            }
            if (typeof this.doSubmit != 'function') {
                return;
            }
            this.submitingFlag = true;
            if (this.$refs["submitForm"]) {
                this.$refs["submitForm"].validate(valid => {
                    if (valid) {
                        const loading = this.$loading({
                            lock: true,
                            text: '正在提交信息，请稍候'
                        });
                        this.doSubmit();
                    } else {
                        this.submitingFlag = false;
                        return false;
                    }
                });
            } else {
                const loading = this.$loading({
                    lock: true,
                    text: '正在提交信息，请稍候'
                });
                this.doSubmit();
            }
        },
        afterSubmit(goback) {
            Vue.prototype.$loading().close();
            if (goback) {
                this.returnBack();
            } else {
                this.submitingFlag = false;
            }
        },
        returnBack() {
            window.history.back();
        }
    }
};
export default formMixin