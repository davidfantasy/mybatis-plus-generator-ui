const listMixin = {
    data() {
        return {
            computedTableHeight: 0,
            listColumnSortOrder: {}
        }
    },
    mounted() {
        setTimeout(() => {
            if (window.screen.height == 1080) {
                this.computedTableHeight = window.screen.height - 522;
            } else if (window.screen.height == 900) {
                this.computedTableHeight = window.screen.height - 350;
            } else if (window.screen.height == 768) {
                this.computedTableHeight = window.screen.height - 340;
            } else {
                this.computedTableHeight = 450
            }
        }, 600)
    },
    methods: {
        handlerListSortChange({
            column,
            prop,
            order
        }) {
            let property = prop;
            if (this.listColumnPropMapping) {
                property = this.listColumnPropMapping[prop]
            }
            if (!property) {
                property = prop;
            }
            this.listColumnSortOrder = {};
            this.$set(this.listColumnSortOrder, property, order);
        }
    }
};
export default listMixin