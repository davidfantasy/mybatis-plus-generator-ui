const userMixin = {
  computed: {
    loginUser() {
      return this.$store.state.user
    }
  },
  methods: {
    isProcessEnded(taskKey) {
      if (!taskKey) {
        return true;
      }
      return "task_finished" === taskKey || "task_discard" === taskKey || "manual_finished" === taskKey;
    },
    isSameAssignee(assignee) {
      return this.$store.state.user.account === assignee;
    },
    hasRole(aRole) {
      let index = _.findIndex(this.$store.state.user.roles, (role) => {
        return role === aRole;
      });
      if (index === -1) {
        return false;
      }
      return true;
    },
    hasPerm: function (perm) {
      let allowed = false;
      if (this.loginUser.perms) {
        this.loginUser.perms.forEach(ownedPerm => {
          if (ownedPerm === perm || _.startsWith(perm, ownedPerm + ":")) {
            allowed = true
          }
        });
      }
      return allowed;
    },
    hasPerms(perms) {
      if (!Array.isArray(perms)) {
        return false;
      }
      let flag = true;
      perms.forEach(perm => {
        if (!this.hasPerm(perm)) {
          flag = false;
        }
      });
      return flag;
    }
  }
};
export default userMixin